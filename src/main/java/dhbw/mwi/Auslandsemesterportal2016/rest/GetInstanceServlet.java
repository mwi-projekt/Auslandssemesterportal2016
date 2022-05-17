package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "GetInstanceServlet", urlPatterns = { "/getInstance" })
public class GetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			int matnr = Integer.parseInt(request.getParameter("matnr"));
			String uni = request.getParameter("uni");
			int prio = Integer.parseInt(request.getParameter("prio"));
			String model = SQLQueries.getmodel(uni);

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			// Holt instanceId aus DB
			String instance_id = SQLQueries.getInstanceId(matnr, uni);
			if (instance_id == "") {
				// Lege neue Instanz an
				ProcessInstance instance = runtime.startProcessInstanceByKey(model);
				instance_id = instance.getId();
				String[] user = SQLQueries.getUserData(matnr);
				if (user.length > 0) {
					runtime.setVariable(instance_id, "bewNachname", user[0]);
					runtime.setVariable(instance_id, "bewVorname", user[1]);
					runtime.setVariable(instance_id, "bewEmail", user[2]);
					runtime.setVariable(instance_id, "matrikelnummer", matnr);
					runtime.setVariable(instance_id, "aktuelleUni", user[3]);
					runtime.setVariable(instance_id, "bewStudiengang", user[4]);
					runtime.setVariable(instance_id, "bewKurs", user[5]);
					runtime.setVariable(instance_id, "prioritaet", prio);
				}
				runtime.setVariable(instance_id, "uni", uni);
				SQLQueries.createInstance(instance_id, uni, matnr, prio, 10);
			}

			JsonObject json = new JsonObject();
			json.addProperty("instanceId", instance_id);
			json.addProperty("uni", uni);
			Util.writeJson(response, json);
		}
	}
}

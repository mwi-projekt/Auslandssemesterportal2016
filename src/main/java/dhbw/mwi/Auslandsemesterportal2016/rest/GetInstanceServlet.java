package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "GetInstanceServlet", urlPatterns = { "/getInstance" })
public class GetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(401);
			return;
		}

		int matnr;
		int prio;
		try {
			matnr = Integer.parseInt(request.getParameter("matnr"));
			prio = Integer.parseInt(request.getParameter("prio"));
		} catch (NumberFormatException numberFormatException) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}
		String uni = request.getParameter("uni");
		if (null == uni || "".equals(uni)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}


		String instanceId = SQLQueries.getInstanceId(matnr, uni);
		if ("".equals(instanceId)) { // noch keine Instanz vorhanden -> neue anlegen
			String model = SQLQueries.getModel(uni);

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			ProcessInstance instance = runtime.startProcessInstanceByKey(model);
			instanceId = instance.getId();

			String[] user = SQLQueries.getUserData(matnr);
			if (user.length == 6) {
				runtime.setVariable(instanceId, "bewNachname", user[0]);
				runtime.setVariable(instanceId, "bewVorname", user[1]);
				runtime.setVariable(instanceId, "bewEmail", user[2]);
				runtime.setVariable(instanceId, "matrikelnummer", matnr);
				runtime.setVariable(instanceId, "aktuelleUni", user[3]);
				runtime.setVariable(instanceId, "bewStudiengang", user[4]);
				runtime.setVariable(instanceId, "bewKurs", user[5]);
				runtime.setVariable(instanceId, "prioritaet", prio);
			}
			runtime.setVariable(instanceId, "uni", uni);
			SQLQueries.createInstance(instanceId, uni, matnr, prio, 10);
		}

		JsonObject json = new JsonObject();
		json.addProperty("instanceId", instanceId);
		json.addProperty("uni", uni);
		Util.writeJson(response, json);
	}
}

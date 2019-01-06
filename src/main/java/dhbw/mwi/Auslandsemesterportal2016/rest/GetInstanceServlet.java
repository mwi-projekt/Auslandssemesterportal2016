package dhbw.mwi.Auslandsemesterportal2016.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetInstanceServlet", urlPatterns = { "/getInstance" })
public class GetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			int matnr = Integer.parseInt(request.getParameter("matnr"));
			String uni = request.getParameter("uni");
			String model = SQL_queries.getmodel(uni);

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			// Holt instanceId aus DB
			String instance_id = SQL_queries.getInstanceId(matnr, uni);
			if (instance_id == "") {
				// Lege neue Instanz an
				ProcessInstance instance = runtime.startProcessInstanceByKey(model);
				instance_id = instance.getId();
				String[] user = SQL_queries.getUserData(matnr);
				if (user.length > 0) {
					runtime.setVariable(instance_id, "bewNachname", user[0]);
					runtime.setVariable(instance_id, "bewVorname", user[1]);
					runtime.setVariable(instance_id, "bewEmail", user[2]);
					runtime.setVariable(instance_id, "matrikelnummer", matnr);
					runtime.setVariable(instance_id, "aktuelleUni", user[3]);
					runtime.setVariable(instance_id, "bewStudiengang", user[4]);
					runtime.setVariable(instance_id, "bewKurs", user[5]);
				}
				runtime.setVariable(instance_id, "uni", uni);
				SQL_queries.createInstance(instance_id, uni, matnr, 10);
			}

			JSONObject json = new JSONObject();
			json.put("instanceId", instance_id);
			json.put("uni", uni);
			Util.writeJson(response, json);
		}
	}
}

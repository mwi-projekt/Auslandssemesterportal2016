package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetSGLTasksServlet", urlPatterns = { "/getSGLTasks" })
public class GetSGLTasksServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2 && rolle != 4) {
			response.sendError(401, "Rolle: " + rolle);
		} else {

			JsonObject json = new JsonObject();
			JsonArray arr = new JsonArray();

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			List<ProcessInstance> results = runtime.createProcessInstanceQuery().list();
			for (int i = 0; i < results.size(); i++) {
				String instanceId = results.get(i).getId();
				List<String> activities = runtime.getActiveActivityIds(instanceId);
				JsonObject obj = new JsonObject();
				System.out.println(activities);
				if (activities.get(0).equals("abgeschlossen") || activities.get(0).equals("datenValidierenSGL") || activities.get(0).equals("datenValidieren") || activities.get(0).equals("abgelehnt")) {
					obj.addProperty("id", instanceId);
					obj.addProperty("name", runtime.getVariable(instanceId, "bewNachname").toString());
					obj.addProperty("vname", runtime.getVariable(instanceId, "bewVorname").toString());
					obj.addProperty("aktuelleUni", runtime.getVariable(instanceId, "aktuelleUni").toString());
					obj.addProperty("kurs", runtime.getVariable(instanceId, "bewKurs").toString());
					obj.addProperty("uni", runtime.getVariable(instanceId, "uni").toString());
					obj.addProperty("matrikelnummer", runtime.getVariable(instanceId, "matrikelnummer").toString());
					if (activities.get(0).equals("abgeschlossen")) {
						obj.addProperty("status", "complete");
					} else if (activities.get(0).equals("datenValidierenSGL")){
						obj.addProperty("status", "validateSGL");
					} else if (activities.get(0).equals("abgelehnt")) {
						obj.addProperty("status", "abgelehnt");
					} else {
						obj.addProperty("status", "validate");
					}
					arr.add(obj);
				}
			}		
			System.out.println(arr);
			json.add("data", arr);
			Util.writeJson(response, json);
		}
	}
}

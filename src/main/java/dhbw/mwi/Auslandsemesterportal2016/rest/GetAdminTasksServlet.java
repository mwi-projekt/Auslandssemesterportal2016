package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "GetAdminTasksServlet", urlPatterns = { "/getTasks" })
public class GetAdminTasksServlet extends HttpServlet {

	private static final String STATUS = "status";
	private static final String STATUS_ABGESCHLOSSEN = "abgeschlossen";
	private static final String STATUS_ABGELEHNT = "abgelehnt";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2 && rolle != 4) {
			response.sendError(SC_UNAUTHORIZED, "Rolle: " + rolle);
			return;
		}

		JsonArray arr = new JsonArray();
		collectJsonRelevantData(arr);

		JsonObject json = new JsonObject();
		json.add("data", arr);
		Util.writeJson(response, json);
	}

	private void collectJsonRelevantData(JsonArray arr) {
		RuntimeService runtime = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		List<ProcessInstance> results = runtime.createProcessInstanceQuery().list();

		for (ProcessInstance result : results) {
			String instanceId = result.getId();
			List<String> activities = runtime.getActiveActivityIds(instanceId);
			JsonObject obj = new JsonObject();

			if (activities.get(0).equals(STATUS_ABGESCHLOSSEN) || activities.get(0).equals("datenValidieren") || activities.get(0).equals("datenValidierenSGL") || activities.get(0).equals(STATUS_ABGELEHNT)) {
				addProcessInstanceDataToJsonObject(runtime, instanceId, activities, obj);
				arr.add(obj);
			}
		}
	}

	private void addProcessInstanceDataToJsonObject(RuntimeService runtime, String instanceId, List<String> activities, JsonObject obj) {
		obj.addProperty("id", instanceId);
		obj.addProperty("name", runtime.getVariable(instanceId, "bewNachname").toString());
		obj.addProperty("vname", runtime.getVariable(instanceId, "bewVorname").toString());
		obj.addProperty("aktuelleUni", runtime.getVariable(instanceId, "aktuelleUni").toString());
		obj.addProperty("kurs", runtime.getVariable(instanceId, "bewKurs").toString());
		obj.addProperty("uni", runtime.getVariable(instanceId, "uni").toString());
		obj.addProperty("prioritaet", runtime.getVariable(instanceId, "prioritaet").toString());
		obj.addProperty("matrikelnummer", runtime.getVariable(instanceId, "matrikelnummer").toString());
		addStatusToJsonObject(activities, obj);
	}

	private void addStatusToJsonObject(List<String> activities, JsonObject obj) {
		switch (activities.get(0)) {
			case STATUS_ABGESCHLOSSEN:
				obj.addProperty(STATUS, "complete");
				break;
			case "datenValidieren":
				obj.addProperty(STATUS, "validate");
				break;
			case "datenValidierenSGL":
				obj.addProperty(STATUS, "validateSGL");
				break;
			case STATUS_ABGELEHNT:
				obj.addProperty(STATUS, STATUS_ABGELEHNT);
				break;
			default:
				break;
		}
	}
}

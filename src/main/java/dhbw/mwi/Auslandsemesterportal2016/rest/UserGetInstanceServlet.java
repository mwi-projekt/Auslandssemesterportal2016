package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/getUserInstances" })
public class UserGetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.setResponseHeaders(request,response);

		int matnr = Integer.parseInt(request.getParameter("matnr"));

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		String reply = "";
		// Holt instanceId aus DB
		ArrayList<String[]> instances = SQL_queries.getUserInstances(matnr);
		JsonObject json = new JsonObject();
		JsonArray data = new JsonArray();

		for (int i = 0; i < instances.size(); i++) {
			JsonObject row = new JsonObject();
			String[] listEntry = instances.get(i);
			String uni = listEntry[0];
			String instanceID = listEntry[1];
			String stepCounter = "";
			List<String> activityList;
			try {
				activityList = runtime.getActiveActivityIds(instanceID);
				if (activityList.get(0).equals("abgeschlossen")) {
					// Prozess abgeschlossen
					stepCounter = "Abgeschlossen";
				} else {
					String currentActivity = activityList.get(0);
					if (currentActivity.equals("datenPruefen")) {
						stepCounter = "Daten prüfen";
					} else if (currentActivity.equals("datenValidieren") || currentActivity.equals("datenValidierenSGL")) {
						stepCounter = "Auf Rückmeldung warten";
					} else if (currentActivity.equals("abgelehnt")) {
						stepCounter = "Bewerbung wurde abgelehnt";
					} else {
						// Rufe Schrittzahl aus Tabelle ab
						stepCounter = SQL_queries.getStepCounter(currentActivity, "studentBewerben");
					}
				}
				row.addProperty("instanceID", instanceID);
				row.addProperty("uni", uni);
				row.addProperty("stepCounter", stepCounter);
				data.add(row);
			} catch (Exception e) {

			}
		}
		json.add("data", data);
		Util.writeJson(response, json);
	}
}

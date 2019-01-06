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
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/getUserInstances" })
public class UserGetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int matnr = Integer.parseInt(request.getParameter("matnr"));

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		String reply = "";
		// Holt instanceId aus DB
		ArrayList<String[]> instances = SQL_queries.getUserInstances(matnr);
		JSONObject json = new JSONObject();
		JSONArray data = new JSONArray();

		for (int i = 0; i < instances.size(); i++) {
			JSONObject row = new JSONObject();
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
					} else if (currentActivity.equals("datenValidieren")) {
						stepCounter = "Auf Rückmeldung warten";
					} else {
						// Rufe Schrittzahl aus Tabelle ab
						stepCounter = SQL_queries.getStepCounter(currentActivity, "studentBewerben");
					}
				}
				row.put("instanceID", instanceID);
				row.put("uni", uni);
				row.put("stepCounter", stepCounter);
				data.put(row);
			} catch (Exception e) {

			}
		}
		json.put("data", data);
		Util.writeJson(response, json);
	}
}

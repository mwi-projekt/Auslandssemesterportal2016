package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(urlPatterns = { "/getUserInstances" })
public class UserGetInstanceServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1 || rolle > 4) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		int matrikelnummer;
		try {
			matrikelnummer = Integer.parseInt(request.getParameter("matnr"));
		} catch (NumberFormatException e) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		// Holt instanceId aus DB
		ArrayList<String[]> instances = SQLQueries.getUserInstances(matrikelnummer);
		JsonArray data = collectJsonRelevantData(instances);

		JsonObject json = new JsonObject();
		json.add("data", data);
		Util.writeJson(response, json);
	}

	private JsonArray collectJsonRelevantData(ArrayList<String[]> instances) {
		RuntimeService runtime = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		JsonArray data = new JsonArray();

		for (String[] instance : instances) {
			JsonObject row = new JsonObject();
			String currentActivity = runtime.getActiveActivityIds(instance[1]).get(0);

			row.addProperty("instanceID", instance[1]);
			row.addProperty("uni", instance[0]);
			row.addProperty("stepCounter", getNameOfStep(currentActivity));
			row.addProperty("prioritaet", instance[2]);
			data.add(row);
		}
		return data;
	}

	private String getNameOfStep(String currentActivity) {
		String stepCounter;
		switch (currentActivity) {
			case "abgeschlossen":
				stepCounter = "Abgeschlossen";
				break;
			case "datenPruefen":
				stepCounter = "Daten prüfen";
				break;
			case "datenValidieren":
			case "datenValidierenSGL":
				stepCounter = "Auf Rückmeldung warten";
				break;
			case "abgelehnt":
				stepCounter = "Bewerbung wurde abgelehnt";
				break;
			default:
				// Rufe Schrittzahl aus Tabelle ab
				stepCounter = SQLQueries.getStepCounter(currentActivity, "standard");
				break;
		}
		return stepCounter;
	}
}

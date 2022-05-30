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

import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "GetCurrentActivityServlet", urlPatterns = { "/currentActivity" })
public class GetCurrentActivityServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			String instanceID = request.getParameter("instance_id");
			String uni = request.getParameter("uni");
			String model = SQLQueries.getModel(uni);
			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();

			if (instanceID != null) {
				List<String> activitiesList = runtime.getActiveActivityIds(instanceID);
				String activeActivity = activitiesList.get(0);
				JsonObject json = new JsonObject();
				json.addProperty("active", activeActivity);
				json.addProperty("data", model);
				Util.writeJson(response, json);
			}
		}
	}
}

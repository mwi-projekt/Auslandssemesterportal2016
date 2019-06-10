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
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetCurrentActivityServlet", urlPatterns = { "/currentActivity" })
public class GetCurrentActivityServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		/*if (rolle < 1) {
			response.sendError(401);
		} else {*/
			String instanceID = request.getParameter("instance_id");
			String uni = request.getParameter("uni");
			String model = SQL_queries.getmodel(uni);
			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();

			if (instanceID != null) {
				List<String> activitiesList = runtime.getActiveActivityIds(instanceID);
				String activeActivity = activitiesList.get(0);
				JSONObject json = new JSONObject();
				json.put("active", activeActivity);
				json.put("data", model);
				Util.writeJson(response, json);
			}
		}
	}
//}

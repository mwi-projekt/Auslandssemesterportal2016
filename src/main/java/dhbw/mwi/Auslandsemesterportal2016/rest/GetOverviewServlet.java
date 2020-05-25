package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetOverviewServlet", urlPatterns = { "/getOverview" })
public class GetOverviewServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			response.setCharacterEncoding("UTF-8");
			String definition = request.getParameter("definition"); // Process Definition Key aus Camunda

			String activityString = SQL_queries.getAllActivities(definition);
			if (activityString.length() == 0) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			}
			activityString = activityString.substring(0, activityString.length() - 1);
			String[] activities = activityString.split(";");

			JsonArray arr = new JsonArray();

			for (int i = 0; i < activities.length; i++) {
				ResultSet rs = SQL_queries.getJson(activities[i], definition);
				try {
					rs.next();
					JsonObject line = new JsonObject();
					line.addProperty("activity", activities[i]);
					line.addProperty("data", rs.getString("json"));
					arr.add(line);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}

			JsonObject json = new JsonObject();
			json.add("data", arr);
			Util.writeJson(response, json);
		}
	}
}

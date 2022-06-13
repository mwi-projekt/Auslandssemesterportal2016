package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.exception.NullValueException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "GetCurrentActivityServlet", urlPatterns = { "/currentActivity" })
public class GetCurrentActivityServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(401);
			return;
		}

		String instanceID = request.getParameter("instance_id");
		String uni = request.getParameter("uni");
		if ((null == instanceID) || "".equals(instanceID) || (null == uni) || "".equals(uni)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		String model = SQLQueries.getModel(uni);

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		List<String> activitiesList;
		try {
			activitiesList = runtime.getActiveActivityIds(instanceID);
		} catch (NullValueException nullValueException) {
			response.sendError(SC_BAD_REQUEST, "no such instanceId");
			return;
		}
		String activeActivity = activitiesList.get(0);

		JsonObject json = new JsonObject();
		json.addProperty("active", activeActivity);
		json.addProperty("data", model);
		Util.writeJson(response, json);
	}
}

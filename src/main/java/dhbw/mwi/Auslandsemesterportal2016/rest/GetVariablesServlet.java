package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "GetVariablesServlet", urlPatterns = { "/getVariables" })
public class GetVariablesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(401);
		} else {
			String instanceID = request.getParameter("instance_id");
			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();

			JsonObject json = new JsonObject();

			Map<String, Object> variables = runtime.getVariables(instanceID);
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				Object obj = entry.getValue();
				if (obj != null) {
					json.addProperty(entry.getKey(), obj.toString());
				}
			}
			Util.writeJson(response, json);
		}
	}
}

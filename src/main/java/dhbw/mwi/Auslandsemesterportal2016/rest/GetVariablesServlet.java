package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetVariablesServlet", urlPatterns = { "/getVariables" })
public class GetVariablesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(401);
		} else {
			String instanceID = request.getParameter("instance_id");
			String key = request.getParameter("key");
			String[] keys = key.split("\\|", -1);
			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();

			JsonObject json = new JsonObject();

			for (int i = 0; i < keys.length; i++) {
				Object obj = runtime.getVariable(instanceID, keys[i]);
				if (obj != null) {
					json.addProperty(keys[i], obj.toString());
				}
			}
			Util.writeJson(response, json);
		}
	}
}

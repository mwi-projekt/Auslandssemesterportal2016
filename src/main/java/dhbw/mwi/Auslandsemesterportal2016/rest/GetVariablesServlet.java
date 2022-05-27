package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "GetVariablesServlet", urlPatterns = { "/getVariables" })
public class GetVariablesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		String instanceID = request.getParameter("instance_id");
		if (instanceID == null || "".equals(instanceID)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

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

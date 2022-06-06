package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum.UPDATEINSTANCE;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "UpdateInstanceServlet", urlPatterns = { "/setVariable" })
public class UpdateInstanceServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		PrintWriter toClient = response.getWriter();

		String instanceID = request.getParameter("instance_id");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		if (key == null || value == null) {
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
			toClient.print("Variables not set");
			return;
		}

		String[] keys = key.split("\\|", -1);
		String[] values = value.split("\\|", -1);
		String[] types = type.split("\\|", -1);

		Map<String, Object> variables = addVariablesToMap(keys, values, types);

		TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
		taskService.complete(
				taskService.createTaskQuery().processInstanceId(instanceID).singleResult().getId(),
				variables);
		toClient.println(UPDATEINSTANCE);
	}

	private Map<String, Object> addVariablesToMap(String[] keys, String[] values, String[] types) {
		Map<String, Object> variables = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			// runtime.setVariable(instance.getId(), keys[i], values[i]);
			switch (types[i]) {
				case "text":
				case "email":
					variables.put(keys[i], values[i]);
					break;
				case "number":
					if (values[i].equals("")) {
						values[i] = "0";
					}
					variables.put(keys[i], Integer.parseInt(values[i]));
					break;
				case "boolean":
					variables.put(keys[i], Boolean.parseBoolean(values[i]));
					break;
			}
		}
		return variables;
	}
}

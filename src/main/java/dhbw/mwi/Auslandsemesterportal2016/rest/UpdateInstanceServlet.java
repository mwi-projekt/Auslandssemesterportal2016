package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;

@WebServlet(name = "UpdateInstanceServlet", urlPatterns = { "/setVariable" })
public class UpdateInstanceServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			PrintWriter toClient = response.getWriter();

			String instanceID = request.getParameter("instance_id");
			String key = request.getParameter("key");
			String val = request.getParameter("value");
			String type = request.getParameter("type");

			if (key != null && val != null) {
				String[] keys = key.split("\\|", -1);
				String[] vals = val.split("\\|", -1);
				String[] types = type.split("\\|", -1);
				Map<String, Object> vars = new HashMap<String, Object>();
				ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

				for (int i = 0; i < keys.length; i++) {
					// runtime.setVariable(instance.getId(), keys[i], vals[i]);
					if (types[i].equals("text")) {
						vars.put(keys[i], vals[i]);
					} else if (types[i].equals("number")) {
						if (vals[i].equals("")) {
							vals[i] = "0";
						}
						vars.put(keys[i], Integer.parseInt(vals[i]));
					} else if (types[i].equals("email")) {
						vars.put(keys[i], vals[i]);
					} else if (types[i].equals("boolean")) {
						vars.put(keys[i], Boolean.parseBoolean(vals[i]));
					}
				}
				engine.getTaskService().complete(
						engine.getTaskService().createTaskQuery().processInstanceId(instanceID).singleResult().getId(),
						vars);
				toClient.println(SuccessEnum.UPDATEINSTANCE);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.print("Variables not set");
			}
		}
	}
}

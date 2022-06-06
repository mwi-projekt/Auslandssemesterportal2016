package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
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

@WebServlet(urlPatterns = { "/changePriority" })
public class ChangePriorityServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		String instanceId = request.getParameter("instance");
		if (instanceId == null || "".equals(instanceId)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		int newPrio;
		try {
			newPrio = Integer.parseInt(request.getParameter("prio"));
		} catch (NumberFormatException numberFormatException) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		int matrikelnummer = Integer.parseInt(UserAuthentification.getUserInfo(request).matrikelnummer);
		ArrayList<String[]> instances = SQLQueries.getUserInstances(matrikelnummer);
		String found = null;
		int oldPrio = 0;
		for (String[] instance:instances) {
			int prio = Integer.parseInt(instance[2]);
			String id = instance[1];
			if (prio == newPrio) {
				found = id;
			}
			if (instanceId.equals(id)) {
				oldPrio = prio;
			}
		}

		updatePrio(instanceId, newPrio);
		if (found != null) {
			updatePrio(found, oldPrio);
		}
	}

	private void updatePrio(String id, int prio) {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		runtime.setVariable(id, "prioritaet", prio);
		String query = "UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?";
		String[] params = new String[]{"" + prio, id};
		String[] types = new String[]{"int", "String"};
		SQLQueries.executeUpdate(query, params, types);
	}

}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.User;
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

@WebServlet(urlPatterns = { "/changePriority" })
public class ChangePriorityServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle < 1) {
			response.sendError(401);
		} else {
			User user = UserAuthentification.getUserInfo(request);
			int matnr = Integer.parseInt(user.matrikelnummer);
			String instance_id = request.getParameter("instance");
			int newPrio = Integer.parseInt(request.getParameter("prio"));
			ArrayList<String[]> instances = SQLQueries.getUserInstances(matnr);
			String found = null;
			int oldPrio = 0;
			for (int i = 0; i < instances.size(); i++) {
				String[] listEntry = instances.get(i);
				int prio = Integer.parseInt(listEntry[2]);
				String id = listEntry[1];
				if (prio == newPrio) {
					found = listEntry[1];
				}
				if (instance_id.equals(id)) {
					oldPrio = prio;
				}
			}

			updatePrio(instance_id, newPrio);
			if (found != null) {
				updatePrio(found, oldPrio);
			}
		}
	}

	private int updatePrio(String id, int prio) {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		runtime.setVariable(id, "prioritaet", prio);
		String query_ = "UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?";
		String[] params_ = new String[]{"" + prio, id};
		String[] types_ = new String[]{"int", "String"};
		return SQLQueries.executeUpdate(query_, params_, types_);
	}

}

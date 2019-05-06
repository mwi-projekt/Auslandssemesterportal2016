package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "TaskDeleteServlet", urlPatterns = { "/task/delete" })
public class TaskDeleteServlet extends HttpServlet {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle !=2) {
			response.sendError(401, "Rolle: " + rolle);
		} else {
			String taskId = request.getParameter("taskId");
			PrintWriter toClient = response.getWriter();

			if (taskId != null) {
				try {
					processEngine.getRuntimeService().deleteProcessInstance(taskId, "Die Bewerbung wurde vom AAA gelöscht");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: parameter are missing");
			}
		}
	}
}

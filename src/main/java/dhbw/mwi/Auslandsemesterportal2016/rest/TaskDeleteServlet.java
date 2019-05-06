package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
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
			
			String matrikelnummer = request.getParameter("matrikelnummer");
			String uni = request.getParameter("uni");
			PrintWriter toClient = response.getWriter();

			if (matrikelnummer != null && uni != null) {
				String id = ProcessService.getProcessId(matrikelnummer, uni);

				if (id != null && id != "leer") {
					Connection connection = DB.getInstance();

					// ProzessInstanz löschen
					try {
						processEngine.getRuntimeService().deleteProcessInstance(id,
								"Bewerbung wurde von Studenten beendet");
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
}

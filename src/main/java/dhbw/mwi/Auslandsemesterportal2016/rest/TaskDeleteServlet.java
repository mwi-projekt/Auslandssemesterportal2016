package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.*;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "TaskDeleteServlet", urlPatterns = { "/task/delete" })
public class TaskDeleteServlet extends HttpServlet {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 2) {
			response.sendError(401, "Rolle: " + rolle);
			return;
		}

		String matrikelnummer = request.getParameter("matrikelnummer");
		String uni = request.getParameter("uni");
		PrintWriter toClient = response.getWriter();

		if (matrikelnummer == null || uni == null) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		String id = ProcessService.getProcessId(matrikelnummer, uni);
		if (id == null || id == "leer") {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			toClient.println(PARAMMISSING);
			return;
		}

		// ProzessInstanz löschen
		try {
			processEngine.getRuntimeService().deleteProcessInstance(id,
					"Bewerbung wurde von Studenten beendet");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

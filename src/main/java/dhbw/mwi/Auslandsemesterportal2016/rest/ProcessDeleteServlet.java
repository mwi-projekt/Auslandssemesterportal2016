package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "ProcessDeleteServlet", urlPatterns = { "/process/delete" })
public class ProcessDeleteServlet extends HttpServlet {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int role = userAuthentification.isUserAuthentifiedByCookie(request);

		if (role < 1) {
			response.sendError(401);
		} else {
			String matrnumber = request.getParameter("matrnumber");
			String uni = request.getParameter("uni");
			PrintWriter toClient = response.getWriter();

			if (matrnumber != null && uni != null) {
				String id = ProcessService.getProcessId(matrnumber, uni);

				if (id != null && id != "leer") {
					Connection connection = DB.getInstance();

					// ProzessInstanz löschen
					try {
						processEngine.getRuntimeService().deleteProcessInstance(id,
								"Bewerbung wurde von Studenten beendet");
					} catch (Exception e) {
						e.printStackTrace();
					}

					// SQL-Befehl für das Löschen der Bewerbung aus der MySQL-DB
					//TODO: Split up prepared Statement for database refactoring (new Attributes: User.matrNumber, Application.startDate, ForeignUniversity.name, Step.number, Offering.totalSteps rest ist eliminated)

					String deleteStatement = "DELETE FROM bewerbungsprozess WHERE matrNumber = '" + matrnumber
							+ "' AND uniName = '" + uni + "' ";

					String deleteStatement2 = "DELETE FROM MapUserInstanz WHERE matrNumber = '" + matrnumber
							+ "' AND uni = '" + uni + "' ";

					try {
						Statement statement = connection.createStatement();
						statement.executeUpdate(deleteStatement);
						statement.executeUpdate(deleteStatement2);
						statement.close();
					} catch (SQLException e) {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						e.printStackTrace();
					}

					toClient.println(id);

				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					toClient.println("Error: can not find process");
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: parameter are missing");
			}
		}
	}
}

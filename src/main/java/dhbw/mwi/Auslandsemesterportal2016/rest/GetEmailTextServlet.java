package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetEmailTextServlet", urlPatterns = { "/getMailText" })
public class GetEmailTextServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2 && rolle != 4) {
			response.sendError(401);
		} else {
			response.setCharacterEncoding("UTF-8");
			PrintWriter toClient = response.getWriter();

			String instanceID = request.getParameter("instance_id");
			String validation_result = request.getParameter("validate");

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			String student_name = runtime.getVariable(instanceID, "bewNachname").toString();
			String uni = runtime.getVariable(instanceID, "uni").toString();
			String output = "";

			if (rolle == 2) {

				if (validation_result.equals("true")) {
					// Text für erfolgreiche Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "
							+ uni + " wurde erfolgreich an das Akademisches Auslandsamt versendet." + "\n" + "\n"
							+ "-- Platzhalter für Anmerkungen des Auslandsamts --" + "\n" + "\n"
							+ "Im nächsten Schritt wird sich ein Mitarbeiter zeitnah um die Bearbeitung Ihrer Bewerbung kümmern und	entscheiden, ob Sie in die engere Auswahl potentieller Bewerber kommen."
							+ "\n"
							+ "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de."
							+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
							+ "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Akademisches Auslandsamt";
				} else if (validation_result.equals("false")) {
					// Text für abgelehnte Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni + "\n"
							+ "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
							+ "Folgende Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de."
							+ "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n"
							+ "\n" + "Ihr Akademisches Auslandsamt";
				} else if (validation_result.equals("edit")){
					// Text für Bewerbung zur Bearbeitung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni + "\n"
							+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
							+ "Folgende Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können."
							+ "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de."
							+ "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n"
							+ "\n" + "Ihr Akademisches Auslandsamt";
				}

			} else if (rolle == 3){
				//Hier muss eine Mail an den SGL generiert werden, wenn der Student die Bewerbung abschickt
			}
		      else if (rolle == 4) {
				if (validation_result.equals("true")) {
					// Text für erfolgreiche Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "
							+ uni + " wurde erfolgreich durch ihren Studiengangsleiter validiert." + "\n" + "\n"
							+ "-- Platzhalter für Anmerkungen des Studiengangsleiters --" + "\n" + "\n" + "\n"
							+ "Im nächsten Schritt wird ihre Bewerbung an einen Mitarbeiter des Akademischen Auslandsamtes für einen weiteren Validierungsprozess übergeben."
							+ "\n"
							+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
							+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de."
							+ "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Studiengangsleiter/in";;
				} else if (validation_result.equals("false")) {
					// Text für abgelehnte Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni + "\n"
							+ "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
							+ "Folgende Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de."
							+ "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n"
							+ "\n" + "Ihr Studiengangsleiter/in";
				} else {
					// Text für Bewerbung zur Bearbeitung
					output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni + "\n"
							+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
							+ "Folgende Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können."
						 	+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de."
							+ "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n"
							+ "\n" + "Ihr Studiengangsleiter/in";
				}
			}
			toClient.print(output);
		}
	}
}

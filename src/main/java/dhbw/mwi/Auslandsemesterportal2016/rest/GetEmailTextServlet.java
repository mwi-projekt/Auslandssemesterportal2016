package dhbw.mwi.Auslandsemesterportal2016.rest;

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
import java.io.PrintWriter;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "GetEmailTextServlet", urlPatterns = { "/getMailText" })
public class GetEmailTextServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle != 1 && rolle != 2 && rolle != 4) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		response.setCharacterEncoding("UTF-8");
		PrintWriter toClient = response.getWriter();

		String instanceID = request.getParameter("instance_id");
		String validationResult = request.getParameter("validate");
		if (instanceID == null || "".equals(instanceID) || validationResult == null || "".equals(validationResult)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		String studentName = runtime.getVariable(instanceID, "bewNachname").toString();
		String uni1 = runtime.getVariable(instanceID, "uni1").toString();
		String uni2 = runtime.getVariable(instanceID, "uni2").toString();
		String uni3 = runtime.getVariable(instanceID, "uni3").toString();

		String output = "";
		String vpnDHBW = "\n" + "\n" + "************" + "\n"
				+ "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";

		if (rolle == 2) {

			switch (validationResult) {
				case "true":
					// Text für erfolgreiche Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot für die Universitäten: "
							+ "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n" + "wurde erfolgreich an das Akademisches Auslandsamt versendet." + "\n" + "\n"
							+ "-- Platzhalter für Anmerkungen des Auslandsamts --" + "\n" + "\n"
							+ "Zeitnah nach der Anmeldefrist werden alle Bewerbungen gesichtet und entschieden, ob Sie in die engere Auswahl potentieller Bewerber kommen. Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per E-Mail über das Ergebnis informieren."
							+ "\n"
							+ " Bei Fragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de."
							+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
							+ "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Akademisches Auslandsamt"
							+ "\n" + "\n" + vpnDHBW;
					break;
				case "false":
					// Text für abgelehnte Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n"
							+ "\n" + "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
							+ "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de." + "\n"
							+ "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
							+ "Ihr Akademisches Auslandsamt" + vpnDHBW;
					break;
				case "edit":
					// Text für Bewerbung zur Bearbeitung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n" + "\n"
							+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
							+ "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können. "
							+ "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de." + "\n"
							+ "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
							+ "Ihr Akademisches Auslandsamt" + vpnDHBW;
					break;
				default:
					return;
			}

		} else if (rolle == 4) {
			switch (validationResult) {
				case "true":
					// Text für erfolgreiche Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot für die Universitäten: " + "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n" +
							"wurde erfolgreich durch ihre/n Studiengangsleiter/in validiert." + "\n" + "\n"
							+ "-- Platzhalter für Anmerkungen des Studiengangsleiters --" + "\n" + "\n" + "\n"
							+ "Im nächsten Schritt wird ihre Bewerbung an ein/e Mitarbeiter/in des Akademischen Auslandsamtes für einen weiteren Validierungsprozess übergeben."
							+ "\n"
							+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren. "
							+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
							+ "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Studiengangsleiter/in" + vpnDHBW;
					break;
				case "false":
					// Text für abgelehnte Bewerbung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n" + "\n"
							+ "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
							+ "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
							+ "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
							+ "Ihr Studiengangsleiter/in" + vpnDHBW;
					break;
				case "edit":
					// Text für Bewerbung zur Bearbeitung
					output = "Sehr geehrte/r Herr/Frau " + studentName + (",") + "\n" + "\n"
							+ "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n" + "- " + uni1 + "\n" + "- " + uni2 + "\n" + "- " + uni3 + "\n" + "\n"
							+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
							+ "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
							+ " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
							+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können. "
							+ "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
							+ "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
							+ "Ihr Studiengangsleiter/in" + vpnDHBW;
					break;
				default:
					return;
			}
		}
		toClient.print(output);
	}
}

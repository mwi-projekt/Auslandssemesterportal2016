package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetEmailTextServlet", urlPatterns = { "/getMailText" })
public class GetEmailTextServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2) {
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

			JFrame frame = new JFrame();
			JEditorPane ausgabe = new JEditorPane();
			ausgabe.setContentType("text/html");

			if (validation_result.equals("true")) {
				// Text für erfolgreiche Bewerbung
				/*
				 * output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n" +
				 * "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "
				 * + uni + " wurde erfolgreich an das Akademisches Auslandsamt versendet." +
				 * "\n" + "<b> -- Platzhalter für Anmerkungen des Auslandsamts -- </b>" + "\n" +
				 * "\n" + "\n" +
				 * "Im nächsten Schritt wird sich ein Mitarbeiter zeitnah um die Bearbeitung Ihrer Bewerbung kümmern und entscheiden, ob Sie in die engere Auswahl potentieller Bewerber kommen."
				 * + "\n" +
				 * "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
				 * + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" +
				 * "Ihr Akademisches Auslandsamt";
				 */
				ausgabe.setText("<html>" + "Sehr geehrte/r Herr/Frau " + student_name + (",") + "<br>" + "<br>"
						+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "
						+ uni + " wurde erfolgreich an das Akademisches Auslandsamt versendet." + "<br>"
						+ "<b> -- Platzhalter für Anmerkungen des Auslandsamts -- </b>" + "<br>" + "<br" + "<br>"
						+ "Im nächsten Schritt wird sich ein Mitarbeiter zeitnah um die Bearbeitung Ihrer Bewerbung kümmern und entscheiden, ob Sie in die engere Auswahl potentieller Bewerber kommen."
						+ "<br>"
						+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
						+ "<br>" + "<br>" + "Mit freundlichen Grüßen," + "<br>" + "<br>"
						+ "Ihr Akademisches Auslandsamt" + "</html>.");
			} else {
				// Text für abgelehnte Bewerbung
				/*
				 * output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n" +
				 * "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni +
				 * "\n" +
				 * "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." +
				 * "\n" + "\n" + "Folgende Problem hat sich ergeben:" + "\n " +
				 * " <strong> -- Platzhalter für Erläuterung des Problems -- </strong> " + "\n"
				 * + "\n" +
				 * "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können."
				 * + "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" +
				 * "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Akademisches Auslandsamt";
				 */
				ausgabe.setText("<html>" + "Sehr geehrte/r Herr/Frau " + student_name + (",") + "\n" + "\n"
						+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: " + uni + "\n"
						+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
						+ "Folgende Problem hat sich ergeben:" + "\n "
						+ " <strong> -- Platzhalter für Erläuterung des Problems -- </strong> " + "\n" + "\n"
						+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können."
						+ "\n" + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n"
						+ "\n" + "Ihr Akademisches Auslandsamt" + "</html>.");
			}

			// toClient.print(output);
			toClient.print(ausgabe.getText());
			ausgabe.setEditable(false);
			frame.add(ausgabe);
			frame.setVisible(true);

		}
	}
};
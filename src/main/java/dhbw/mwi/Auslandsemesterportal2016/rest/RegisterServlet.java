package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.MessageEnum;

@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8664029054622031120L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);
		// NO AUTHENTIFICATION NEEDED
		PrintWriter out = response.getWriter();

		int rolle = 0;

		if (SQLQueries.isEmailUsed(request.getParameter("email"))) {
			out.print(ErrorEnum.MAILERROR);
			out.flush();
		} else if (SQLQueries.isMatnrUsed(Integer.parseInt(request.getParameter("matrikelnummer")))) {
			out.print("matnrError");
			out.flush();
		} else {

			if (request.getParameter("rolle").equals("Studierender")) {
				rolle = 3;
			} else if (request.getParameter("rolle").equals("Auslandsmitarbeiter")) {
				rolle = 2;
			}

			try {
				Message message = Util.getEmailMessage(request.getParameter("email"),
						MessageEnum.AAAREGISTR.toString());
				UUID id = UUID.randomUUID();
				System.out.println(id);

				// Zufälliges Salt generieren und Passwort hashen
				String salt = Util.generateSalt();
				String pw = Util.hashSha256(Util.hashSha256(request.getParameter("passwort")) + salt);

				String link = Config.MWI_URL + "/?confirm=" + id;

				message.setContent("<h2>Hallo " + request.getParameter("vorname")
						+ ",</h2> Du hast Dich auf der Seite des Auslandsportals registriert. "
						+ "Um Deine Registrierung abzuschlie&szlig;en, klicke bitte auf folgenden Link. <br><br> "
						+ "<a href=\"" + link + "\" target=\"new\">Anmeldung best&auml;tigen</a>", "text/html");

				// Send message
				Transport.send(message);

				// Verbindung zur DB um neuen Nutzer zu speichern
				// Statement statement = connection.createStatement();
				// int rsupd = statement.executeUpdate(sqlupd);
				int rsupd = SQLQueries.userRegister(request.getParameter("vorname"), request.getParameter("nachname"),
						pw, salt, rolle, request.getParameter("email"), request.getParameter("studiengang"),
						request.getParameter("kurs"), Integer.parseInt(request.getParameter("matrikelnummer")),
						request.getParameter("tel"), request.getParameter("mobil"), request.getParameter("standort"),
						"" + id);
				out.println(rsupd);
				// statement.close();

			} catch (MessagingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);

			}
		}

	}
}
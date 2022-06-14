package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.common.annotations.VisibleForTesting;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SendApplicationMailServlet", urlPatterns = { "/sendNewApplicationMail" })
public class SendApplicationMailServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		// Umgehungslösung: Keine Mails bei Testusern
		if (UserAuthentification.isTestUser(request.getCookies())) {
			PrintWriter writer = response.getWriter();
			writer.print("Keine E-Mail im Kontext von Tests versendet");
		} else {
			try {
				Transport.send(getMessage());
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	@VisibleForTesting
	public Message getMessage() throws MessagingException {
		Message message = Util.getEmailMessage("thomas.freytag@dhbw-karlsruhe.de",
				"Neue Bewerbung im Auslandssemesterportal");
		String link = "http://10.3.15.45/";
		message.setContent("<h2>Sehr geehrter Herr Freytag"
				+ ",</h2> es ist eine neue Bewerbung im Auslandssemesterportal."
				+ "<br><br> "
				+ "<a href= \"" + link + "\" target=\"new\">Auslandssemesterportal</a>", "text/html");
		return message;
	}

}
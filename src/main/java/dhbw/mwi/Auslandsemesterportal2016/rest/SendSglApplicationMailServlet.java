package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.common.annotations.VisibleForTesting;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SendSglApplicationMailServlet", urlPatterns = { "/sendSqlApplicationMail" })
public class SendSglApplicationMailServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		try {
			Transport.send(getMessage());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@VisibleForTesting
	public Message getMessage() throws MessagingException {
		Message message = Util.getEmailMessage("sarah.witte@dhbw-karlsruhe.de",
				"Neue Bewerbung im Auslandssemesterportal");
		String link = "http://10.3.15.45/";

		message.setContent("<h2>Sehr geehrte Frau Witte"
				+ ",</h2> es ist eine neue Bewerbung im Auslandssemesterportal."
				+ "<br><br> "
				+ "<a href= \"" + link + "\" target=\"new\">Auslandssemesterportal</a>", "text/html");
		return message;
	}
}
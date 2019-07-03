package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(name = "SendApplicationMailServlet", urlPatterns = { "/sendNewApplicationMail" })
public class SendApplicationMailServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String instanceId = request.getParameter("instance_id");

		try {
			Message message = Util.getEmailMessage("hen.sey@t-online.de",
					"Neue Bewerbung im Auslandssemesterportal");
			String link = "http://10.3.15.45:8080/Auslandssemesterportal/WebContent/";
				
			message.setContent("<h2>Sehr geehrter Herr Freytag"
					+ ",</h2> es ist eine neue Bewerbung im Auslandssemesterportal."
					+ "<br><br> "
					+ "<a href= \"" + link + "\" target=\"new\">Auslandssemesterportal</a>", "text/html");

			// Send message
			Transport.send(message);


		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
}
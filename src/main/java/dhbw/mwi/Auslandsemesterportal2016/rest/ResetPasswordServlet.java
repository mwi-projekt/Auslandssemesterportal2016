package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = { "/resetPassword" })
public class ResetPasswordServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.setResponseHeaders(request,response);
		// NO AUTHENTIFICATION NEEDED
		PrintWriter out = response.getWriter();
		String to = request.getParameter("email");
		if (!(SQL_queries.isEmailUsed(to))) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.write("No account registered for this email adress");
			throw new RuntimeException();
		}

		String uuid = SQL_queries.forgetPassword(to);

		String link = Config.MWI_URL + "/changePw.html?uuid=" + uuid;

		try {
			Message message = Util.getEmailMessage(to, "Passwortruecksetzung Auslandssemesterportal");
			message.setContent("<h2>Hallo"
					+ ",</h2> Du hast eine Kennwortrücksetzung für dein Nutzerkonto im Auslandssemesterportal angefordert. \n"
					+ "Um dein neues Kennwort zu setzen, klicke bitte auf folgenden Link. \n \n " + "<a href=\"" + link
					+ "\" target=\"new\">Passwort zurücksetzen</a>", "text/html; charset=UTF-8");

			// Send message
			Transport.send(message);

			// Verbindung zur DB um neuen Nutzer zu speichern
			// Statement statement = connection.createStatement();
			// int rsupd = statement.executeUpdate(sqlupd);
			// statement.close();
			out.print("Done resetting account " + to);

		} catch (MessagingException e) {
			e.printStackTrace();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

		}
	}

}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "UserUpdateServlet", urlPatterns = { "/user/update" })
public class UserUpdateServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int authenticated = userAuthentification.isUserAuthentifiedByCookie(request);

		if (authenticated != 1 && authenticated != 2) {
			response.sendError(401, "Rolle: " + authenticated);
		} else {
			PrintWriter toClient = response.getWriter();
			try {
				String mail = request.getParameter("email");
				String oldmail = request.getParameter("oldmail");
				String firstName = request.getParameter("firstName");
				String name = request.getParameter("name");
				String role = request.getParameter("role");

				if (oldmail.equals("0")) {
					int result;
					if (role.equals("2")) {
						String phone = request.getParameter("phone");
						String mobile = request.getParameter("mobile");
						result = SQL_queries.updateMA(firstName, name, mail, phone, mobile);
					} else {
						String studgang = request.getParameter("studgang");
						String kurs = request.getParameter("kurs");
						String matnr = request.getParameter("matnr");
						result = SQL_queries.updateUser(firstName, name, mail, studgang, kurs, matnr);
					}

					if (result == 1) {
						toClient.println("success");
					} else {
						response.sendError(500, "UpdateError");
					}
				} else {
					int result;
					if (role.equals("2")) {
						String phone = request.getParameter("phone");
						String mobile = request.getParameter("mobile");
						result = SQL_queries.updateMA(firstName, name, oldmail, phone, mobile, mail);
					} else {
						String studgang = request.getParameter("studgang");
						String kurs = request.getParameter("kurs");
						String matnr = request.getParameter("matnr");
						result = SQL_queries.updateUser(firstName, name, oldmail, studgang, kurs, matnr, mail);
					}

					if (result == 1) {
						String link = Config.MWI_URL + "/?confirm="
								+ SQL_queries.deactivateUser(mail);
						Message message = Util.getEmailMessage(mail,
								"Bestätigen: Geänderte E-Mail-Adresse Auslandssemesterportal");
						message.setContent("<h2>Hallo"
								+ ",</h2> Die E-Mail-Adresse deines Accounts für das Auslandssemesterportal wurde erfolgreich ge&auml;ndert.<br>"
								+ "Um Deine neue Adresse zu best&auml;tigen, klicke bitte auf folgenden Link. Danach kannst du dich mit deiner neuen Adresse und deinem Passwort wie gewohn einloggen.<br><br> "
								+ "<a href=\"" + link + "\" target=\"new\">E-Mail-Adresse best&auml;tigen</a>",
								"text/html; charset=UTF-8");

						Transport.send(message);
						toClient.println("Success");
					} else {
						response.sendError(500, "UpdateError");
					}
				}

			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println(e.toString());
			}
		}
	}
}

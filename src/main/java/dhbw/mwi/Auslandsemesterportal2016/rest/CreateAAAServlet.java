package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.mail.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.MessageEnum;

@WebServlet(name = "createAAAServlet", urlPatterns = { "/createAAA" })
public class CreateAAAServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		PrintWriter out = response.getWriter();
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		// Rolle Admin = 1
		if (rolle != 1) {
			response.sendError(401, "Rolle: " + rolle);
		} else {
			int role = 2;

			if (SQLQueries.isEmailUsed(request.getParameter("email"))) {
				out.print(ErrorEnum.MAILERROR);
				out.flush();
				out.close();
			} else {
				try {
					Message message = Util.getEmailMessage(request.getParameter("email"),
							MessageEnum.AAAREGISTR.toString());
					// Random initial Password
					UUID id = UUID.randomUUID();

					// Zufälliges Salt generieren und Passwort hashen
					String salt = Util.generateSalt();
					String pw = Util.hashSha256(Util.hashSha256(id.toString()) + salt);
					String aa = "--";
					// Verbindung zur DB um neuen Nutzer zu speichern
					int rsupd = SQLQueries.userRegister(request.getParameter("vorname"),
							request.getParameter("nachname"), pw, salt, role, request.getParameter("email"), aa, aa, -1,
							request.getParameter("tel"), request.getParameter("mobil"), aa, "1");

					if (rsupd == 0) {
						out.print(ErrorEnum.USERREGISTER);
						out.flush();
						out.close();
					} else {
						RequestDispatcher rd = request.getRequestDispatcher("resetPassword");
						rd.forward(request, response);
						out.close();
					}
				} catch (Exception e) {
					response.sendError(500, ErrorEnum.USERCREATE + e.getMessage());
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
}

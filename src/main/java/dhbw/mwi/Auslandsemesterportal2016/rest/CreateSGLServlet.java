package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.enums.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.mail.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "createSGLServlet", urlPatterns = { "/createSGL" })
public class CreateSGLServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		PrintWriter out = response.getWriter();
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(401, "Rolle: " + rolle);
		} else {

			// Rolle SGL Eintragen
			int role = 4;

			if (SQL_queries.isEmailUsed(request.getParameter("email"))) {
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
					String help = "--";
					// Verbindung zur DB um neuen Nutzer zu speichern
					// Hier fehlt noch die Übergabe des Studiengangs
					int rsupd = SQL_queries.userRegister(request.getParameter("vorname"),
							request.getParameter("nachname"), pw, salt, role, request.getParameter("email"),
							request.getParameter("studgang"), request.getParameter("kurs"), -1, help, help,
							request.getParameter("standort"), "1");

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

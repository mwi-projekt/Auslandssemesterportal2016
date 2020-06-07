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

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "createSGLServlet", urlPatterns = { "/createSGL" })
public class createSGLServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		int role = userAuthentification.isUserAuthentifiedByCookie(request);
		
		if (role != 1 && role != 2) {
			response.sendError(401, "Rolle: " + role);
		} else {
		
			//Rolle SGL Eintragen
			int role = 4;

			if (SQL_queries.isEmailUsed(request.getParameter("email"))) {
				out.print("mailError");
				out.flush();
			} else {
				try {
					Message message = Util.getEmailMessage(request.getParameter("email"),
							"Akademisches Auslandsamt Registrierung");
					// Random initial Password
					UUID id = UUID.randomUUID();

					// Zufälliges Salt generieren und Passwort hashen
					String salt = Util.generateSalt();
					String pw = Util.HashSha256(Util.HashSha256(id.toString()) + salt);
					String aa = "--";
					// Verbindung zur DB um neuen Nutzer zu speichern
					//Hier fehlt noch die Übergabe des Studiengangs
					int rsupd = SQL_queries.userRegister(request.getParameter("firstName"),
							request.getParameter("name"), pw, salt, role, request.getParameter("email"), request.getParameter("studiengang"), aa, -1,
							request.getParameter("phone"), request.getParameter("mobile"), aa, "1");

					if (rsupd == 0) {
						out.print("registerError");
						out.flush();
					} else {
						RequestDispatcher rd = request.getRequestDispatcher("resetPassword");
						rd.forward(request, response);
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);

				}

			}

		}

	}
}

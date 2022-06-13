package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		// ONLY VALID SESSIONS CAN LOGOUT
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle > 1) {
			Cookie[] cookies = request.getCookies();

			for (Cookie c : cookies) {
				if (c.getName().equals("email")) {
					c.setValue("");
					c.setMaxAge(0);
					response.addCookie(c);
				} else if (c.getName().equals("sessionID")) {
					SQLQueries.userLogout(c.getValue());
					c.setValue("");
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
	}
}

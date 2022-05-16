package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(urlPatterns = { "/logout" })
public class Logout extends HttpServlet {

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
					SQL_queries.userLogout(c.getValue());
					c.setValue("");
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
	}
}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetUserServlet", urlPatterns = { "/getUser" })
public class GetUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2) {
			response.sendError(401);
		} else {
			String sql = "SELECT nachname, vorname, email, tel, mobil, studiengang, kurs, matrikelnummer, standort FROM user WHERE rolle ='"
					+ request.getParameter("rolle") + "'";
			ResultSet rs = SQL_queries.executeStatement(sql);
			Util.writeJson(response, rs);
		}
	}
}

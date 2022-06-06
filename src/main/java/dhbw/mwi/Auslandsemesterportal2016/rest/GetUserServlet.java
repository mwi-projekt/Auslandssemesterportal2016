package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "GetUserServlet", urlPatterns = { "/getUser" })
public class GetUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2) {
			response.sendError(SC_UNAUTHORIZED);
		} else {
			String roleOfRequestedUsers = request.getParameter("rolle");
			if (roleOfRequestedUsers == null || "".equals(roleOfRequestedUsers)) {
				response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			}
			String sql = "SELECT nachname, vorname, email, tel, mobil, studiengang, kurs, matrikelnummer, standort FROM user WHERE rolle ='"
					+ roleOfRequestedUsers + "'";
			ResultSet rs = SQLQueries.executeStatement(sql);
			Util.writeJson(response, rs);
		}
	}
}

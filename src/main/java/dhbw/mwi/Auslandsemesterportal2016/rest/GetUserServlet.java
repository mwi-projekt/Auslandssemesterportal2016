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
		int role = userAuthentification.isUserAuthentifiedByCookie(request);

		if (role != 1 && role != 2) {
			response.sendError(401);
		} else {
			String sql = "SELECT name, firstName, email, phone, mobile, courseID, kurs, matrNumber, location FROM User WHERE role ='"
					+ request.getParameter("role") + "'";
			ResultSet rs = SQL_queries.executeStatement(sql);
			Util.writeJson(response, rs);
		}
	}
}

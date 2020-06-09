package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "UserDeleteServlet", urlPatterns = { "/user/delete" })
public class UserDeleteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int role = userAuthentification.isUserAuthentifiedByCookie(request);

		if ((role != 1 && role != 2) && role != 3) {
			response.sendError(401, "Rolle: " + role);
		} else {
			String matrnumber = request.getParameter("matrnumber");
			PrintWriter toClient = response.getWriter();

			if (matrnumber != null) {
				String query = "DELETE FROM User WHERE matrNumber = ?";
				String[] args = new String[] { matrnumber };
				String[] types = new String[] { "int" };
				SQL_queries.executeUpdate(query, args, types);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: parameter are missing");
			}
		}
	}
}

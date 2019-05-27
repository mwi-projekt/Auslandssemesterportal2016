package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "SGLDeleteServlet", urlPatterns = { "/user/deleteSGL" })
public class SGLDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1 && rolle != 2) {
			response.sendError(401, "Rolle: " + rolle);
		} else {
			String mail = request.getParameter("mail");
			PrintWriter toClient = response.getWriter();

			if (mail != null) {
				String query = "DELETE FROM user WHERE email = ?";
				String[] args = new String[] { mail };
				String[] types = new String[] { "String" };
				int result = SQL_queries.executeUpdate(query, args, types);

				if (result == 1) {
					toClient.println("User Deleted");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					toClient.println("User not found or could not be deleted");
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: parameter are missing");
			}
		}
	}
}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.enums.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "SGLDeleteServlet", urlPatterns = { "/user/deleteSGL" })
public class SGLDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
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
					toClient.println(SuccessEnum.USERDELETE.toString());
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					toClient.println(ErrorEnum.USERNOTDELETED.toString());
				}
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println(ErrorEnum.PARAMMISSING.toString());
			}
		}
	}
}

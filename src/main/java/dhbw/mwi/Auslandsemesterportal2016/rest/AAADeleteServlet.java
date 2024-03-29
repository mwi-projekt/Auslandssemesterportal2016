package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.*;

@WebServlet(name = "AAADeleteServlet", urlPatterns = { "/user/deleteAAA" })
public class AAADeleteServlet extends HttpServlet {

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
				int result = SQLQueries.executeUpdate(query, args, types);

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

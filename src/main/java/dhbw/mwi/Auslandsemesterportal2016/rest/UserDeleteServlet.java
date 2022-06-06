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
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;

@WebServlet(name = "UserDeleteServlet", urlPatterns = { "/user/delete" })
public class UserDeleteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(401, "Rolle: " + rolle);
		} else {
			String matrikelnummer = request.getParameter("matrikelnummer");
			PrintWriter toClient = response.getWriter();

			if (matrikelnummer != null) {
				String query = "DELETE FROM user WHERE matrikelnummer = ?";
				String[] args = new String[] { matrikelnummer };
				String[] types = new String[] { "int" };
				int result = SQLQueries.executeUpdate(query, args, types);

				if (result == 1) {
					toClient.println(SuccessEnum.USERDELETE);
				}

			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println(ErrorEnum.PARAMMISSING);
			}
		}
	}
}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.DBERROR;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "ModelProcessListServlet", urlPatterns = { "/processmodel/list" })
public class ModelProcessListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(SC_UNAUTHORIZED);
		} else {
			PrintWriter toClient = response.getWriter();

			String model = request.getParameter("model");

			if (model != null) {

				String query = "SELECT step FROM processModel WHERE model = ?";
				String[] args = new String[] { model };
				String[] types = new String[] { "String" };
				ResultSet rs = SQLQueries.executeStatement(query, args, types);
				try {
					while (rs.next()) {
						toClient.print(rs.getString("step") + ";");
					}

				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(SC_INTERNAL_SERVER_ERROR);
					toClient.println(DBERROR);
					toClient.println(e.getMessage());
				}

			} else {
				response.setStatus(SC_INTERNAL_SERVER_ERROR);
				toClient.println(PARAMMISSING);
			}
		}
	}
}

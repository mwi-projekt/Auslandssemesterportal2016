package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "ModelProcessListServlet", urlPatterns = { "/processmodel/list" })
public class ModelProcessListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int role = userAuthentification.isUserAuthentifiedByCookie(request);

		if (role != 1) {
			response.sendError(401);
		} else {
			PrintWriter toClient = response.getWriter();

			String model = request.getParameter("model");

			if (model != null) {

				String query = "SELECT step FROM processModel WHERE model = ?";
				String[] args = new String[] { model };
				String[] types = new String[] { "String" };
				ResultSet rs = SQL_queries.executeStatement(query, args, types);
				try {
					while (rs.next()) {
						toClient.print(rs.getString("step") + ";");
					}

				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					toClient.println("Error: db error");
					toClient.println(e.getMessage());
				}

			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: missing parameters");
			}
		}
	}
}

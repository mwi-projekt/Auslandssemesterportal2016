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

@WebServlet(name = "ModelProcessSaveServlet", urlPatterns = { "/processmodel/save" })
public class ModelProcessSaveServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int role = userAuthentification.isUserAuthentifiedByCookie(request);

		if (role != 1) {
			response.sendError(401);
		} else {
			PrintWriter toClient = response.getWriter();

			String model = request.getParameter("model");
			String step = request.getParameter("step");
			String json = request.getParameter("json");
			String stepnumber = request.getParameter("stepnumber");

			if (model != null && step != null && json != null && stepnumber != null) {

				String query = "SELECT * FROM processModel WHERE model = ? AND step = ?";
				String[] args = new String[] { model, step };
				String[] types = new String[] { "String", "String" };
				ResultSet rs = SQL_queries.executeStatement(query, args, types);
				try {
					if (rs.next()) {
						String id = rs.getString("id");
						query = "UPDATE processModel SET model = ?, step = ?, json = ?, stepNumber = ? WHERE id = ?";
						args = new String[] { model, step, json, stepnumber, id };
						types = new String[] { "String", "String", "String", "int", "int" };
						SQL_queries.executeUpdate(query, args, types);
					} else {
						query = "INSERT INTO processModel (model, step, json, stepNumber) VALUES " + "(?,?,?,?)";
						args = new String[] { model, step, json, stepnumber };
						types = new String[] { "String", "String", "String", "int" };
						SQL_queries.executeUpdate(query, args, types);
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

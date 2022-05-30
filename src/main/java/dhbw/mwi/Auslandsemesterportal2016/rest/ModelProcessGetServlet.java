package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;

@WebServlet(name = "ModelProcessGetServlet", urlPatterns = { "/processmodel/get" })
public class ModelProcessGetServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);
		// NO AUTHENTIFICATION NEEDED
		PrintWriter toClient = response.getWriter();

		String model = request.getParameter("model");
		String step = request.getParameter("step");

		if (model != null && step != null) {
			ResultSet rs = SQLQueries.getProcessModelJson(step, model);

			try {
				if (rs.next()) {
					toClient.print(rs.getString("json"));
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					toClient.println("Error: can not find entry");
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println(ErrorEnum.DBERROR.toString());
				toClient.println(e.getMessage());
			}

		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			toClient.println(ErrorEnum.PARAMMISSING.toString());
		}
	}
}

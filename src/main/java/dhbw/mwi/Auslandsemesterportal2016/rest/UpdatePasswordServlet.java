package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = { "/updatePassword" })
public class UpdatePasswordServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		// NO AUTHENTICATION NEEDED
		String uuid = request.getParameter("uuid");
		String pw = request.getParameter("password");
		if (uuid == null || pw == null) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		PrintWriter out = response.getWriter();
		out.print(SQLQueries.setPassword(uuid, pw));
		out.close();
	}

}

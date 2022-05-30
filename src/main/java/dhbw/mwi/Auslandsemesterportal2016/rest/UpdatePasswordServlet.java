package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = { "/updatePassword" })
public class UpdatePasswordServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		// NO AUTHENTIFICATION NEEDED
		PrintWriter out = response.getWriter();
		String uuid = request.getParameter("uuid");
		String pw = request.getParameter("password");
		int rowCount = SQLQueries.setPassword(uuid, pw);
		out.print(rowCount);
		out.close();
	}

}

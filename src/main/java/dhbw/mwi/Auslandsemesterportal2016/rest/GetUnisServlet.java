package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(urlPatterns = { "/unis" })
public class GetUnisServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		String studiengang = request.getParameter("studiengang");
		if (studiengang == null || "".equals(studiengang)) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		String sql = "SELECT uniTitel FROM cms_auslandsAngeboteInhalt WHERE studiengang ='"
				+ studiengang + "' ";
		ResultSet rs = SQLQueries.executeStatement(sql);
		Util.writeJson(response, rs);
	}

}

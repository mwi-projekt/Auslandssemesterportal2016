package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/auslandsAngebote" })
public class AuslandsAngebote extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		String sql = "SELECT studiengang FROM cms_auslandsAngebote WHERE ID > 0";
		ResultSet rs = SQLQueries.executeStatement(sql);
		Util.writeJson(response, rs);
	}

}

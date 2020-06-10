package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/portalInfo" })
public class PortalInfo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		String sql = "SELECT titel, listelement1, link1, listelement2, link2, listelement3, link3, listelement4, link4, listelement5, link5, listelement6, link6, listelement7, link7 FROM cms_infoMaterial";
		ResultSet rs = SQL_queries.executeStatement(sql);
		Util.writeJson(response, rs);
	}

}

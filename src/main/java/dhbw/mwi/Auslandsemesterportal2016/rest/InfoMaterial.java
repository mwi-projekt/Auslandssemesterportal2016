package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/infoMaterial" })
public class InfoMaterial extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);
		String sql = "SELECT titel, listelement1, link1, listelement2, link2, listelement3, link3, listelement4, link4, listelement5, link5, listelement6, link6, listelement7, link7 FROM cms_infoMaterial";
		ResultSet rs = SQLQueries.executeStatement(sql);
		Util.writeJson(response, rs);
	}

}

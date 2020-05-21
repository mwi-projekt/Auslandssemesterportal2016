package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(name = "ActivateUserServlet", urlPatterns = { "/confirm" })
public class ActivateUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.setResponseHeaders(request,response);
		
		String code = request.getParameter("code");
		
		String userId = "_";
		if (code != null) {

			try {
				String sql = "SELECT userID FROM user WHERE verifiziert='" + code + "'";
				System.out.println(sql);
				// Open a connection
				Connection conn = DB.getInstance();
				// Execute SQL query
				java.sql.Statement stmt = conn.createStatement();
				System.out.println("Connect");
				if (sql != "leer") {
					ResultSet rs = stmt.executeQuery(sql);
					int spalten = rs.getMetaData().getColumnCount();
					while (rs.next()) {
						for (int k = 1; k <= spalten; k++) {

							System.out.println(rs.getString(k) + ";");
							userId = rs.getString(k);
						}
					}
					sql = "leer";

				}
				if (userId != "_") {
					String query = "UPDATE user SET verifiziert = 1 WHERE userID=" + userId;
					int rsupd = stmt.executeUpdate(query);

				}
			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
				System.out.println("Fehler se");
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
				System.out.println("Fehler e");
			}

		}

	}
}
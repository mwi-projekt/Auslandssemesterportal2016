package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/WebContent/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      // NO AUTHENTIFICATION NEEDED
      
    	//Connection connection = DB.getInstance();
        String salt = "";
        String mail = "";

/*
        // SQL-Statement für Salt vorbereiten
        String sqlsalt = "SELECT salt FROM user WHERE '" + request.getParameter("email") + "'= email";
        String salt = "";

        // Verbindung zur DB um Salt abzurufen
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlsalt);
            int spalten = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int k = 1; k <= spalten; k++) {
                    // Salt abrufen
                    salt = rs.getString(k);
                }
            }

            // Berechnung des Passworthashes aus gehashtem Eingabewert und Salt
            String pw = Util.HashSha256(Util.HashSha256(request.getParameter("pw")) + salt);

            // SQL-Statement für die Anmeldung
            String sql = "SELECT rolle, matrikelnummer, studiengang FROM user WHERE '" + request.getParameter("email")
                    + "'= email AND '" + pw + "' = passwort"; */
        	mail = request.getParameter("email");
        	salt = SQL_queries.getSalt(mail);
        	String[] result = SQL_queries.userLogin(mail, salt, request.getParameter("pw"));
            //rs = statement.executeQuery(sql);
          Cookie cookie = new Cookie( "sessionID", result[4] );
          Cookie mailcookie = new Cookie( "email", mail );
          response.addCookie( cookie );
          response.addCookie( mailcookie );
          PrintWriter out = response.getWriter();
          out.append("" + result[0] + ";" + result[1] + ";" + result[2]+ ";" + result[3]);
          out.close();

        /*} catch (SQLException e)
            e.printStackTrace();
        }

    }*/

}
}

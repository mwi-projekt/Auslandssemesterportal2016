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

@WebServlet(name = "LoginServlet", urlPatterns = {"/WebContent/login"})
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      // NO AUTHENTIFICATION NEEDED
      
    	//Connection connection = DB.getInstance();
        String salt = "";
        String mail = "";

        mail = request.getParameter("email");
        salt = SQL_queries.getSalt(mail);
        String[] result = SQL_queries.userLogin(mail, salt, request.getParameter("pw"));
        Cookie cookie = new Cookie( "sessionID", result[4] );
        Cookie mailcookie = new Cookie( "email", mail );
        response.addCookie( cookie );
        response.addCookie( mailcookie );
        PrintWriter out = response.getWriter();
        out.append("" + result[0] + ";" + result[1] + ";" + result[2]+ ";" + result[3]);
        out.close();
}
}

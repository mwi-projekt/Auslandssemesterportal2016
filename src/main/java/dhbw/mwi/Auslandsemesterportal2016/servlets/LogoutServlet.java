package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/WebContent/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //ONLY VALID SESSIONS CAN LOGOUT
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle<1){
        //JUST REDIRECT TO HOME IF NO ACTIVE SESSION
        String baseLocation = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/WebContent/";
        response.sendRedirect(baseLocation);
      }
      else{
        Cookie[] cookies = request.getCookies();
        String sessionId = null, mail = null;

        for(Cookie c : cookies){
          if(c.getName().equals("email")){
            c.setValue("");
            c.setMaxAge(0);
            response.addCookie(c);
          }
          else if(c.getName().equals("sessionID")){
            SQL_queries.userLogout(c.getValue());
            c.setValue("");
            c.setMaxAge(0);
            response.addCookie(c);
          }
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        String baseLocation = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/WebContent/";
        response.sendRedirect(baseLocation);
      }
    }
}

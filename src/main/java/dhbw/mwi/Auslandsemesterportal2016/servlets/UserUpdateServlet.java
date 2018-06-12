package dhbw.mwi.Auslandsemesterportal2016.servlets;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;
import javax.servlet.RequestDispatcher;

@WebServlet(name = "UserUpdateServlet", urlPatterns = {"/WebContent/user/update"})
public class UserUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle!=1 && rolle!=2){
        response.sendError(401,"Rolle: "+rolle);
      }
      else{
          try{
          String mail = request.getParameter("email");
          String newmail = request.getParameter("newemail");
          String vorname = request.getParameter("vorname");
          String nachname = request.getParameter("nachname");
          String tel = request.getParameter("tel");
          String mobil = request.getParameter("mobil");
          String studgang = request.getParameter("studgang");
          String kurs = request.getParameter("kurs");
          String matnr = request.getParameter("matnr");
          
         
          PrintWriter toClient = response.getWriter();
          if(newmail.equals("0")){
              toClient.println(SQL_queries.updateUser(vorname, nachname, mail, studgang, kurs, matnr, tel, mobil, mail));
          }
          else{
              SQL_queries.updateUser(vorname, nachname, mail, studgang, kurs, matnr, tel, mobil, newmail);
              RequestDispatcher rd = request.getRequestDispatcher("resetPassword");
              rd.forward(request,response);
          }
                            
          } catch(Exception e) {
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              
          }
      }
    }
}

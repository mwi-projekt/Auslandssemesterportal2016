package dhbw.mwi.Auslandsemesterportal2016.servlets;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "AAADeleteServlet", urlPatterns = {"/WebContent/user/deleteAAA"})
public class AAADeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle!=1 && rolle!=2){
        response.sendError(401,"Rolle: "+rolle);
      }
      else{
          String mail = request.getParameter("mail");
          PrintWriter toClient = response.getWriter();

          if (mail != null) {
                  String query = "DELETE FROM user WHERE mail = ?";
                  String[] args = new String[]{mail};
                  String[] types = new String[]{"String"};
                  toClient.println(SQL_queries.executeUpdate(query,args,types));
          } else {
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              toClient.println("Error: parameter are missing");
          }
      }
    }
}

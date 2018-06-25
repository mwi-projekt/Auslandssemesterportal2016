package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;
import javax.servlet.RequestDispatcher;

@WebServlet(name = "createAAAServlet", urlPatterns = {"/WebContent/createAAA"})
public class createAAAServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

        if(rolle!=1 && rolle!=2){
          response.sendError(401,"Rolle: "+rolle);
        }
        else{
            int role = 2;

            if (SQL_queries.isEmailUsed(request.getParameter("email"))){
                    out.print("mailError");
                    out.flush();
            }
            else{
                try {
                    Message message = Util.getEmailMessage(request.getParameter("email")
                            , "Akademisches Auslandsamt Registrierung");
                //Random initial Password
                UUID id = UUID.randomUUID();

                // Zufälliges Salt generieren und Passwort hashen
                String salt = Util.generateSalt();
                String pw = Util.HashSha256(Util.HashSha256(id.toString()) + salt);
                String aa = "--";
                // Verbindung zur DB um neuen Nutzer zu speichern
                int rsupd = SQL_queries.userRegister(request.getParameter("vorname"),request.getParameter("nachname"),pw,
                        salt,role,request.getParameter("email"),aa,aa,
                        -1,request.getParameter("phone"),request.getParameter("mobil"),aa,"1");
                
                if(rsupd == 0){
                    out.print("registerError");
                    out.flush();
                }
                else{
                    RequestDispatcher rd = request.getRequestDispatcher("resetPassword");
                    rd.forward(request,response);
                }      

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);

                }
                
            }
            
        }

    }
}

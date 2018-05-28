package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

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

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/WebContent/resetPassword"})
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //NO AUTHENTIFICATION NEEDED
        PrintWriter out = response.getWriter();
        String to = request.getParameter("email");
        if (!(SQL_queries.isEmailUsed(to))){
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	out.write("No account registered for this email adress");
        	throw new RuntimeException();
        }

        String uuid = SQL_queries.forgetPassword(to);

        // registrierungsemail senden
        // Recipient's email ID needs to be mentioned.

        // Sender's email ID needs to be mentioned
        /*String from = "noreply@dhbw-karlsruhe.de";// change accordingly

        // Get the Session object.
        Session session = Util.getEmailSession();

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
<<<<<<< HEAD
            message.setSubject("Passwortrücksetzung Auslandssemesterportal");

=======
            message.setSubject("Passwortruecksetzung Auslandssemesterportal");
*/
>>>>>>> 178fa330c41d4d9ee34e13b9aa234808cea8a4d0
			String link = "http://193.196.7.215:8080/Auslandssemesterportal/WebContent/changePw.html?uuid=" + uuid;

        	try{
        		Message message = Util.getEmailMessage(to
        				, "Passwortruecksetzung Auslandssemesterportal");
            message.setContent("<h2>Hallo"
                    + ",</h2> Du hast eine Kennwortrücksetzung für dein Nutzerkonto im Auslandssemesterportal angefordert. \n"
                    + "Um dein neues Kennwort zu setzen, klicke bitte auf folgenden Link. \n \n "
                    + "<a href=\"" + link + "\" target=\"new\">Passwort zurücksetzen</a>", "text/html; charset=UTF-8");

            // Send message
            Transport.send(message);


            // Verbindung zur DB um neuen Nutzer zu speichern
            //Statement statement = connection.createStatement();
            //int rsupd = statement.executeUpdate(sqlupd);
            //statement.close();
            out.print("Done resetting account " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
        //} catch (SQLException e) {
        //    e.printStackTrace();
        //}

    }
    }

}

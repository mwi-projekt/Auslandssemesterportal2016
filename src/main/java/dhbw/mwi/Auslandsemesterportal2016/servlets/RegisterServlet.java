package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

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

@WebServlet(name = "RegisterServlet", urlPatterns = {"/WebContent/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = DB.getInstance();
        PrintWriter out = response.getWriter();

        int rolle = 0;

        if (request.getParameter("rolle").equals("Studierender")) {
            rolle = 3;
        } else if (request.getParameter("rolle").equals("Auslandsmitarbeiter")) {
            rolle = 2;
        }

        // registrierungsemail senden
        // Recipient's email ID needs to be mentioned.
        String to = request.getParameter("email");// change accordingly

        // Sender's email ID needs to be mentioned
        String from = "mwiausland@gmail.com";// change accordingly
        final String username = "mwiausland@gmail.com";// change
        // accordingly
        final String password = "MWIAusland1";// change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Akademisches Auslandsamt Registrierung");

            // Now set the actual message
					/*
					 * message.setText(
					 * "Hello, this is sample for to check send " +
					 * "email using JavaMailAPI ");
					 */
            UUID id = UUID.randomUUID();
            System.out.println(id);

            // Zufälliges Salt generieren und Passwort hashen
            String salt = Util.generateSalt();
            String pw = Util.HashSha256(Util.HashSha256(request.getParameter("passwort")) + salt);

            String sqlupd = "INSERT INTO user (vorname, nachname, passwort, salt, rolle, email, studiengang, kurs, matrikelnummer, tel, mobil, standort, verifiziert) VALUES ('"
                    + request.getParameter("vorname") + "', '" + request.getParameter("nachname") + "', '" + pw
                    + "', '" + salt + "', '" + rolle + "', '" + request.getParameter("email") + "', '"
                    + request.getParameter("studiengang") + "', '" + request.getParameter("kurs") + "', '"
                    + request.getParameter("matrikelnummer") + "', '" + request.getParameter("tel") + "', '"
                    + request.getParameter("mobil") + "', '" + request.getParameter("standort") + "', '" + id
                    + "')";

            //String link = "193.196.7.215:8080/Auslandssemesterportal/WebContent/index.html?confirm=" + id;
            String link = "http://193.196.7.215:8080/Auslandssemesterportal/WebContent/index.html?confirm=" + id;

            message.setContent("<h2>Hallo " + request.getParameter("vorname")
                    + ",</h2> du hast dich auf der Seite des Auslandsportals registriert. "
                    + "Um deine Registrierung abzuschlie&szlig;en klicke bitte auf folgenden Link. <br><br> "
                    + "<a href=\"" + link + "\" target=\"new\">Anmeldung best&auml;tigen</a>", "text/html");

            // Send message
            Transport.send(message);


            // Verbindung zur DB um Salt abzurufen
            Statement statement = connection.createStatement();
            int rsupd = statement.executeUpdate(sqlupd);
            out.println(rsupd);
            statement.close();

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
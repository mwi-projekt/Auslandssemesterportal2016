package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.mail.Message;
import javax.mail.Transport;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;

@WebServlet(name = "UserUpdateServlet", urlPatterns = { "/user/update" })
public class UserUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Util.addResponseHeaders(request, response);
        int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
        if (rolle != 1) {
            response.sendError(401, "Rolle: " + rolle);
        } else {
            PrintWriter toClient = response.getWriter();
            try {
                String mail = request.getParameter("email");
                String oldmail = request.getParameter("oldmail");
                String vorname = request.getParameter("vorname");
                String nachname = request.getParameter("nachname");
                String role = request.getParameter("role");
                if (oldmail.equals("0")) {
                    int result;
                    if (role.equals("2")) {
                        String tel = request.getParameter("tel");
                        String mobil = request.getParameter("mobil");
                        result = SQLQueries.updateMA(vorname, nachname, mail, tel, mobil);
                    } else if (role.equals("4")) {
                        String standort = request.getParameter("standort");
                        String studgang = request.getParameter("studgang");
                        String kurs = request.getParameter("kurs");
                        result = SQLQueries.updateStud(vorname, nachname, mail, standort, studgang, kurs);
                    } else {
                        String studgang = request.getParameter("studgang");
                        String kurs = request.getParameter("kurs");
                        String matnr = request.getParameter("matnr");
                        String standort = request.getParameter("standort");
                        result = SQLQueries.updateUser(vorname, nachname, mail, studgang, kurs, matnr, standort);
                    }
                    if (result == 1) {
                        toClient.println(SuccessEnum.UPDATEUSER);
                    } else {
                        response.sendError(500, ErrorEnum.USERUPDATE.toString());
                    }
                } else {
                    int result;
                    if (role.equals("2")) {
                        String tel = request.getParameter("tel");
                        String mobil = request.getParameter("mobil");
                        result = SQLQueries.updateMA(vorname, nachname, oldmail, tel, mobil, mail);
                    } else if (role.equals("4")) {
                        String standort = request.getParameter("standort");
                        String studgang = request.getParameter("studgang");
                        String kurs = request.getParameter("kurs");
                        result = SQLQueries.updateStud(vorname, nachname, mail, standort, studgang, kurs, oldmail);
                    } else {
                        String studgang = request.getParameter("studgang");
                        String kurs = request.getParameter("kurs");
                        String matnr = request.getParameter("matnr");
                        String standort = request.getParameter("standort");
                        result = SQLQueries.updateUser(vorname, nachname, oldmail, studgang, kurs, matnr, mail,
                                standort);
                    }
                    if (result == 1) {
                        String link = Config.MWI_URL + "/?confirm=" + SQLQueries.deactivateUser(mail);
                        Message message = Util.getEmailMessage(mail,
                                "Bestätigen: Geänderte E-Mail-Adresse Auslandssemesterportal");
                        message.setContent("<h2>Hallo"
                                + ",</h2> Die E-Mail-Adresse deines Accounts für das Auslandssemesterportal wurde erfolgreich ge&auml;ndert.<br>"
                                + "Um Deine neue Adresse zu best&auml;tigen, klicke bitte auf folgenden Link. Danach kannst du dich mit deiner neuen Adresse und deinem Passwort wie gewohn einloggen.<br><br> "
                                + "<a href=\"" + link + "\" target=\"new\">E-Mail-Adresse best&auml;tigen</a>",
                                "text/html; charset=UTF-8");

                        Transport.send(message);
                        toClient.println(SuccessEnum.UPDATEUSER);
                    } else {
                        response.sendError(500, ErrorEnum.USERUPDATE.toString());
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                toClient.println(e);
            }
        }
    }
}

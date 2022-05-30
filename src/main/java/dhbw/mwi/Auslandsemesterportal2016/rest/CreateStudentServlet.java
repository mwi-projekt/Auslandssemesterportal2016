package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.MessageEnum;

import javax.mail.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "CreateStudentServlet", urlPatterns = { "/createStudent" })
public class CreateStudentServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

        if (rolle != 1) {
            response.sendError(401, "Rolle: " + rolle);
        } else {

            int role = 3;

            if (SQLQueries.isEmailUsed(request.getParameter("email"))) {
                out.print(ErrorEnum.MAILERROR);
                out.flush();
            } else {
                try {
                    Message message = Util.getEmailMessage(request.getParameter("email"),
                            MessageEnum.AAAREGISTR.toString());
                    // Random initial Password
                    UUID id = UUID.randomUUID();

                    // Zufälliges Salt generieren und Passwort hashen
                    String salt = Util.generateSalt();
                    String pw = Util.hashSha256(Util.hashSha256(id.toString()) + salt);
                    String aa = "--";
                    // Verbindung zur DB um neuen Nutzer zu speichern
                    // Hier fehlt noch die Übergabe des Studiengangs
                    int rsupd = SQLQueries.userRegister(request.getParameter("vorname"),
                            request.getParameter("nachname"), pw, salt, role, request.getParameter("email"),
                            request.getParameter("studgang"), request.getParameter("kurs"),
                            Integer.parseInt(request.getParameter("matnr")), aa, aa, request.getParameter("standort"),
                            "1");

                    if (rsupd == 0) {
                        out.print(ErrorEnum.USERREGISTER);
                        out.flush();
                    } else {
                        RequestDispatcher rd = request.getRequestDispatcher("resetPassword");
                        rd.forward(request, response);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

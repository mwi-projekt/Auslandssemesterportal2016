package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

import javax.mail.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(name = "CreateStudentServlet", urlPatterns = {"/createStudent"})
public class CreateStudentServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

        if (rolle != 1 && rolle != 2) {
            response.sendError(401, "Rolle: " + rolle);
        } else {

            int role = 3;

            if (SQL_queries.isEmailUsed(request.getParameter("email"))) {
                out.print("mailError");
                out.flush();
            } else {
                try {
                    Message message = Util.getEmailMessage(request.getParameter("email"),
                            "Akademisches Auslandsamt Registrierung");
                    // Random initial Password
                    UUID id = UUID.randomUUID();

                    // Zufälliges Salt generieren und Passwort hashen
                    String salt = Util.generateSalt();
                    String pw = Util.HashSha256(Util.HashSha256(id.toString()) + salt);
                    String aa = "--";
                    // Verbindung zur DB um neuen Nutzer zu speichern
                    //Hier fehlt noch die Übergabe des Studiengangs
                    int rsupd = SQL_queries.userRegister(
                            request.getParameter("vorname"),
                            request.getParameter("nachname"),
                            pw,
                            salt,
                            role,
                            request.getParameter("email"),
                            request.getParameter("studgang"),
                            request.getParameter("kurs"),
                            Integer.parseInt(request.getParameter("matnr")),
                            aa,
                            aa,
                            request.getParameter("standort"),
                            "1");

                    if (rsupd == 0) {
                        out.print("registerError");
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
package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ProcessCompleteServlet", urlPatterns = {"/WebContent/process/complete"})
public class ProcessCompleteServlet extends HttpServlet {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String matrikelnummer = request.getParameter("matrikelnummer");
        String uni = request.getParameter("uni");
        PrintWriter toClient = response.getWriter();

        if (matrikelnummer != null && uni != null) {
            String id = ProcessService.getProcessId(matrikelnummer, uni);

            if (id != null && id != "leer") {

                processEngine.getTaskService().complete(
                        processEngine.getTaskService().createTaskQuery().processInstanceId(id).singleResult().getId());

                toClient.println(id);

            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                toClient.println("Error: can not find process");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            toClient.println("Error: parameter are missing");
        }
    }
}

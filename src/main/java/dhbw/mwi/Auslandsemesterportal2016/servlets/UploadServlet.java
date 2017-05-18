package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "UploadServlet", urlPatterns = {"/WebContent/upload"})
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class UploadServlet extends HttpServlet {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String id = ProcessService.getProcessId(request.getParameter("matrikelnummer"), request.getParameter("uni"));
        PrintWriter out = response.getWriter();
        Part filePart = null;
        String key;

        if (id.equals("leer")) {
            out.print("Error: can not find process id");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if (action.equals("nach_Abitur_Upload")) {
            key = "Abiturzeugnis";
        } else if (action.equals("nach_DAAD_Upload")) {
            key = "DAAD_Formular";
        } else if (action.equals("nach_Dualis_Upload")) {
            key = "Dualis_Dokumente";
        } else if (action.equals("nach_Motivation_Upload")) {
            key = "Motivationsschreiben";
        } else if (action.equals("nach_Zustimmung_Upload")) {
            key = "Zustimmungsfomular";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error: wrong action");
            return;
        }

        try {
            filePart = request.getPart("file");

            if (filePart != null){
                FileValue typedFileValue = Variables.fileValue(key + ".pdf").file(filePart.getInputStream()).create();
                processEngine.getRuntimeService().setVariable(id, key, typedFileValue);

                completeTask(id);
            }

        } catch (ServletException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error: wrong file");
        }

    }

    public void completeTask(String instanceId) {
        processEngine.getTaskService().complete(
                processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult().getId());
    }

}

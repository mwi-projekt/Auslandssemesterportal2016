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

@WebServlet(name = "UploadServlet", urlPatterns = {"/WebContent/upload_new"})
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

        System.out.println("File-Upload Servlet");
        if (id.equals("leer")) {
            out.print("Error: can not find process id");
            out.flush();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if (action == null || action.equals("")) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error: wrong action");
            out.flush();
            return;
        }

        try {
            filePart = request.getPart("file");

            if (filePart != null){
                FileValue typedFileValue = Variables.fileValue(action + ".pdf").file(filePart.getInputStream()).create();
                processEngine.getRuntimeService().setVariable(id, action, typedFileValue);
                out.print("jop");
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Error: wrong file");
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error: wrong file");
            out.flush();
        }

    }

}

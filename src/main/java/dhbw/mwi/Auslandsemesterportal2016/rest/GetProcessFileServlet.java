package dhbw.mwi.Auslandsemesterportal2016.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetProcessFileServlet", urlPatterns = {"/getProcessFile"})
public class GetProcessFileServlet extends HttpServlet {

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // checks if the file exists
        String instanceID = request.getParameter("instance_id");
        String key = request.getParameter("key");
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        FileValue typedFileValue = (FileValue) runtime.getVariableTyped(instanceID, key);
        if (typedFileValue != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      /*int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle<1){
        response.sendError(401);
      }
      else{*/
          ServletOutputStream toClient = response.getOutputStream();

          String instanceID = request.getParameter("instance_id");
          String key = request.getParameter("key");
          ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
          RuntimeService runtime = engine.getRuntimeService();
          FileValue typedFileValue = (FileValue) runtime.getVariableTyped(instanceID, key);
          InputStream is = typedFileValue.getValue();

          try {
              response.setContentType(typedFileValue.getMimeType());
              int nRead;
              byte[] buf = new byte[1024];

              for(int nChunk = is.read(buf); nChunk!=-1; nChunk = is.read(buf)) {
                  toClient.write(buf, 0, nChunk);
              }

              toClient.flush();

          } catch (Exception e) {

          }
       // }
    }
}

package dhbw.mwi.Auslandsemesterportal2016.servlets;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "UpdateInstanceServlet", urlPatterns = {"/WebContent/setVariable"})
public class UpdateInstanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String instanceID = request.getParameter("instance_id");
        String key = request.getParameter("key");
        String val = request.getParameter("value");
        String[] keys = key.split("\\|", -1);
        String[] vals = val.split("\\|", -1);
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        ProcessInstance instance = runtime.createProcessInstanceQuery().processInstanceId(instanceID).singleResult();
   
        if (key != null && val != null) {
        	for (int i = 0; i < keys.length; i++){
        		runtime.setVariable(instance.getId(), keys[i], vals[i]);
        		}
        	toClient.write("Success");
        }
    }
}

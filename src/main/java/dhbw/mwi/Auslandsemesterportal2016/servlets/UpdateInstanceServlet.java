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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdateInstanceServlet", urlPatterns = {"/WebContent/setVariable"})
public class UpdateInstanceServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String instanceID = request.getParameter("instance_id");
        String key = request.getParameter("key");
        String val = request.getParameter("value");
        String[] keys = key.split("\\|", -1);
        String[] vals = val.split("\\|", -1);
        Map<String,Object> vars = new HashMap<String,Object>();
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        ProcessInstance instance = runtime.createProcessInstanceQuery().processInstanceId(instanceID).singleResult();
   
        if (key != null && val != null) {
        	for (int i = 0; i < keys.length; i++){
        		//runtime.setVariable(instance.getId(), keys[i], vals[i]);
        		vars.put(keys[i], vals[i]);	
        	}
        	engine.getTaskService().complete(engine.getTaskService().createTaskQuery().processInstanceId(instanceID).singleResult().getId(), vars);	
    		toClient.write("Saved");
        } else {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	toClient.print("Variables not set");
        }
    }
}

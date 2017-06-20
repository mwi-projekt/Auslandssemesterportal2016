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

@WebServlet(name = "GetVariablesServlet", urlPatterns = {"/WebContent/getVariable"})
public class GetVariablesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String instanceID = request.getParameter("instance_id");
        String key = request.getParameter("key");
        String[] keys = key.split("\\|", -1);
        String output = "";
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
   
        for (int i = 0; i < keys.length; i++){
			Object obj = runtime.getVariable(instanceID, keys[i]);
			output = output + obj.toString() + "|";
		}		
		toClient.println(output);
    }
}

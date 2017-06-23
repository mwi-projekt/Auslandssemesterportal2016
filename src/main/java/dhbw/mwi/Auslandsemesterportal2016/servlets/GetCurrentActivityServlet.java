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
import java.util.List;

@WebServlet(name = "GetCurrentActivityServlet", urlPatterns = {"/WebContent/currentActivity"})
public class GetCurrentActivityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String instanceID = request.getParameter("instance_id");
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
   
        if (instanceID != null) {
        	List<String> activitiesList = runtime.getActiveActivityIds(instanceID);
        	String activeActivity = activitiesList.get(0);
        	toClient.write(activeActivity);
        }
    }
}

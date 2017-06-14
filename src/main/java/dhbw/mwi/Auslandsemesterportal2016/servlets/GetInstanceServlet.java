package dhbw.mwi.Auslandsemesterportal2016.servlets;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;

@WebServlet(name = "GetInstanceServlet", urlPatterns = {"/WebContent/getInstance"})
public class GetInstanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        
        String instance_id = SQL_queries.getInstanceId(Integer.parseInt(request.getParameter("matnr")), request.getParameter("uni"));
        if (instance_id == ""){
        	ProcessInstance instance = runtime.startProcessInstanceById("studentBewerben");
        	instance_id = instance.getId();
        }
        toClient.print(instance_id);
    }
}

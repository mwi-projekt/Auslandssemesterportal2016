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
        
        int matnr = Integer.parseInt(request.getParameter("matnr"));
        String uni = request.getParameter("uni");

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        //Holt instanceId aus DB
        String instance_id = SQL_queries.getInstanceId(matnr, uni);
        if (instance_id == ""){
        	//Lege neue Instanz an
        	ProcessInstance instance = runtime.startProcessInstanceByKey("studentBewerben");
        	instance_id = instance.getId();
        	long stepcount = engine.getTaskService().createTaskQuery().caseInstanceId(instance_id).count();
        	SQL_queries.createInstance(instance_id, uni, matnr, (int) stepcount);
        }
        toClient.print(instance_id);
    }
}

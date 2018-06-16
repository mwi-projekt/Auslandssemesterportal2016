package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GetAdminTasksServlet", urlPatterns = {"/WebContent/getTasks"})
public class GetAdminTasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle!=1 && rolle!=2){
        response.sendError(401,"Rolle: "+rolle);
      }
      else{
        response.setCharacterEncoding("UTF-8");
          PrintWriter toClient = response.getWriter();

          ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
          RuntimeService runtime = engine.getRuntimeService();
          String output = "";
          List<ProcessInstance> results = runtime.createProcessInstanceQuery().list();
          for (int i = 0; i < results.size(); i++){
          	String instanceId = results.get(i).getId();
          	List<String> activities = runtime.getActiveActivityIds(instanceId);
          	if (activities.get(0).equals("abgeschlossen")){
          		String name = (String) runtime.getVariable(instanceId, "bewNachname");
          		String vname = (String) runtime.getVariable(instanceId, "bewVorname");
          		String uni = (String) runtime.getVariable(instanceId, "aktuelleUni");
          		String kurs = (String) runtime.getVariable(instanceId, "bewKurs");
          		String partnerUni = (String) runtime.getVariable(instanceId, "uni");
          		
          		output = output + instanceId + "|" + name + "|" + vname + "|"  + uni + "|" + kurs + "|" + partnerUni + "|complete;";
          	} else if (activities.get(0).equals("datenValidieren")){
          		String name = (String) runtime.getVariable(instanceId, "bewNachname");
          		String vname = (String) runtime.getVariable(instanceId, "bewVorname");
          		String uni = (String) runtime.getVariable(instanceId, "aktuelleUni");
          		String kurs = (String) runtime.getVariable(instanceId, "bewKurs");
          		String partnerUni = (String) runtime.getVariable(instanceId, "uni");
          		output = output + instanceId + "|" + name + "|" + vname + "|"  + uni + "|" + kurs + "|" + partnerUni + "|validate;";
          	}
          }
         toClient.print(output);
      }
    }
}

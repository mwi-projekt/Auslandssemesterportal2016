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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserGetInstanceServlet", urlPatterns = {"/WebContent/getUserInstances"})
public class UserGetInstanceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setCharacterEncoding("UTF-8");
        PrintWriter toClient = response.getWriter();
        int matnr = Integer.parseInt(request.getParameter("matnr"));

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        String reply = "";
        //Holt instanceId aus DB
        ArrayList<String[]> instances = SQL_queries.getUserInstances(matnr);
        
        for (int i = 0; i < instances.size(); i++){
        	String[] listEntry = instances.get(i);
        	String uni = listEntry[0];
        	String instanceID = listEntry[1];
        	String stepCounter = "";
        	List<String> activityList = runtime.getActiveActivityIds(instanceID);
        	if (activityList.isEmpty()){
        		//Prozess abgeschlossen
        		stepCounter = "Abgeschlossen";
        	} else {
        		String currentActivity = activityList.get(0);
        		if (currentActivity.equals("datenPruefen")){
        			stepCounter = "Daten prüfen";
        		} else if (currentActivity.equals("datenValidieren")){
        			stepCounter = "Auf Rückmeldung warten";
        		} else {
        			//Rufe Schrittzahl aus Tabelle ab
        			stepCounter = SQL_queries.getStepCounter(currentActivity, "studentBewerben");
        		}
        	}
        	toClient.println(instanceID + "|" + uni + "|" + stepCounter);
        }
    }
}

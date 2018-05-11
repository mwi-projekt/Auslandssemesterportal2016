package dhbw.mwi.Auslandsemesterportal2016.servlets;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetOverviewServlet", urlPatterns = {"/WebContent/getOverview"})
public class GetOverviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle<1){
        response.sendError(401);
      }
      else{
      	response.setCharacterEncoding("UTF-8");
          PrintWriter toClient = response.getWriter();


          String definition = request.getParameter("definition"); //Process Definition Key aus Camunda
          String output = "";

          	String activityString = SQL_queries.getAllActivities(definition);
          	activityString = activityString.substring(0, activityString.length() - 1);
          	String[] activities = activityString.split(";");
          	for (int i = 0; i< activities.length;i++){
          		output = output + activities[i] + "|";
          		ResultSet rs = SQL_queries.getJson(activities[i], definition);
          		try{
          			rs.next();
          			output = output + rs.getString("json");
          			toClient.println(output);
          			output = "";
          		} catch (Exception e){
          			e.printStackTrace();
          			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          		}
          	}
          }
    }
}

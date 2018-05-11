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
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetProcessCounter", urlPatterns = {"/WebContent/processCounter"})
public class GetProcessCounter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

      if(rolle<1){
        response.sendError(401);
      }
      else{
          PrintWriter toClient = response.getWriter();

          String instanceID = request.getParameter("instance_id");
          ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
          RuntimeService runtime = engine.getRuntimeService();

          if (instanceID != null) {
          	List<String> activitiesList = runtime.getActiveActivityIds(instanceID);
          	String query = "SELECT stepNumber FROM processModel WHERE model = ? AND step = ?";
              String[] args = new String[]{"studentBewerben", activitiesList.get(0)};
              String[] types = new String[]{"String","String"};
              ResultSet rs = SQL_queries.executeStatement(query,args,types);
              try{
                  if (rs.next()) {
                      toClient.print(rs.getString("stepNumber"));

                      query = "SELECT COUNT(*) AS num FROM processModel WHERE model = ?";
                      args = new String[]{"studentBewerben"};
                      types = new String[]{"String","String"};
                      rs = SQL_queries.executeStatement(query,args,types);

                      if (rs.next()) {
                          toClient.print(";"+rs.getString("num"));
                      }

                  } else {
                      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                  }
              }
              catch (Exception e){
                  e.printStackTrace();
                  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              }
          }
        }
    }
}

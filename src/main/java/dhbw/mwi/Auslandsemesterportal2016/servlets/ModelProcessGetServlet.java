package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "ModelProcessGetServlet", urlPatterns = {"/WebContent/processmodel/get"})
public class ModelProcessGetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String model = request.getParameter("model");
        String step = request.getParameter("step");

        if (model != null && step != null) {

            String query = "SELECT json FROM processModel WHERE model = ? AND step = ?";
            String[] args = new String[]{model, step};
            String[] types = new String[]{"String","String"};
            ResultSet rs = SQL_queries.executeStatement(query,args,types);
            try{
                if (rs.next()) {
                    toClient.print(rs.getString("json"));
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    toClient.println("Error: can not find entry");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                toClient.println("Error: db error");
                toClient.println(e.getMessage());
            }

        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            toClient.println("Error: missing parameters");
        }
    }
}

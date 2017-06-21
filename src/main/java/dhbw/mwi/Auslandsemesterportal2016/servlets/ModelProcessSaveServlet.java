package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ModelProcessSaveServlet", urlPatterns = {"/WebContent/processmodel/save"})
public class ModelProcessSaveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String model = request.getParameter("model");
        String step = request.getParameter("step");
        String json = request.getParameter("json");
        String stepnumber = request.getParameter("stepnumber");

        if (model != null && step != null && json != null && stepnumber != null) {

            String query = "SELECT * FROM processModel WHERE model = ? AND step = ?";
            String[] args = new String[]{model, step};
            String[] types = new String[]{"String","String"};
            ResultSet rs = SQL_queries.executeStatement(query,args,types);
            try{
                if (rs.next()) {
                    String id = rs.getString("id");
                    query = "UPDATE processModel SET model = ?, step = ?, json = ?, stepNumber = ? WHERE id = ?";
                    args = new String[]{model, step, json, id, stepnumber};
                    types = new String[]{"String","String","String", "int", "int"};
                    SQL_queries.executeUpdate(query,args,types);
                } else {
                    query = "INSERT INTO processModel (model, step, json, stepNumber) VALUES " +
                            "(?,?,?,?)";
                    args = new String[]{model, step, json, stepnumber};
                    types = new String[]{"String","String","String","int"};
                    SQL_queries.executeUpdate(query,args,types);
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

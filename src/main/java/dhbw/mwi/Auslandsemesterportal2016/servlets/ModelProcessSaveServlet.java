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
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ModelProcessSaveServlet", urlPatterns = {"/WebContent/processmodel/save"})
public class ModelProcessSaveServlet extends HttpServlet {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = DB.getInstance();
        PrintWriter toClient = response.getWriter();

        // check if exists ==> update

        String query = "INSERT INTO processModel (model, step, json) VALUES " +
                "(?,?,?)";
        String[] args = new String[]{"", "", ""};
        String[] types = new String[]{"String","String","String"};
        SQL_queries.executeUpdate(query,args,types);
    }
}

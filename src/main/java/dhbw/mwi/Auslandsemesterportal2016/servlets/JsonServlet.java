package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "JsonServlet", urlPatterns = {"/WebContent/get_json"})
public class JsonServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	//Connection connection = DB.getInstance();
        PrintWriter out = response.getWriter();
        String stepId = "";
        String json = "";
        

        	stepId = request.getParameter("stepid");
        	json = SQL_queries.getJson(stepId);
            //rs = statement.executeQuery(sql);
        	out.print(json);
        	out.flush();
        	out.close();
        /*} catch (SQLException e) {
            e.printStackTrace();
        }

    }*/

}
}

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

@WebServlet(name = "GetBPMNServlet", urlPatterns = {"/WebContent/bpmn/get"})
public class GetBPMNServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String model = request.getParameter("model");

        final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z\\_\\-]*");

        // check for File inclusion vulnerability
        if (model != null && pattern.matcher(model).matches()) {

            java.lang.ClassLoader classLoader = getClass().getClassLoader();
            java.io.File file = new java.io.File(classLoader.getResource(model+".bpmn").getFile());

            try {
                if(file.exists() && !file.isDirectory()) {
                    java.io.FileInputStream fis = new java.io.FileInputStream(file);
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(fis, "UTF8"));
                    response.setContentType("application/octet-stream");
                    String s = null;
                    while((s = reader.readLine()) != null) {
                        toClient.println(s);
                    }
                    reader.close();
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    toClient.println("file not found");
                }
            } catch (Exception e) {

            }

        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            toClient.println("Error: missing parameters");
        }
    }
}

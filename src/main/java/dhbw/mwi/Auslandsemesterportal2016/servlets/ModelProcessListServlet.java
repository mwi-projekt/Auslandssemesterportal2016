package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "ModelProcessListServlet", urlPatterns = {"/WebContent/processmodel/list"})
public class ModelProcessListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter toClient = response.getWriter();

        String model = request.getParameter("model");

        if (model != null) {

            String query = "SELECT step FROM processModel WHERE model = ?";
            String[] args = new String[]{model};
            String[] types = new String[]{"String"};
            ResultSet rs = SQL_queries.executeStatement(query,args,types);
            try{
                while (rs.next()) {
                    toClient.print(rs.getString("step")+";");
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

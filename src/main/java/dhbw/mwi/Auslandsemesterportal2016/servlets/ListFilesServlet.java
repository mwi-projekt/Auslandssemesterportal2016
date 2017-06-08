package dhbw.mwi.Auslandsemesterportal2016.servlets;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "ListFilesServlet", urlPatterns = {"/WebContent/filelist"})
public class ListFilesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter outp = response.getWriter();
        File folder = new File("/var/www/files");
        File[] listOfFiles = folder.listFiles();

        response.setContentType("application/json");
        outp.println("{");
        outp.println("\"name\": \"files\",");
        outp.println("\"type\": \"folder\",");
        outp.println("\"path\": \""+ folder.getPath() +"\",");
        outp.println("\"items\":[");
        boolean first = true;

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if (!first) {
                    outp.print(", ");
                }
                outp.println("{");
                outp.println("\"name\": \""+ listOfFiles[i].getName() +"\",");
                outp.println("\"path\": \""+ listOfFiles[i].getPath() +"\",");
                outp.println("\"size\": 0");
                outp.println("}");
                first = false;
               /* "name" => $f,
                        "type" => "file",
                        "path" => $dir . '/' . $f,
                        "size" => filesize($dir . '/' . $f)*/
            }
        }

        outp.println("]");
        outp.println("}");
    }
}

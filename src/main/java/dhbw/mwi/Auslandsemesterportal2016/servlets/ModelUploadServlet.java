package dhbw.mwi.Auslandsemesterportal2016.servlets;

import com.google.common.io.Files;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

@WebServlet(name = "ModelUploadServlet", urlPatterns = {"/WebContent/model/upload"})
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class ModelUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Part filePart = null;

        try {
            filePart = request.getPart("file");

            if (filePart != null){
                System.out.println(getFileName(filePart));
                OutputStream outs = new FileOutputStream(new File("/var/www/files/"+getFileName(filePart)));
                byte[] buf = new byte[1024];
                int len;
                InputStream is = filePart.getInputStream();
                while((len=is.read(buf)) > 0){
                    outs.write(buf,0,len);
                }
                outs.close();
                is.close();

                out.print("jop");
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Error: wrong file");
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("Error: wrong file");
            out.flush();
        }

    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}

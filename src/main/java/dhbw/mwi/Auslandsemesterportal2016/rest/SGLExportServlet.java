package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.enums.*;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.Message;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.value.FileValue;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "sglExportServlet", urlPatterns = { "/sglExport" })
public class SGLExportServlet extends HttpServlet {

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 4) {
			response.sendError(401, "Rolle: " + rolle);
		} else {
            response.setHeader("Content-Disposition","attachment; filename=\"export.zip\"");
            response.setContentType("application/zip");

            Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);

            try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
                RuntimeService runtime = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
		        List<ProcessInstance> results = runtime.createProcessInstanceQuery().list();

                for (ProcessInstance result : results) {
                    String instanceId = result.getId();
                    List<String> activities = runtime.getActiveActivityIds(instanceId);
                    
                    if (activities.get(0).equals("abgeschlossen")) {
                        String lastName = runtime.getVariable(instanceId, "bewNachname").toString();
                        String firstName = runtime.getVariable(instanceId, "bewVorname").toString();
                        String kurs = runtime.getVariable(instanceId, "bewKurs").toString();
                        String shortName = kurs + "_" + lastName + "_" + firstName;

                        ZipEntry e = new ZipEntry(shortName + "/");
                        out.putNextEntry(e);

                        FileValue typedFileValue = (FileValue) runtime.getVariableTyped(instanceId, "dualisAuszug");
                        InputStream is = typedFileValue.getValue();

                        ZipEntry pdf = new ZipEntry(shortName + "/Dualis_Auszug.pdf");
                        out.putNextEntry(pdf);

                        byte[] buf = new byte[1024];
                        for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
                            out.write(buf, 0, nChunk);
                        }
                        out.closeEntry();

                        // create simple doc and write to a ByteArrayOutputStream
                        Document document = new Document();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PdfWriter.getInstance(document, baos);
                        float width = document.getPageSize().getWidth();
                        document.open();
    
                        float[] columnDefinitionSize = { 50F, 50F };
                        PdfPTable table = null;
                        PdfPCell cell = null;

                        table = new PdfPTable(columnDefinitionSize);
                        table.getDefaultCell().setBorder(0);
                        table.setHorizontalAlignment(0);
                        table.setTotalWidth(width - 72);
                        table.setLockedWidth(true);

                        cell = new PdfPCell(new Phrase(shortName));
                        cell.setColspan(columnDefinitionSize.length);
                        table.addCell(cell);

                        Map<String, Object> variables = runtime.getVariables(instanceId);

                        for (String key : variables.keySet().stream().sorted().collect(Collectors.toList())) {
                            Object obj = variables.get(key);
                            if (obj != null && !shouldIgnoreKey(key)) {
                                table.addCell(new Phrase(key, font8));
                                table.addCell(new Phrase(obj.toString(), font8));
                            }
                        }
                        
                        document.add(table);
                        document.close();
                        ZipEntry pdf2 = new ZipEntry(shortName + "/Angaben.pdf");
                        pdf2.setSize(baos.size());
                        out.putNextEntry(pdf2);
                        baos.writeTo(out);
                        out.closeEntry();
                    }
                }


                out.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    private static boolean shouldIgnoreKey(String key) {
		return (key.equals("dualisAuszug")
				|| key.equals("mailText"));
	}
}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

@WebServlet(name = "UploadServlet", urlPatterns = { "/upload" })
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class UploadServlet extends HttpServlet {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			String action = request.getParameter("action");
			String id = request.getParameter("instance");
			PrintWriter out = response.getWriter();
			Part filePart = null;

			System.out.println("File-Upload Servlet");
			if (id.equals("leer")) {
				out.print("Error: can not find process id");
				out.flush();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.close();
				return;
			}

			if (action == null || action.equals("")) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.print("Error: wrong action");
				out.flush();
				out.close();
				return;
			}

			try {
				filePart = request.getPart("file");

				if (filePart != null) {
					FileValue typedFileValue = Variables.fileValue(action + ".pdf").file(filePart.getInputStream())
							.create();
					processEngine.getRuntimeService().setVariable(id, action, typedFileValue);
					out.print("jop");
					out.flush();
					out.close();
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.print("Error: wrong file");
					out.flush();
					out.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.print("Error: wrong file");
				out.flush();
				out.close();
			}
		}
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
		Util.addResponseHeaders(request,response);
		Util.writeJson(response, new JsonObject());
	}
}

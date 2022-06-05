package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.value.FileValue;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebServlet(name = "GetProcessFileServlet", urlPatterns = { "/getProcessFile" })
public class GetProcessFileServlet extends HttpServlet {

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);
		// checks if the file exists
		String instanceID = request.getParameter("instance_id");
		String key = request.getParameter("key");
		if (instanceID == null || key == null) {
			response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
			return;
		}

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtime = engine.getRuntimeService();
		FileValue typedFileValue = (FileValue) runtime.getVariableTyped(instanceID, key);
		if (typedFileValue != null) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle < 1) {
			response.sendError(401);
		} else {
			ServletOutputStream toClient = response.getOutputStream();

			String instanceID = request.getParameter("instance_id");
			String key = request.getParameter("key");
			if (instanceID == null || key == null) {
				response.sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
				return;
			}

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			FileValue typedFileValue = (FileValue) runtime.getVariableTyped(instanceID, key);
			InputStream is = null;
			try {
				is = typedFileValue.getValue();
			} catch (NullPointerException e) {
				response.sendError(SC_NOT_FOUND);
			}

			try {
				response.setContentType(typedFileValue.getMimeType());
				byte[] buf = new byte[1024];

				for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
					toClient.write(buf, 0, nChunk);
				}

				toClient.flush();

			} catch (Exception e) {

			}
		}
	}
}

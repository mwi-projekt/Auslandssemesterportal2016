package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.FileCreator;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "GetBPMNServlet", urlPatterns = { "/bpmn/get" })
public class GetBPMNServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request, response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle != 1) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		PrintWriter toClient = response.getWriter();

		String model = request.getParameter("model");
		final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z\\_\\-]*");
		// check for File inclusion vulnerability
		if (model == null || !pattern.matcher(model).matches()) {
			response.setStatus(SC_INTERNAL_SERVER_ERROR);
			toClient.println(PARAMMISSING);
			toClient.close();
			return;
		}

		ClassLoader classLoader = getClass().getClassLoader();
		File file = FileCreator.getBPMNFile(model, classLoader);

		try {
			if (file.exists() && !file.isDirectory()) {
				java.io.FileInputStream fis = new java.io.FileInputStream(file);
				java.io.BufferedReader reader = new java.io.BufferedReader(
						new java.io.InputStreamReader(fis, StandardCharsets.UTF_8));
				response.setContentType("application/octet-stream");

				String s;
				while ((s = reader.readLine()) != null) {
					toClient.println(s);
				}
				toClient.close();
				reader.close();
			} else {
				response.setStatus(SC_INTERNAL_SERVER_ERROR);
				toClient.println("file not found");
				toClient.close();
			}
		} catch (Exception e) {
			response.sendError(SC_INTERNAL_SERVER_ERROR, "file error");
		}

	}


}

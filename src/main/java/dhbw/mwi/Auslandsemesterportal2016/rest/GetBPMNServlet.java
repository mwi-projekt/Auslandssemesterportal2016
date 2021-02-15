package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetBPMNServlet", urlPatterns = { "/bpmn/get" })
public class GetBPMNServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(401);
		} else {
			PrintWriter toClient = response.getWriter();

			String model = request.getParameter("model");

			final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z\\_\\-]*");

			// check for File inclusion vulnerability
			if (model != null && pattern.matcher(model).matches()) {

				java.lang.ClassLoader classLoader = getClass().getClassLoader();
				java.io.File file = new java.io.File(classLoader.getResource(model + ".bpmn").getFile());

				try {
					if (file.exists() && !file.isDirectory()) {
						java.io.FileInputStream fis = new java.io.FileInputStream(file);
						java.io.BufferedReader reader = new java.io.BufferedReader(
								new java.io.InputStreamReader(fis, "UTF8"));
						response.setContentType("application/octet-stream");

						String s = null;
						while ((s = reader.readLine()) != null) {
							toClient.println(s);
						}
						toClient.close();
						reader.close();
					} else {
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						toClient.println("file not found");
						toClient.close();
					}
				} catch (Exception e) {

				}

			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				toClient.println("Error: missing parameters");
				toClient.close();
			}
		}
	}
}

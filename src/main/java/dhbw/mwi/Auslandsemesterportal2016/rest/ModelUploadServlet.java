package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.FileCreator;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "ModelUploadServlet", urlPatterns = { "/model/upload" })
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class ModelUploadServlet extends HttpServlet {

	private static final String OUTPUT_STRING = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);
		if (rolle != 1) {
			response.sendError(SC_UNAUTHORIZED);
			return;
		}

		PrintWriter out = response.getWriter();
		Part filePart;

		response.setContentType("text/html");
		String action = request.getParameter("CKEditorFuncNum");

		try {
			filePart = request.getPart("upload");

			if (filePart == null) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.println(OUTPUT_STRING + action
						+ ", '', 'Datei fehlt');</script>");
				out.flush();
				return;
			}

			String fileName = getFileName(filePart);
			OutputStream outs = new FileOutputStream(FileCreator.getFileInUploadFolder(fileName));
			byte[] buf = new byte[1024];
			int len;
			InputStream is = filePart.getInputStream();
			while ((len = is.read(buf)) > 0) {
				outs.write(buf, 0, len);
			}

			out.println(OUTPUT_STRING + action
					+ ", '" + Config.UPLOAD_URL + fileName + "', '');</script>");
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
			out.println(OUTPUT_STRING + action
					+ ", '', 'Server Fehler');</script>");
			out.flush();
		}

	}

	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

}

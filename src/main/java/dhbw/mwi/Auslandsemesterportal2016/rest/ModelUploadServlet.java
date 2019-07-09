package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "ModelUploadServlet", urlPatterns = { "/model/upload" })
@MultipartConfig(maxFileSize = 16177215) // 16MB
public class ModelUploadServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(401);
		} else {
			PrintWriter out = response.getWriter();
			Part filePart = null;

			response.setContentType("text/html");

			String action = request.getParameter("CKEditorFuncNum");

			try {
				filePart = request.getPart("upload");

				if (filePart != null) {
					String fileName = getFileName(filePart);
					OutputStream outs = new FileOutputStream(new File(Config.UPLOAD_DIR + "/"  + fileName));
					byte[] buf = new byte[1024];
					int len;
					InputStream is = filePart.getInputStream();
					while ((len = is.read(buf)) > 0) {
						outs.write(buf, 0, len);
					}
					outs.close();
					is.close();

					out.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + action
							+ ", '" + Config.UPLOAD_URL + fileName + "', '');</script>");
					out.flush();
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + action
							+ ", '', 'Datei fehlt');</script>");
					out.flush();
				}

			} catch (Exception e) {
				e.printStackTrace();
				out.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + action
						+ ", '', 'Server Fehler');</script>");
				out.flush();
			}
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

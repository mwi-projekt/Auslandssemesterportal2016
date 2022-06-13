package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.FileCreator;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet(name = "ListFilesServlet", urlPatterns = { "/filelist" })
public class ListFilesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(SC_UNAUTHORIZED);
		} else {
			File folder = FileCreator.getUploadFolder();
			File[] listOfFiles = folder.listFiles();

			JsonObject json = new JsonObject();
			json.addProperty("name", "files");
			json.addProperty("type", "folder");
			json.addProperty("path", Config.UPLOAD_URL);
			JsonArray arr = new JsonArray();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					JsonObject file = new JsonObject();
					file.addProperty("name", listOfFiles[i].getName());
					file.addProperty("type", "file");
					file.addProperty("path", Config.UPLOAD_URL + listOfFiles[i].getName());
					file.addProperty("size", listOfFiles[i].length());
					arr.add(file);
				}
			}

			json.add("items", arr);
			Util.writeJson(response, json);
		}
	}
}

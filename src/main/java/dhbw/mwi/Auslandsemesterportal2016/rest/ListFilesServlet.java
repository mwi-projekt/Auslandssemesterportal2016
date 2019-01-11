package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.File;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "ListFilesServlet", urlPatterns = { "/filelist" })
public class ListFilesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		if (rolle != 1) {
			response.sendError(401);
		} else {
			File folder = new File("/var/www/files");
			File[] listOfFiles = folder.listFiles();

			JSONObject json = new JSONObject();
			json.put("name", "files");
			json.put("type", "folder");
			json.put("path", "http://193.196.7.215/files/");
			JSONArray arr = new JSONArray();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					JSONObject file = new JSONObject();
					file.put("name", listOfFiles[i].getName());
					file.put("type", "file");
					file.put("path", "http://193.196.7.215/files/" + listOfFiles[i].getName());
					file.put("size", listOfFiles[i].length());
					arr.put(file);
				}
			}

			json.put("items", arr);
			Util.writeJson(response, json);
		}
	}
}

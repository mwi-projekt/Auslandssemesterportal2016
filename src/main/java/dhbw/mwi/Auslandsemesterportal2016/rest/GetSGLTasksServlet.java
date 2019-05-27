package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetAdminTasksServlet", urlPatterns = { "/getTasks" })
public class GetSGLTasksServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int rolle = userAuthentification.isUserAuthentifiedByCookie(request);

		/*if (rolle != 1 && rolle != 2 && rolle != 4) {
			response.sendError(401, "Rolle: " + rolle);
		} else {*/

			JSONObject json = new JSONObject();
			JSONArray arr = new JSONArray();

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtime = engine.getRuntimeService();
			List<ProcessInstance> results = runtime.createProcessInstanceQuery().list();
			for (int i = 0; i < results.size(); i++) {
				String instanceId = results.get(i).getId();
				List<String> activities = runtime.getActiveActivityIds(instanceId);
				JSONObject obj = new JSONObject();
				System.out.println(activities);
				if (activities.get(0).equals("abgeschlossen") || activities.get(0).equals("datenValidieren")) {
					obj.put("id", instanceId);
					obj.put("name", runtime.getVariable(instanceId, "bewNachname"));
					obj.put("vname", runtime.getVariable(instanceId, "bewVorname"));
					obj.put("aktuelleUni", runtime.getVariable(instanceId, "aktuelleUni"));
					obj.put("kurs", runtime.getVariable(instanceId, "bewKurs"));
					obj.put("uni", runtime.getVariable(instanceId, "uni"));
					//zu Testzwecken hinzugefügt für AAA löschen 
					obj.put("matrikelnummer", runtime.getVariable(instanceId, "matrikelnummer"));
					if (activities.get(0).equals("abgeschlossen")) {
						obj.put("status", "complete");
					} else {
						obj.put("status", "validate");
					}

					arr.put(obj);
				}
			}
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(arr);
			out.flush();
			
			/*System.out.println(arr);
			json.put("data", arr);
			Util.writeJson(response, json);*/
		}
	}
//}

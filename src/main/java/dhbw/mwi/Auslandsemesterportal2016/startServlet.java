package dhbw.mwi.Auslandsemesterportal2016;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;


@WebServlet(name="HelloServlet", urlPatterns={"/processservlet"})
public class startServlet extends HttpServlet{
	
	
	private TaskService taskService;
	
	private static final long serialVersionUID = 4494530890303223968L;

	public void service (HttpServletRequest request, HttpServletResponse response) throws IOException{ 
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Auslandsemesterportal2016ProcessApplication test = new Auslandsemesterportal2016ProcessApplication();
		
		//Startet die Bewerbung
		ProcessInstance pI = test.bewerbungStarten(processEngine);
		String id = pI.getId();
		System.out.println("Started process instance id " + id);
		
		//Liste mit allen ID-Activitys des Prozesses
		
		List<String> activityIds = processEngine.getRuntimeService().getActiveActivityIds(id);
		
		//Map für Prozessvariablen
		Map<String,Object> inputVariablen = new HashMap<String, Object>();
		
		//Setzen Prozessvariablen
		inputVariablen.put("Vorname", "Michael");
		inputVariablen.put("Nachname", "Michael");
		inputVariablen.put("Email", "Michael");
		inputVariablen.put("Kurs", "Michael");
		inputVariablen.put("Universität", "Michael");
		inputVariablen.put("Notenschnitt", "Michael");
		inputVariablen.put("vorname", "Michael");

		//Erstellen eines Files
		FileValue typedFileValue = Variables
				  .fileValue("test.txt")
				  .file(new File("C:\\Users\\user\\Desktop\\test.txt"))
				  .mimeType("text/plain")
				  .encoding("UTF-8")
				  .create();
		processEngine.getRuntimeService().setVariable(id, "fileVariable", typedFileValue);
		
		
		if (activityIds.isEmpty() == true){
			System.out.println("Alter die Kacke ist leer");
			
		}else{
			//User Task beenden
			System.out.print("Task Id: " + activityIds.get(0));
			
			processEngine.getTaskService().complete(processEngine.getTaskService().createTaskQuery().processInstanceId(id).singleResult().getId(),inputVariablen);
			
			System.out.print("Nach Task!");
		}
		
		int nr = 0;
		
		response.setContentType("text/html;charset=UTF-8");
		
		nr = Integer.parseInt(request.getParameter("nummer"));
		
		PrintWriter toClient = response.getWriter();
		toClient.println("<html><body>");
		toClient.println("<h3>HelloServlet</h3>");

		toClient.println(nr);
		toClient.println("</html></body>");
				
	}
}

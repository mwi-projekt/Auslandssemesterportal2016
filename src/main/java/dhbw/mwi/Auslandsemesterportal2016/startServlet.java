package dhbw.mwi.Auslandsemesterportal2016;

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
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;


@WebServlet(name="HelloServlet", urlPatterns={"/processservlet"})
public class startServlet extends HttpServlet{
	
	
	private TaskService taskService;
	
	private static final long serialVersionUID = 4494530890303223968L;

	public void service (HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Auslandsemesterportal2016ProcessApplication test = new Auslandsemesterportal2016ProcessApplication();
		
		ProcessInstance pI = test.bewerbungStarten(processEngine);
		String id = pI.getId();
		System.out.println("Started process instance id " + id);
		
		//Liste mit allen ID-Activitys des Prozesses
		
		List<String> activityIds = processEngine.getRuntimeService().getActiveActivityIds(id);

		if (activityIds.isEmpty() == true){
			System.out.println("Alter die Kacke ist leer");
			
		}else{
			//User Task beenden
			System.out.print("Task Id: " + activityIds.get(0));
			
			processEngine.getTaskService().complete(processEngine.getTaskService().createTaskQuery().processInstanceId(id).singleResult().getId());
			
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

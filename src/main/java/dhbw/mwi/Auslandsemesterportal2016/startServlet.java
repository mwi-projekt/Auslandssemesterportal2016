package dhbw.mwi.Auslandsemesterportal2016;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;


@WebServlet(name="HelloServlet", urlPatterns={"/processservlet"})
public class startServlet extends HttpServlet{
	
	public void service (HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Auslandsemesterportal2016ProcessApplication test = new Auslandsemesterportal2016ProcessApplication();
		test.onDeploymentFinished(processEngine);
		
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

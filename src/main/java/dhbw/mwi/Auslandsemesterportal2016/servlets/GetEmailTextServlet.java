package dhbw.mwi.Auslandsemesterportal2016.servlets;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GetEmailTextServlet", urlPatterns = {"/WebContent/getMailText"})
public class GetEmailTextServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setCharacterEncoding("UTF-8");
        PrintWriter toClient = response.getWriter();
        
        String instanceID = request.getParameter("instance_id");
        String validation_result = request.getParameter("validate");

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtime = engine.getRuntimeService();
        String student_name = runtime.getVariable(instanceID, "bewNachname").toString();
        String uni = runtime.getVariable(instanceID, "uni").toString();
        String output = "";
        
        if (validation_result.equals("true")){
        	//Text für erfolgreiche Bewerbung
        	output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + 
    				"\n"+ 
    				"\n"+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "+ uni +" wurde erfolgreich an das Akademisches Auslandsamt versendet."+
    				"\n"+
    				"-- Platzhalter für Anmerkungen des Auslandsamts --" + 
    				"\n"+
    				"\n"+
    				"\n"+ "Im nächsten Schritt wird sich ein Mitarbeiter zeitnah um die Bearbeitung Ihrer Bewerbung kümmern und entscheiden, ob Sie in die engere Auswahl potentieller Bewerber kommen."+ 
    				"\n"+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren." +  
    				"\n"+ 
    				"\n"+ "Mit freundlichen Grüßen," + 
    				"\n"+ 
    				"\n"+ "Ihr Akademisches Auslandsamt";
        } else {
        	//Text für abgelehnte Bewerbung
        	output = "Sehr geehrte/r Herr/Frau " + student_name + (",") + 
    				"\n"+ 
    				"\n"+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: "+ uni + 
    				"\n"+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + 
    				"\n"+		
    				"\n"+ "Folgende Problem hat sich ergeben:" +
    				"\n -- Platzhalter für Erläuterung des Problems --" +
    				"\n"+
    				"\n"+ "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können." +
    				"\n"+ "Wir bitten um Ihr Verständnis." +
    				"\n"+ 
    				"\n"+ "Mit freundlichen Grüßen," + 
    				"\n"+ 
    				"\n"+ "Ihr Akademisches Auslandsamt";
        }
        
       toClient.print(output); 
    }
}

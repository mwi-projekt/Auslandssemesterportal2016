	package dhbw.mwi.Auslandsemesterportal2016;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * Process Application exposing this application's resources the process engine. 
 */
@ProcessApplication ("Auslandsemesterportal2016")
public class Auslandsemesterportal2016ProcessApplication extends ServletProcessApplication {

 // private static final String PROCESS_DEFINITION_KEY = "Auslandsemesterportal2016";

  /**
   * Innerhalb dieser Methode wird der Prozess "studentBewerben.bpmn" angestoßen.
   */
  
  public ProcessInstance bewerbungStarten(ProcessEngine processEngine) {
	return processEngine.getRuntimeService().startProcessInstanceByKey("studentBewerben");
  }
  
  public ProcessInstance bewerbungStartenSchottland(ProcessEngine processEngine) {
		return processEngine.getRuntimeService().startProcessInstanceByKey("schottland");
  }
  
  public ProcessInstance bewerbungStartenCaliforniaSM(ProcessEngine processEngine) {
		return processEngine.getRuntimeService().startProcessInstanceByKey("california-sm");
  }
  
  public ProcessInstance bewerbungStartenCaliforniaCI(ProcessEngine processEngine) {
		return processEngine.getRuntimeService().startProcessInstanceByKey("california-ci");
  }
  
  public ProcessInstance bewerbungStartenPoland(ProcessEngine processEngine) {
		return processEngine.getRuntimeService().startProcessInstanceByKey("poland");
  }
  
  public ProcessInstance bewerbungStartenCroatia(ProcessEngine processEngine) {
		return processEngine.getRuntimeService().startProcessInstanceByKey("croatia");
  }

}

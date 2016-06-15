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

    // start an initial process instance
	return processEngine.getRuntimeService().startProcessInstanceByKey("studentBewerben");
  }

}

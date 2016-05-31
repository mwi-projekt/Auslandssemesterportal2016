package dhbw.mwi.Auslandsemesterportal2016;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.application.impl.ServletProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;

/**
 * Process Application exposing this application's resources the process engine. 
 */
@ProcessApplication ("Auslandsemesterportal2016")
public class Auslandsemesterportal2016ProcessApplication extends ServletProcessApplication {

 // private static final String PROCESS_DEFINITION_KEY = "Auslandsemesterportal2016";

  /**
   * In a @PostDeploy Hook you can interact with the process engine and access 
   * the processes the application has deployed. 
   */
  @PostDeploy
  public void onDeploymentFinished(ProcessEngine processEngine) {

    // start an initial process instance
	 processEngine.getRuntimeService().startProcessInstanceByKey("studentBewerben");
  }

}

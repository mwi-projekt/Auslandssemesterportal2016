package dhbw.mwi.Auslandsemesterportal2016.camunda;

import com.google.common.annotations.VisibleForTesting;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class GoOutConnector implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        sendDataToGoOutForm(getRelevantProcessData(""));
    }

    @VisibleForTesting
    public BewerbungsDaten getRelevantProcessData(String instanceId) {
        RuntimeService runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();

        String vorname = String.valueOf(runtimeService.getVariable(instanceId, "bewVorname"));
        String nachname = String.valueOf(runtimeService.getVariable(instanceId, "bewNachname"));
        String semester = String.valueOf(runtimeService.getVariable(instanceId, "bewSemester"));

        return BewerbungsDaten.builder()
                .name(vorname + " " + nachname)
                .email(String.valueOf(runtimeService.getVariable(instanceId, "bewEmail")))
                .studiengang(String.valueOf(runtimeService.getVariable(instanceId, "bewStudiengang")))
                .aktuellesSemester(Integer.parseInt(semester.split(".")[0]))
                .uniPrio1(String.valueOf(runtimeService.getVariable(instanceId, "uni1")))
                .uniPrio2(String.valueOf(runtimeService.getVariable(instanceId, "uni2")))
                .uniPrio3(String.valueOf(runtimeService.getVariable(instanceId, "uni3")))
                .build();
    }

    @VisibleForTesting
    public void sendDataToGoOutForm(BewerbungsDaten bewerbungsDaten) {

    }
}

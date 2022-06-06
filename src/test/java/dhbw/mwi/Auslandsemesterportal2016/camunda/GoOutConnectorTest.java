package dhbw.mwi.Auslandsemesterportal2016.camunda;

import dhbw.mwi.Auslandsemesterportal2016.rest.CamundaHelper;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GoOutConnectorTest {

    private ProcessEngine processEngine;

    @Test
    void getRelevantProcessData() {
        // given
        BewerbungsDaten expected = BewerbungsDaten.builder()
                .name(TESTVORNAME.toString() + TESTNACHNAME)
                .geburtsdatum(TESTGEBURTSDATUM.toString())
                .email(TESTEMAIL.toString())
                .studiengang(TESTSTUDIENGANG.toString())
                .aktuellesSemester(2)
                .uniPrio1("California State University San Marcos (USA)")
                .uniPrio2("South-Eastern Finland University of Applied Sciences (Finnland)")
                .uniPrio3("Finnland")
                .begruendungBenachteiligt("")
                .build();
        String instanceId = prozessInstanzVorbereiten();

        // when
        BewerbungsDaten actual = new GoOutConnector().getRelevantProcessData(instanceId);

        //then
        assertEquals(expected, actual);
    }

    private String prozessInstanzVorbereiten() {
        CamundaHelper camundaHelper = new CamundaHelper(processEngine);
        String instanceId = camundaHelper.startProcess("standard");
        camundaHelper.processUntilSendToGoOut(instanceId);
        System.out.println(processEngine.getRuntimeService().getActiveActivityIds(instanceId));
//        assertEquals("datenAnGoOut", processEngine.getRuntimeService().getActiveActivityIds(instanceId));
        return instanceId;
    }
}
package dhbw.mwi.Auslandsemesterportal2016.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import dhbw.mwi.Auslandsemesterportal2016.rest.CamundaHelper;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.List;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GoOutFormularServiceTest {

    private ProcessEngine processEngine;
    private CamundaHelper camundaHelper;

    @BeforeEach
    void setUp() {
        this.camundaHelper = new CamundaHelper(processEngine);
    }

    @Test
    void getRelevantProcessData() {
        // given
        BewerbungsDaten expected = getBewerbungsDaten();
        String instanceId = prozessInstanzVorbereiten();

        // when
        BewerbungsDaten actual = new GoOutFormularService().getRelevantProcessData(instanceId);

        //then
        assertEquals(expected, actual);
    }

    private BewerbungsDaten getBewerbungsDaten() {
        return BewerbungsDaten.builder()
                .name(TESTVORNAME + " " + TESTNACHNAME)
                .geburtsdatum(TESTGEBURTSDATUM.toString())
                .email(TESTEMAIL.toString())
                .studiengang(TESTSTUDIENGANG.toString())
                .aktuellesSemester("2. Semester")
                .uniPrio1("California State University San Marcos (USA)")
                .uniPrio2("South-Eastern Finland University of Applied Sciences (Finnland)")
                .uniPrio3("Abertay University of Dundee (Schottland)")
                .benachteiligung("")
                .einverstaendnisBericht(true)
                .build();
    }

    private String prozessInstanzVorbereiten() {
        String instanceId = camundaHelper.startProcess("standard");
        camundaHelper.processUntilSendToGoOut(instanceId);
        return instanceId;
    }

    @Test
    void testGoOutWebsiteWithoutSubmitting() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setTimeout(10000);
        HtmlPage goOutWebsite = null;
        try {
            goOutWebsite = webClient.getPage("http://10.3.15.45:8085");
        } catch (IOException e) {
            fail();
        }

        HtmlPage result = null;
        GoOutFormularService goOutFormularService = new GoOutFormularService();
        goOutFormularService.fillForm(getBewerbungsDaten(), goOutWebsite);
        try {
            result = goOutFormularService.sendData(goOutWebsite);
        } catch (IOException e) {
            fail();
        }

        System.out.println(result.getBody().getTextContent());
    }
}
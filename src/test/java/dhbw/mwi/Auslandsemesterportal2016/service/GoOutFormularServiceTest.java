package dhbw.mwi.Auslandsemesterportal2016.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import dhbw.mwi.Auslandsemesterportal2016.rest.CamundaHelper;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
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
                .uniPrio3("Finnland")
                .begruendungBenachteiligt("")
                .build();
    }

    private String prozessInstanzVorbereiten() {
        CamundaHelper camundaHelper = new CamundaHelper(processEngine);
        String instanceId = camundaHelper.startProcess("standard");
        camundaHelper.processUntilSendToGoOut(instanceId);
        System.out.println(processEngine.getRuntimeService().getActiveActivityIds(instanceId));
//        assertEquals("datenAnGoOut", processEngine.getRuntimeService().getActiveActivityIds(instanceId));
        return instanceId;
    }

    @Test
    void testHtmlUnit() {
        WebClient webClient = new WebClient();

        try {
            HtmlPage wikipediaSearch = webClient.getPage("https://www.wikipedia.de");
            HtmlInput txtSearch = wikipediaSearch.getHtmlElementById("txtSearch");
            txtSearch.setValueAttribute("Duale Hochschule");

            HtmlForm form = wikipediaSearch.getForms().get(0);
            HtmlButton submit = form.getOneHtmlElementByAttribute("button", "id", "cmdSearch");
            HtmlPage newPage = submit.click();

            String content = newPage.getHtmlElementById("firstHeading").getTextContent();
            assertEquals("Duale Hochschule", content);
        } catch (IOException e) {
            fail();
        }

        webClient.close();
    }

    @Test
    void testGoOutWebsiteWithoutSubmitting() {
        WebClient webClient = new WebClient();
        HtmlPage goOutWebsite = null;
        try {
            goOutWebsite = webClient.getPage("https://www.karlsruhe.dhbw.de/international-office/go-out-auslandssemester.html");
        } catch (IOException e) {
            fail();
        }

        fillForm(getBewerbungsDaten(), goOutWebsite);
        
        assertEquals("Test Admin", goOutWebsite.getHtmlElementById("powermail_field_name").getAttribute("value"));
        assertEquals("01.01.2000", goOutWebsite.getHtmlElementById("powermail_field_geburtsdatum").getAttribute("value"));
    }

    @Test
    void testGetSubmitButton() {
        WebClient webClient = new WebClient();
        HtmlPage goOutWebsite = null;

        try {
            goOutWebsite = webClient.getPage("https://www.karlsruhe.dhbw.de/international-office/go-out-auslandssemester.html");
        } catch (IOException e) {
            fail();
        }
        List<HtmlSubmitInput> submitButton = goOutWebsite.getByXPath("/html/body/main/div/div[3]/div/div[2]/div[3]/div/div/div/form/fieldset/div[9]/div/div/input");
        assertEquals(1, submitButton.size());

        assertEquals("Absenden", submitButton.get(0).getAttribute("value"));
        assertEquals("btn btn-primary", submitButton.get(0).getAttribute("class"));
    }

    private void fillForm(BewerbungsDaten bewerbungsDaten, HtmlPage goOutWebsite) {
        HtmlInput fieldName = goOutWebsite.getHtmlElementById("powermail_field_name");
        fieldName.setValueAttribute(bewerbungsDaten.getName());

        HtmlInput fieldGeburtsdatum = goOutWebsite.getHtmlElementById("powermail_field_geburtsdatum");
        fieldGeburtsdatum.setValueAttribute(bewerbungsDaten.getGeburtsdatum());

        HtmlInput fieldEmail = goOutWebsite.getHtmlElementById("powermail_field_e_mail");
        fieldEmail.setValueAttribute(bewerbungsDaten.getEmail());

        HtmlInput fieldStudiengang = goOutWebsite.getHtmlElementById("powermail_field_studiengang");
        fieldStudiengang.setValueAttribute(bewerbungsDaten.getStudiengang());

        HtmlInput fieldStudiengangsleitung = goOutWebsite.getHtmlElementById("powermail_field_studiengangsleitung");
        fieldStudiengangsleitung.setValueAttribute(bewerbungsDaten.getStudiengangsleitung());

        HtmlInput fieldAktuellesSemester = goOutWebsite.getHtmlElementById("powermail_field_semesterzumzeitpunktderregistrierung");
        fieldAktuellesSemester.setValueAttribute("" + bewerbungsDaten.getAktuellesSemester());

        HtmlInput fieldUniPrio1 = goOutWebsite.getHtmlElementById("powermail_field_laenderbzwhochschulwahlerstwunsch");
        fieldUniPrio1.setValueAttribute(bewerbungsDaten.getUniPrio1());

        HtmlInput fieldUniPrio2 = goOutWebsite.getHtmlElementById("powermail_field_laenderbzwhochschulwahlzweitwunsch");
        fieldUniPrio2.setValueAttribute(bewerbungsDaten.getUniPrio2());

        HtmlInput fieldUniPrio3 = goOutWebsite.getHtmlElementById("powermail_field_laenderbzwhochschulwahldrittwunsch");
        fieldUniPrio3.setValueAttribute(bewerbungsDaten.getUniPrio3());

        HtmlInput fieldGenehmigungUnternehmen = goOutWebsite.getHtmlElementById("powermail_field_hatbereitseineabsprachemitdemunternehmenstattgefunden");
        fieldGenehmigungUnternehmen.setValueAttribute(bewerbungsDaten.isEinwilligungUnternehmen() ? "Ja" : "Nein");

        HtmlInput fieldGenehmigungStudiengangsleitung = goOutWebsite.getHtmlElementById("powermail_field_hatbereitseineabsprachemitderstudiengangsleitungstattgefunden");
        fieldGenehmigungStudiengangsleitung.setValueAttribute(bewerbungsDaten.isEinwilligungStudiengangsleiter() ? "Ja" : "Nein");

        HtmlRadioButtonInput fieldEinverstaendnisBerichtJa = goOutWebsite.getHtmlElementById("powermail_field_einverstaendniserklaerungbericht_1");
        HtmlRadioButtonInput fieldEinverstaendnisBerichtNein = goOutWebsite.getHtmlElementById("powermail_field_einverstaendniserklaerungbericht_2");
        if (bewerbungsDaten.isEinverstaendnisBericht()) {
            fieldEinverstaendnisBerichtJa.setChecked(true);
        } else {
            fieldEinverstaendnisBerichtNein.setChecked(true);
        }

        HtmlRadioButtonInput fieldEinverstaendnisDatenschutz = goOutWebsite.getHtmlElementById("powermail_field_einverstaendiserklaerungdatenschutz_1");
        fieldEinverstaendnisDatenschutz.setChecked(true);
    }
}
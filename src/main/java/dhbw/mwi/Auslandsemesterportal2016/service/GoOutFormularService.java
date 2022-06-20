package dhbw.mwi.Auslandsemesterportal2016.service;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.google.common.annotations.VisibleForTesting;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.IOException;
import java.util.List;

public class GoOutFormularService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        sendDataToGoOutForm(getRelevantProcessData(delegateExecution.getId()));
    }

    @VisibleForTesting
    public BewerbungsDaten getRelevantProcessData(String instanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        String vorname = String.valueOf(runtimeService.getVariable(instanceId, "bewVorname"));
        String nachname = String.valueOf(runtimeService.getVariable(instanceId, "bewNachname"));

        return BewerbungsDaten.builder()
                .name(vorname + " " + nachname)
                .geburtsdatum(String.valueOf(runtimeService.getVariable(instanceId, "bewGeburtsdatum")))
                .email(String.valueOf(runtimeService.getVariable(instanceId, "bewEmail")))
                .studiengang(String.valueOf(runtimeService.getVariable(instanceId, "bewStudiengang")))
                .aktuellesSemester(String.valueOf(runtimeService.getVariable(instanceId, "bewSemester")))
                .uniPrio1(String.valueOf(runtimeService.getVariable(instanceId, "uni1")))
                .uniPrio2(String.valueOf(runtimeService.getVariable(instanceId, "uni2")))
                .uniPrio3(String.valueOf(runtimeService.getVariable(instanceId, "uni3")))
                .einverstaendnisBericht(true) // TODO (Boolean) runtimeService.getVariable(instanceId, "bewErfahrungsberichtZustimmung"), sobald vorhanden
                .benachteiligung("") // TODO Wert abfragen, sobald vorhanden
                .build();
    }

    private void sendDataToGoOutForm(BewerbungsDaten bewerbungsDaten) {
        WebClient webClient = new WebClient();

        try {
            String url = "https://www.karlsruhe.dhbw.de/international-office/go-out-auslandssemester.html";
            String testUrl = "10.3.15.45:8085";
            HtmlPage goOutWebsite = webClient.getPage(testUrl);

            fillForm(bewerbungsDaten, goOutWebsite);

            sendData(goOutWebsite);
            webClient.close();
        } catch (IOException e) {
            // send Mail to Freytag?
        }
    }

    @VisibleForTesting
    public HtmlPage sendData(HtmlPage goOutWebsite) throws IOException {
        List<HtmlSubmitInput> submitButton = goOutWebsite.getByXPath("/html/body/main/div/div[3]/div/div[2]/div[3]/div/div/div/form/fieldset/div[9]/div/div/input");
        if (submitButton.size() == 1) {
            return submitButton.get(0).click();
        } else {
            throw new IOException("Element konnte nicht eindeutig gefunden werden.");
        }
    }

    @VisibleForTesting
    public void fillForm(BewerbungsDaten bewerbungsDaten, HtmlPage goOutWebsite) {
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

        HtmlInput fieldBenachteiligung = goOutWebsite.getHtmlElementById("powermail_field_zaehlensiesichinbezugaufihrebildungschancenzueinerbenachteiligtengruppezberstepersonausderfamiliediestudiertfallsjastellensiediesbittekurzda");
        fieldBenachteiligung.setValueAttribute(null == bewerbungsDaten.getBenachteiligung() ? "" : bewerbungsDaten.getBenachteiligung());

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

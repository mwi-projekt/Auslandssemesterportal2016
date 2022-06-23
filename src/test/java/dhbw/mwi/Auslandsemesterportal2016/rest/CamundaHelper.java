package dhbw.mwi.Auslandsemesterportal2016.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;

/**
 * Diese Klasse wird von den Unit-Test angesprochen, um Prozesse in einen bestimmten Status zu bringen (Task)
 * Es gibt folgende UserTasks:
 * downloadsAnbieten, universitaetAuswaehlen, datenEingeben, datenEingebenUnt, dualisHochladen, datenPruefen,
 * datenValidierenSGL, abgelehnt, abgeschlossen
 *
 * und folgende automatisierte Tasks:
 * goOutUebergeben, studentBenachrichtigenSGL
 */
public class CamundaHelper {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public CamundaHelper(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
    }

    /**
     * Prozess findet sich im Status "downloadsAnbieten"
     * @param model Name des BPMN-Modells
     * @return instanceId
     */
    public String startProcess(String model) {
        return runtimeService.startProcessInstanceByKey(model).getId();
    }

    /**
     * Prozess befindet sich im Status "downloadsAnbieten"
     * @param instanceId
     */
    public void processUntilUniversitaetAuswaehlen(String instanceId) {
        setVariables(instanceId);
        updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
    }

    /**
     * Prozess befindet sich im Status "datenPruefen" und es wurde ein Formular hochgeladen
     * @param instanceId
     */
    public void processHasDualisDocument(String instanceId) throws FileNotFoundException {
        processUntilUniversitaetAuswaehlen(instanceId);
        for (int i = 0; i<4; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
        Path path = Paths.get("src", "test", "resources");
        FileInputStream fileInputStream = new FileInputStream(path + "/DAAD_Formular_Englisch.pdf");
        FileValue fileValue = Variables.fileValue("daadHochladen.pdf").file(fileInputStream).create();
        runtimeService.setVariable(instanceId, "daadHochladen", fileValue);
        System.out.println(runtimeService.getVariable(instanceId, "daadHochladen"));
    }

    /**
     * Prozess befindet sich im Status "datenPruefen"
     * @param instanceId
     */
    public void processUntilDatenPruefen(String instanceId) {
        processUntilUniversitaetAuswaehlen(instanceId);
        for (int i=0; i<4; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
    }

    /**
     * Prozess befindet sich im Status "datenValidierenSGL"
     * @param instanceId
     */
    public void processUntilDatenValidierenSGL(String instanceId) {
        processUntilDatenPruefen(instanceId);
        updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
    }

    /**
     * Prozess wird zurückgesetzt und befindet sich im Status "downloadsAnbieten"
     * @param instanceId
     */
    public void processUntilUeberarbeiten(String instanceId) {
        processUntilDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONEDITSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    /**
     * Prozess befindet sich im Status "abgelehnt"
     * @param instanceId
     */
    public void processUntilAblehnen(String instanceId) {
        processUntilDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONREJECTEDSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    public void prepareProcessForTestGoOut(String instanceId) {
        setVariables(instanceId);
        Map<String, Object> map = new HashMap<>();
        // relevant für go out
        map.put("uni1", "California State University San Marcos (USA)");
        map.put("uni2", "South-Eastern Finland University of Applied Sciences (Finnland)");
        map.put("uni3", "Abertay University of Dundee (Schottland)");
        map.put("bewGeburtsdatum", "01.01.2000");
        map.put("bewSemester", "2. Semester");
        map.put("bewErfahrungsberichtZustimmung", true);
        //relevant für Camunda-Prozess
        map.put("gdprCompliance", Boolean.TRUE);
        map.put("zeitraum", "Frühlings-/Sommersemester 2023");
        map.put("bewTelefon", TESTTELNR.toString());
        map.put("bewStrasse", "TestStr. 1");
        map.put("bewPLZ", 12345);
        map.put("bewOrt", "Teststadt");
        map.put("bewLand", "Deutschland");
        map.put("bewStudiengang", TESTSTUDIENGANG.toString());
        map.put("muttersprache", "");
        map.put("bewErasmus", "nein");
        map.put("bewLA", "nein");
        map.put("bewSGL", "Frau Wallrath");
        map.put("untName", "ABC AG");
        map.put("untStrasse", "Teststr. 2");
        map.put("untPLZ", 12345);
        map.put("untOrt", "Teststadt");
        map.put("untLand", "Deutschland");
        map.put("untAnsprechpartner", "Frau Holle");
        map.put("untEmail", "test@unternehmen.de");
        map.put("englischNote", "10");
        map.put("semesteradresseAnders", Boolean.FALSE);
        map.put("validierungErfolgreich", true);
        map.put("mailText", "any mail text");
        completeTask(instanceId, map);
    }

    private void setVariables(String instanceId) {
        runtimeService.setVariable(instanceId, "bewNachname", TESTNACHNAME.toString());
        runtimeService.setVariable(instanceId, "bewVorname", TESTVORNAME.toString());
        runtimeService.setVariable(instanceId, "bewEmail", TESTEMAIL.toString());
        runtimeService.setVariable(instanceId, "matrikelnummer", Integer.parseInt(TESTMATRIKELNUMMER.toString()));
        runtimeService.setVariable(instanceId, "aktuelleUni", TESTSTANDORT.toString());
        runtimeService.setVariable(instanceId, "bewStudiengang", TESTSTUDIENGANG.toString());
        runtimeService.setVariable(instanceId, "bewKurs", TESTKURS.toString());
        runtimeService.setVariable(instanceId, "prioritaet", 1);
        runtimeService.setVariable(instanceId, "uni", "USA");
        runtimeService.setVariable(instanceId, "uploadformular", "anyData");
    }

    private void updateInstance(String instanceId, String key, String value, String type) {
        Map<String, Object> variablesMap = getVariablesMap(key, value, type);
        completeTask(instanceId, variablesMap);
    }

    private Map<String, Object> getVariablesMap(String key, String value, String type) {
        String[] keys = key.split("\\|", -1);
        String[] values = value.split("\\|", -1);
        String[] types = type.split("\\|", -1);

        return initHashMap(keys, values, types);
    }

    private HashMap<String, Object> initHashMap(String[] keys, String[] values, String[] types) {
        HashMap<String, Object> variablesMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            switch (types[i]) {
                case "text":
                case "email":
                    variablesMap.put(keys[i], values[i]);
                    break;
                case "number":
                    if (values[i].equals("")) {
                        values[i] = "0";
                    }
                    variablesMap.put(keys[i], Integer.parseInt(values[i]));
                    break;
                case "boolean":
                    variablesMap.put(keys[i], Boolean.parseBoolean(values[i]));
                    break;
                default:
                    break;
            }
        }
        return variablesMap;
    }

    private void completeTask(String instanceId, Map<String, Object> variablesMap) {
        taskService.complete(taskService.createTaskQuery().processInstanceId(instanceId).singleResult().getId(),
                variablesMap);
    }
}

package dhbw.mwi.Auslandsemesterportal2016.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;

public class CamundaHelper {


    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public CamundaHelper(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
    }

    public String startProcess(String model) {
        return runtimeService.startProcessInstanceByKey(model).getId();
    }

    public void processUntilUniversitaetAuswaehlen(String instanceId) {
        setVariables(instanceId);
        updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
    }

    public void processUntilDaadHochladen(String instanceId) throws FileNotFoundException {
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

    public void processUntilDatenPruefen(String instanceId) {
        processUntilUniversitaetAuswaehlen(instanceId);
        for (int i=0; i<9; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
    }

    public void processUntilDatenValidierenSGL(String instanceId) {
        processUntilDatenPruefen(instanceId);
        updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
    }

    public void processUntilUeberarbeiten(String instanceId) {
        processUntilDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONEDITSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    public void processUntilAblehnen(String instanceId) {
        processUntilDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONREJECTEDSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    public void processUntilValidierenAAA(String instanceId) {
        processUntilDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    public void processUntilAbschliessen(String instanceId) {
        processUntilValidierenAAA(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
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

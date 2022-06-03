package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetAdminTasksServletTest {

    private ProcessEngine processEngine;
    private RuntimeService runtimeService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        runtimeService = processEngine.getRuntimeService();
    }

    @AfterEach
    void tearDown() {
        userAuthentificationMockedStatic.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 3})
    void doGetUnauthorizedRole(int role) throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        new GetAdminTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED, "Rolle: " + role);
    }

    @Test
    void doGetNoInstances() throws IOException {
        new GetAdminTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("{\"data\":[]}", writer.toString().trim());
    }

    @Test
    void doGetSortsStatusCorrectly() throws IOException {
        // given
        runtimeService.startProcessInstanceByKey("standard").getId();

        String datenValidierenSGLInstanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzDatenValidierenSGL(datenValidierenSGLInstanceId);

        String abgelehnteInstanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzAblehnen(abgelehnteInstanceId);

        String abgeschlosseneInstanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzAbschliessen(abgeschlosseneInstanceId);

        String datenValidierenInstanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzAAAValidieren(datenValidierenInstanceId);

        // when
        new GetAdminTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        String expectedString = writer.toString().trim();
        assertNotNull(expectedString); // Array ist nicht leer

        JsonArray expectedAsJsonArray = new JsonParser() //
                .parse(expectedString) //
                .getAsJsonObject() //
                .get("data") //
                .getAsJsonArray();
        assertEquals(4, expectedAsJsonArray.size()); // Array enthält 4 Einträge

        List<JsonObject> expectedObjectsAsList = Arrays.asList(expectedAsJsonArray.get(0).getAsJsonObject(),
                expectedAsJsonArray.get(1).getAsJsonObject(),
                expectedAsJsonArray.get(2).getAsJsonObject(),
                expectedAsJsonArray.get(3).getAsJsonObject());

        for (JsonObject object : expectedObjectsAsList) { // Alle Einträge enthalten die notwendigen Variablen
            assertEquals("Student", object.getAsJsonObject().get("name").getAsString());
            assertEquals("Test", object.getAsJsonObject().get("vname").getAsString());
            assertEquals("DHBW Karlsruhe", object.getAsJsonObject().get("aktuelleUni").getAsString());
            assertEquals("WWI18B1", object.getAsJsonObject().get("kurs").getAsString());
            assertEquals("USA", object.getAsJsonObject().get("uni").getAsString());
            assertEquals("1", object.getAsJsonObject().get("prioritaet").getAsString());
            assertEquals("190190190", object.getAsJsonObject().get("matrikelnummer").getAsString());
        }

        // alle 4 relevanten Status sind enthalten
        assertEquals(1, getNumberOfObjectsWithStatus(expectedObjectsAsList, "validate"));
        assertEquals(1, getNumberOfObjectsWithStatus(expectedObjectsAsList, "validateSGL"));
        assertEquals(1, getNumberOfObjectsWithStatus(expectedObjectsAsList, "complete"));
        assertEquals(1, getNumberOfObjectsWithStatus(expectedObjectsAsList, "abgelehnt"));
    }

    private long getNumberOfObjectsWithStatus(List<JsonObject> expectedObjectsAsList, String status) {
        return expectedObjectsAsList //
                .stream() //
                .filter(o -> o.get("status").getAsString().equals(status)) //
                .count();
    }

    private void prozessInstanzDatenValidierenSGL(String instanceId) {
        setVariables(instanceId);
        for (int i=0; i<11; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
    }

    private void prozessInstanzAblehnen(String instanceId) {
        prozessInstanzDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONREJECTEDSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    private void prozessInstanzAAAValidieren(String instanceId) {
        prozessInstanzDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    private void prozessInstanzAbschliessen(String instanceId) {
        prozessInstanzAAAValidieren(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    private void updateInstance(String instanceId, String key, String value, String type) {
        Map<String, Object> variablesMap = getVariablesMap(key, value, type);
        completeTask(instanceId, variablesMap);
    }

    private void completeTask(String instanceId, Map<String, Object> variablesMap) {
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskService.createTaskQuery().processInstanceId(instanceId).singleResult().getId(),
                variablesMap);
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
                    variablesMap.put(keys[i], values[i]);
                    break;
                case "number":
                    if (values[i].equals("")) {
                        values[i] = "0";
                    }
                    variablesMap.put(keys[i], Integer.parseInt(values[i]));
                    break;
                case "email":
                    variablesMap.put(keys[i], values[i]);
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
}
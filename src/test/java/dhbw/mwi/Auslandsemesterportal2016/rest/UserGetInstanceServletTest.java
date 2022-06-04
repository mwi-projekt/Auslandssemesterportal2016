package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.getStepCounter;
import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.getUserInstances;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class UserGetInstanceServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ProcessEngine processEngine;
    private RuntimeService runtimeService;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("matnr")).thenReturn(TESTMATRIKELNUMMER.toString());

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(3);

        runtimeService = processEngine.getRuntimeService();
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        sqlQueriesMockedStatic.close();
        userAuthentificationMockedStatic.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void doGetUnauthorizedRole(int role) throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        new UserGetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doGetMatrikelnummerMissing() throws IOException {
        when(request.getParameter("matnr")).thenReturn(null);

        new UserGetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doGetNoProcessesAvailable() throws IOException {
        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(new ArrayList<String[]>());

        new UserGetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("{\"data\":[]}", writer.toString().trim());
    }

    @Test
    void doGetProcessesWithDifferentStatusAvailable() throws IOException {
        // FIXME abgelehnt wird von Camunda nicht korrekt verarbeitet
        String abgeschlossenId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzAbschliessen(abgeschlossenId);

        String datenPruefenId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzDatenPruefen(datenPruefenId);

        String datenValidierenId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzDatenValidierenSGL(datenValidierenId);

        String abgelehntId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzAblehnen(abgelehntId);

        String uniAuswaehlenId = runtimeService.startProcessInstanceByKey("standard").getId();
        prozessInstanzUniWaehlen(uniAuswaehlenId);

        ArrayList<String[]> userInstances = new ArrayList<>();
        userInstances.add(new String[] {"USA", abgeschlossenId, "1"});
        userInstances.add(new String[] {"USA", datenPruefenId, "1"});
        userInstances.add(new String[] {"USA", datenValidierenId, "1"});
        userInstances.add(new String[] {"USA", abgelehntId, "1"});
        userInstances.add(new String[] {"USA", uniAuswaehlenId, "1"});

        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(userInstances);
        sqlQueriesMockedStatic.when(() -> getStepCounter("universitaetAuswaehlen", "standard"))
                .thenReturn("2");

        new UserGetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"data\":[" +
                "{\"instanceID\":\"" + abgeschlossenId + "\",\"uni\":\"USA\",\"stepCounter\":\"Abgeschlossen\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + datenPruefenId + "\",\"uni\":\"USA\",\"stepCounter\":\"Daten prüfen\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + datenValidierenId + "\",\"uni\":\"USA\",\"stepCounter\":\"Auf Rückmeldung warten\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + abgelehntId + "\",\"uni\":\"USA\",\"stepCounter\":\"Bewerbung wurde abgelehnt\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + uniAuswaehlenId + "\",\"uni\":\"USA\",\"stepCounter\":\"2\",\"prioritaet\":\"1\"}" +
                "]}";
        assertEquals(expected, writer.toString().trim());
    }

    private void prozessInstanzUniWaehlen(String instanceId) {
        setVariables(instanceId);
        for (int i=0; i<1; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
    }

    private void prozessInstanzDatenPruefen(String instanceId) {
        setVariables(instanceId);
        for (int i=0; i<10; i++) {
            updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
        }
    }

    private void prozessInstanzDatenValidierenSGL(String instanceId) {
        prozessInstanzDatenPruefen(instanceId);
        updateInstance(instanceId, TESTKEYSTRING.toString(), TESTVALSTRING.toString(), TESTTYPESTRING.toString());
    }

    private void prozessInstanzAblehnen(String instanceId) {
        prozessInstanzDatenValidierenSGL(instanceId);
        updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONREJECTEDSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
    }

    private void prozessInstanzAbschliessen(String instanceId) {
        prozessInstanzDatenValidierenSGL(instanceId);
        for (int i=0; i<2; i++) {
            updateInstance(instanceId, TESTKEYVALIDATESTRING.toString(), TESTVALUEVALIDATIONSTRING.toString(), TESTTYPEVALIDATIONSTRING.toString());
        }
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
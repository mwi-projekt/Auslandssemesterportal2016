package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetSGLTasksServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private ProcessEngine processEngine;
    private CamundaHelper camundaHelper;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        // kann nicht im Konstruktor (Timing-Problem) oder BeforeClass-Methode (static) initialisiert werden
        camundaHelper = new CamundaHelper(processEngine);
    }

    @AfterEach
    void tearDown() {
        userAuthentificationMockedStatic.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 3})
    void doGetUnauthorizedRole(int role) throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        new GetSGLTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED, "Rolle: " + role);
    }

    @Test
    void doGetNoTasks() throws IOException {
        new GetSGLTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("{\"data\":[]}", writer.toString().trim());
    }

    @Disabled
    @Test
    void doGetTasksWithDifferentStatus() throws IOException {
        // FIXME "abgelehnt" wird nicht richtig von Camunda gesetzt
        // given
        camundaHelper.startProcess("standard");
        String editInstanceId = camundaHelper.startProcess("standard");
        String selectUniInstanceId = camundaHelper.startProcess("standard");
        String validateSGLInstanceId = camundaHelper.startProcess("standard");
        String rejectedInstanceId = camundaHelper.startProcess("standard");
        String completedInstanceId = camundaHelper.startProcess("standard");
        String validateAAAInstanceId = camundaHelper.startProcess("standard");

        processInstancesToSpecificActivity(editInstanceId,
                selectUniInstanceId,
                validateSGLInstanceId,
                rejectedInstanceId,
                completedInstanceId,
                validateAAAInstanceId);
        System.out.println(processEngine.getRuntimeService().getActiveActivityIds(rejectedInstanceId));

        // when
        new GetSGLTasksServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        String expectedEdit = "{\"id\":\"" + editInstanceId + "\",\"name\":\"Student\",\"vname\":\"Test\",\"aktuelleUni" +
                "\":\"DHBW Karlsruhe\",\"kurs\":\"WWI18B1\",\"uni\":\"USA\",\"prioritaet\":\"1\",\"matrikelnummer\":\"" +
                "190190190\",\"status\":\"edit\"}";
        String expectedValidateSGL = "{\"id\":\"" + validateSGLInstanceId + "\",\"name\":\"Student\",\"vname\":\"" +
                "Test\",\"aktuelleUni\":\"DHBW Karlsruhe\",\"kurs\":\"WWI18B1\",\"uni\":\"USA\",\"prioritaet\":\"1\",\"" +
                "matrikelnummer\":\"190190190\",\"status\":\"validateSGL\"}";
        String expectedRejected = "{\"id\":\"" + rejectedInstanceId + "\",\"name\":\"Student\",\"vname\":\"Test\",\"" +
                "aktuelleUni\":\"DHBW Karlsruhe\",\"kurs\":\"WWI18B1\",\"uni\":\"USA\",\"prioritaet\":\"1\",\"" +
                "matrikelnummer\":\"190190190\",\"status\":\"abgelehnt\"}";
        String expectedCompleted = "{\"id\":\"" + completedInstanceId + "\",\"name\":\"Student\",\"vname\":\"Test" +
                "\",\"aktuelleUni\":\"DHBW Karlsruhe\",\"kurs\":\"WWI18B1\",\"uni\":\"USA\",\"prioritaet\":\"1\",\"" +
                "matrikelnummer\":\"190190190\",\"status\":\"complete\"}";
        String expectedValidateAAA = "{\"id\":\"" + validateAAAInstanceId + "\",\"name\":\"Student\",\"vname\":\"" +
                "Test\",\"aktuelleUni\":\"DHBW Karlsruhe\",\"kurs\":\"WWI18B1\",\"uni\":\"USA\",\"prioritaet\":\"1\",\"" +
                "matrikelnummer\":\"190190190\",\"status\":\"validate\"}";

        String actual = writer.toString().trim();
        assertNotNull(actual); // Array ist nicht leer
        JsonArray actualAsJsonArray = getJsonElement(actual) //
                .getAsJsonObject() //
                .get("data") //
                .getAsJsonArray();
        assertEquals(5, actualAsJsonArray.size()); // Array enthält 5 Einträge
        // Array enthält 5 verschiedene Status
        assertTrue(actualAsJsonArray.contains(getJsonElement(expectedEdit)));
        assertTrue(actualAsJsonArray.contains(getJsonElement(expectedValidateSGL)));
        assertTrue(actualAsJsonArray.contains(getJsonElement(expectedCompleted)));
        assertTrue(actualAsJsonArray.contains(getJsonElement(expectedValidateAAA)));
        assertTrue(actualAsJsonArray.contains(getJsonElement(expectedRejected)));
    }

    private void processInstancesToSpecificActivity(String editInstanceId, String uniWaehlenInstanceId, String datenValidierenSGLInstanceId, String abgelehnteInstanceId, String abgeschlosseneInstanceId, String datenValidierenInstanceId) {
        camundaHelper.processUntilAblehnen(abgelehnteInstanceId);
        camundaHelper.processUntilUeberarbeiten(editInstanceId);
        camundaHelper.processUntilUniversitaetAuswaehlen(uniWaehlenInstanceId);
        camundaHelper.processUntilDatenValidierenSGL(datenValidierenSGLInstanceId);
        camundaHelper.processUntilAbschliessen(abgeschlosseneInstanceId);
        camundaHelper.processUntilValidierenAAA(datenValidierenInstanceId);
    }

    private JsonElement getJsonElement(String expectedEdit) {
        return new JsonParser().parse(expectedEdit);
    }
}
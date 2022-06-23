package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
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
import java.util.ArrayList;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.getStepCounter;
import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.getUserInstances;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTMATRIKELNUMMER;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class UserGetInstanceServletTest {

    private CamundaHelper camundaHelper;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ProcessEngine processEngine;
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

        // kann nicht im Konstruktor (Timing-Problem) oder BeforeClass-Methode (static) initialisiert werden
        camundaHelper = new CamundaHelper(processEngine);
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

    @Disabled("abgelehnt wird von Camunda nicht korrekt verarbeitet")
    @Test
    void doGetProcessesWithDifferentStatusAvailable() throws IOException {
        String datenPruefenId = camundaHelper.startProcess("standard");
        String datenValidierenId = camundaHelper.startProcess("standard");
        String abgelehntId = camundaHelper.startProcess("standard");
        String uniAuswaehlenId = camundaHelper.startProcess("standard");
        processInstancesToSpecificActivity(datenPruefenId, datenValidierenId, abgelehntId, uniAuswaehlenId);

        ArrayList<String[]> userInstances = addUserInstances(datenPruefenId,
                datenValidierenId,
                abgelehntId,
                uniAuswaehlenId);

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
                "{\"instanceID\":\"" + datenPruefenId + "\",\"uni\":\"USA\",\"stepCounter\":\"Daten prüfen\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + datenValidierenId + "\",\"uni\":\"USA\",\"stepCounter\":\"Auf Rückmeldung warten\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + abgelehntId + "\",\"uni\":\"USA\",\"stepCounter\":\"Bewerbung wurde abgelehnt\",\"prioritaet\":\"1\"}," +
                "{\"instanceID\":\"" + uniAuswaehlenId + "\",\"uni\":\"USA\",\"stepCounter\":\"2\",\"prioritaet\":\"1\"}" +
                "]}";
        assertEquals(expected, writer.toString().trim());
    }

    private ArrayList<String[]> addUserInstances(String datenPruefenId, String datenValidierenId, String abgelehntId, String uniAuswaehlenId) {
        ArrayList<String[]> userInstances = new ArrayList<>();
        userInstances.add(new String[] {"USA", datenPruefenId, "1"});
        userInstances.add(new String[] {"USA", datenValidierenId, "1"});
        userInstances.add(new String[] {"USA", abgelehntId, "1"});
        userInstances.add(new String[] {"USA", uniAuswaehlenId, "1"});
        return userInstances;
    }

    private void processInstancesToSpecificActivity(String datenPruefenId, String datenValidierenId, String abgelehntId, String uniAuswaehlenId) {
        camundaHelper.processUntilDatenPruefen(datenPruefenId);
        camundaHelper.processUntilDatenValidierenSGL(datenValidierenId);
        camundaHelper.processUntilAblehnen(abgelehntId);
        camundaHelper.processUntilUniversitaetAuswaehlen(uniAuswaehlenId);
    }
}
package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
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
import java.util.List;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.*;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetInstanceServletTest {

    private ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("matnr")).thenReturn(TESTMATRIKELNUMMER.toString());
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());
        when(request.getParameter("prio")).thenReturn("2");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
        processEngine.close();
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(-1);

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401);
    }

    @Test
    void doGetExistingProcessInstanceWithKnownUser() throws IOException {
        String instanceId = processEngine.getRuntimeService().startProcessInstanceByKey("standard").getId();
        String expected = "{\"instanceId\":\"" + instanceId + "\",\"uni\":\"Karlsruhe\"}";

        sqlQueriesMockedStatic.when(() -> getModel(TESTSTANDORT.toString())).thenReturn("standard");
        sqlQueriesMockedStatic.when(() ->
                getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), TESTSTANDORT.toString()))
                .thenReturn(instanceId);

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(expected, writer.toString().trim());
        assertEquals(1, processEngine.getRuntimeService().createProcessInstanceQuery().active().list().size());
    }

    @Test
    void doGetNewProcessInstance() throws IOException {
        String expectedSubstring1 = "{\"instanceId\":\"";
        String expectedSubstring2 = "\",\"uni\":\"Karlsruhe\"}";

        sqlQueriesMockedStatic.when(() -> getModel(TESTSTANDORT.toString())).thenReturn("standard");
        sqlQueriesMockedStatic.when(() ->
                        getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), TESTSTANDORT.toString()))
                .thenReturn("");
        String[] user = new String[] {TESTNACHNAME.toString(), TESTVORNAME.toString(), TESTEMAIL.toString(),
                TESTSTANDORT.toString(), TESTSTUDIENGANG.toString(), TESTKURS.toString()};
        sqlQueriesMockedStatic.when(() -> getUserData(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(user);

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String actual = writer.toString().trim();
        assertTrue(actual.contains(expectedSubstring1));
        assertTrue(actual.contains(expectedSubstring2));
        assertEquals(1, processEngine.getRuntimeService().createProcessInstanceQuery().active().list().size());
        sqlQueriesMockedStatic.verify(() ->
                SQLQueries.createInstance(anyString(), eq(TESTSTANDORT.toString()), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())), eq(2), eq(10)),
                times(1));
    }

    @Test
    void doGetNewProcessInstanceWithUnknownUser() throws IOException {
        String expectedSubstring1 = "{\"instanceId\":\"";
        String expectedSubstring2 = "\",\"uni\":\"Karlsruhe\"}";

        sqlQueriesMockedStatic.when(() -> getModel(TESTSTANDORT.toString())).thenReturn("standard");
        sqlQueriesMockedStatic.when(() ->
                        getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), TESTSTANDORT.toString()))
                .thenReturn("");
        sqlQueriesMockedStatic.when(() -> getUserData(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(new String[] {});

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String actualResponse = writer.toString().trim();
        assertTrue(actualResponse.contains(expectedSubstring1));
        assertTrue(actualResponse.contains(expectedSubstring2));
        List<ProcessInstance> processInstances = processEngine.getRuntimeService().createProcessInstanceQuery()
                .variableValueEquals("uni", TESTSTANDORT.toString())
                .list();
        assertEquals(1, processInstances.size());
        sqlQueriesMockedStatic.verify(() ->
                        SQLQueries.createInstance(anyString(), eq(TESTSTANDORT.toString()), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())), eq(2), eq(10)),
                times(1));
    }

    @Test
    void doGetNewProcessInstanceWithIncompleteUserData() throws IOException {
        String expectedSubstring1 = "{\"instanceId\":\"";
        String expectedSubstring2 = "\",\"uni\":\"Karlsruhe\"}";

        sqlQueriesMockedStatic.when(() -> getModel(TESTSTANDORT.toString())).thenReturn("standard");
        sqlQueriesMockedStatic.when(() ->
                        getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), TESTSTANDORT.toString()))
                .thenReturn("");
        String[] user = {TESTNACHNAME.toString()};
        sqlQueriesMockedStatic.when(() -> getUserData(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(user);

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String actualResponse = writer.toString().trim();
        assertTrue(actualResponse.contains(expectedSubstring1));
        assertTrue(actualResponse.contains(expectedSubstring2));
        List<ProcessInstance> processInstances = processEngine.getRuntimeService().createProcessInstanceQuery()
                .variableValueEquals("uni", TESTSTANDORT.toString())
                .list();
        assertEquals(1, processInstances.size());
        List<ProcessInstance> processInstancesWithUserData = processEngine.getRuntimeService().createProcessInstanceQuery()
                .variableValueEquals("uni", TESTSTANDORT.toString())
                .variableValueEquals("bewNachname", TESTNACHNAME.toString())
                .list();
        assertEquals(0, processInstancesWithUserData.size());
        sqlQueriesMockedStatic.verify(() ->
                        SQLQueries.createInstance(anyString(), eq(TESTSTANDORT.toString()), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())), eq(2), eq(10)),
                times(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"matnr", "uni", "prio"})
    void doGetParamMissing(String missingParam) throws IOException {
        when(request.getParameter(missingParam)).thenReturn(null);

        new GetInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }
}
package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.User;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
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
import java.util.ArrayList;
import java.util.List;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.executeUpdate;
import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.getUserInstances;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.getUserInfo;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTMATRIKELNUMMER;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class ChangePriorityServletTest {

    private ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);
        User user = new User();
        user.matrikelnummer = TESTMATRIKELNUMMER.toString();
        userAuthentificationMockedStatic.when(() -> getUserInfo(request)).thenReturn(user);

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
    }

    @AfterEach
    void tearDown() {
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
        processEngine.close();
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(0);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"instance;anyInstance", "prio;1"})
    void doGetParamMissing(String missingParam) throws IOException {
        String[] keyAndValue = missingParam.split(";");
        when(request.getParameter(keyAndValue[0])).thenReturn(keyAndValue[1]);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doGetSaveNewPriority() throws IOException {
        String instanceId = processEngine.getRuntimeService().startProcessInstanceByKey("standard").getId();
        when(request.getParameter("instance")).thenReturn(instanceId);
        when(request.getParameter("prio")).thenReturn("1");

        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(new ArrayList<String[]>());
        sqlQueriesMockedStatic.when(() -> executeUpdate(anyString(), any(), any())).thenReturn(1);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(1, processEngine.getRuntimeService().getVariable(instanceId, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"1", instanceId}, new String[] {"int", "String"}),
                times(1));
    }

    @Test
    void doGetPrioritizeLowerPriorityHigher() throws IOException {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String instanceIdPrio1 = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceIdPrio1, "prioritaet", 1);
        String instanceIdPrio2 = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceIdPrio2, "prioritaet", 2);
        String instanceIdPrio3 = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceIdPrio3, "prioritaet", 3);

        when(request.getParameter("instance")).thenReturn(instanceIdPrio3);
        when(request.getParameter("prio")).thenReturn("1");

        String[] instancePrio1 = new String[] {"USA", instanceIdPrio1, "1"};
        String[] instancePrio2 = new String[] {"Finnland", instanceIdPrio2, "2"};
        String[] instancePrio3 = new String[] {"Schottland", instanceIdPrio3, "3"};
        List<String[]> instances = new ArrayList<>();
        instances.add(instancePrio1);
        instances.add(instancePrio2);
        instances.add(instancePrio3);
        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(instances);

        sqlQueriesMockedStatic.when(() -> executeUpdate(anyString(), any(), any())).thenReturn(1);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(2, runtimeService.getVariable(instanceIdPrio2, "prioritaet"));
        assertEquals(1, runtimeService.getVariable(instanceIdPrio3, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                        executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"1", instanceIdPrio3}, new String[] {"int", "String"}),
                times(1));
        assertEquals(3, runtimeService.getVariable(instanceIdPrio1, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                        executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"3", instanceIdPrio1}, new String[] {"int", "String"}),
                times(1));
    }

    @Test
    void doGetOverwritePriority() throws IOException {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String instanceIdPrio1 = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceIdPrio1, "prioritaet", 1);
        String emptyInstanceId = runtimeService.startProcessInstanceByKey("standard").getId();

        when(request.getParameter("instance")).thenReturn(emptyInstanceId);
        when(request.getParameter("prio")).thenReturn("1");

        String[] instancePrio1 = new String[] {"USA", instanceIdPrio1, "1"};
        List<String[]> instances = new ArrayList<>();
        instances.add(instancePrio1);
        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(instances);

        sqlQueriesMockedStatic.when(() -> executeUpdate(anyString(), any(), any())).thenReturn(1);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(1, runtimeService.getVariable(emptyInstanceId, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                        executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"1", emptyInstanceId}, new String[] {"int", "String"}),
                times(1));
        assertEquals(0, runtimeService.getVariable(instanceIdPrio1, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                        executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"0", instanceIdPrio1}, new String[] {"int", "String"}),
                times(1));
    }

    @Test
    void doGetOverwritePriorityWithItself() throws IOException {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String instanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceId, "prioritaet", 1);

        when(request.getParameter("instance")).thenReturn(instanceId);
        when(request.getParameter("prio")).thenReturn("1");

        String[] instancePrio1 = new String[] {"USA", instanceId, "1"};
        List<String[]> instances = new ArrayList<>();
        instances.add(instancePrio1);
        sqlQueriesMockedStatic.when(() -> getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString())))
                .thenReturn(instances);

        sqlQueriesMockedStatic.when(() -> executeUpdate(anyString(), any(), any())).thenReturn(1);

        new ChangePriorityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(1, runtimeService.getVariable(instanceId, "prioritaet"));
        sqlQueriesMockedStatic.verify(() ->
                        executeUpdate("UPDATE MapUserInstanz SET prioritaet = ? WHERE processInstance = ?", new String[] {"1", instanceId}, new String[] {"int", "String"}),
                times(2));
    }
}
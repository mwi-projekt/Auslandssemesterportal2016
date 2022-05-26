package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
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

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTSTANDORT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetCurrentActivityServletTest {

    private ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);
    }

    @AfterEach
    void tearDown() {
        sqlQueriesMockedStatic.close();
        userAuthentificationMockedStatic.close();
    }

    @Test
    void doGetUnauthorizedUser() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(0);

        new GetCurrentActivityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"instance_id", "uni"})
    void doGetParamMissing(String queryParam) throws IOException {
        when(request.getParameter(queryParam)).thenReturn("any");

        new GetCurrentActivityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1))
                .sendError(SC_BAD_REQUEST, ErrorEnum.PARAMMISSING.toString());
    }

    @Test
    void doGetCurrentActivity() throws IOException {
        String standardModel = "standard";
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(standardModel);
        when(request.getParameter("instance_id")).thenReturn(processInstance.getId());
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());
        sqlQueriesMockedStatic.when(() -> SQLQueries.getModel(TESTSTANDORT.toString())).thenReturn(standardModel);

        new GetCurrentActivityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"active\":\"downloadsAnbieten\",\"data\":\"standard\"}";
        assertEquals(expected, writer.toString().trim());
    }

    @Test
    void doGetNoSuchInstance() throws IOException {
        when(request.getParameter("instance_id")).thenReturn("notExistingId");
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());
        sqlQueriesMockedStatic.when(() -> SQLQueries.getModel(TESTSTANDORT.toString())).thenReturn("standard");

        new GetCurrentActivityServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, "no such instanceId");
    }
}
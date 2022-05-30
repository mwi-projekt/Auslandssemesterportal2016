package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static dhbw.mwi.Auslandsemesterportal2016.db.ProcessService.getProcessId;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTMATRIKELNUMMER;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTSTANDORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class ProcessDeleteServletTest {

    public ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private StringWriter writer;
    private MockedStatic<ProcessService> processServiceMockedStatic;
    private Connection connection;
    private MockedStatic<DB> dbMockedStatic;
    private Statement statement;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        processServiceMockedStatic = mockStatic(ProcessService.class);

        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request))
                .thenReturn(1);

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        dbMockedStatic = mockStatic(DB.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);
    }

    @AfterEach
    void tearDown() throws IOException, SQLException {
        userAuthentificationMockedStatic.close();
        processServiceMockedStatic.close();
        writer.close();
        dbMockedStatic.close();
        connection.close();
        statement.close();
    }

    @Test
    void doGetUserUnauthorized() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request))
                .thenReturn(0);

        new ProcessDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401);
    }

    @Test
    void doGetParamMissing() throws IOException {
        when(request.getParameter("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());

        new ProcessDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = writer.toString().trim();
        assertEquals(ErrorEnum.PARAMMISSING.toString(), result);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void doGetProcessIdNotFound() throws IOException {
        when(request.getParameter("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());
        processServiceMockedStatic.when(() -> getProcessId(TESTMATRIKELNUMMER.toString(), TESTSTANDORT.toString()))
                .thenReturn(null);

        new ProcessDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = writer.toString().trim();
        assertEquals("Error: can not find process", result);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void doGetDeleteProcess() throws IOException, SQLException {
        when(request.getParameter("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());

        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("standard");
        ProcessEngineTests.assertThat(processInstance).isStarted();
        processServiceMockedStatic.when(() -> getProcessId(TESTMATRIKELNUMMER.toString(), TESTSTANDORT.toString()))
                .thenReturn(processInstance.getId());

        dbMockedStatic.when(DB::getInstance).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(1);

        // when
        new ProcessDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        assertEquals(processInstance.getId(), writer.toString().trim());
        String expectedStatement1 = "DELETE FROM bewerbungsprozess WHERE matrikelnummer = '" + TESTMATRIKELNUMMER
                + "' AND uniName = '" + TESTSTANDORT + "' ";
        String expectedStatement2 = "DELETE FROM MapUserInstanz WHERE matrikelnummer = '" + TESTMATRIKELNUMMER
                + "' AND uni = '" + TESTSTANDORT + "' ";
        verify(statement, times(1)).executeUpdate(expectedStatement1);
        verify(statement, times(1)).executeUpdate(expectedStatement2);
    }
}
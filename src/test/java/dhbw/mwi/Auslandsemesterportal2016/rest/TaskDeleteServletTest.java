package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.db.ProcessService.*;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.*;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static javax.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class TaskDeleteServletTest {
    
    ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private Connection connection;
    private MockedStatic<DB> dbMockedStatic;
    private MockedStatic<UserAuthentification> userAuthentification;
    private MockedStatic<ProcessService> processServiceMockedStatic;
    private MockedStatic<Util> utilMockedStatic;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());

        writer = new StringWriter();
        response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentification = mockStatic(UserAuthentification.class);
        userAuthentification.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(2);

        // TODO wird im Servlet nicht verwendet
        dbMockedStatic = mockStatic(DB.class);
        connection = mock(Connection.class);
        dbMockedStatic.when(DB::getInstance).thenReturn(connection);

        processServiceMockedStatic = mockStatic(ProcessService.class);
    }

    @AfterEach
    void tearDown() throws SQLException, IOException {
        writer.close();
        dbMockedStatic.close();
        connection.close();
        processServiceMockedStatic.close();
        userAuthentification.close();
    }

    @Test
    void doPostUserUnauthorized() throws IOException {
        userAuthentification.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        new TaskDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401, "Rolle: 1");
    }

    @Test
    void doPostParamMissing() throws IOException {
        new TaskDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String actual = writer.toString().trim();
        assertEquals("", actual);
    }

    @Test
    void doPostProcessIdNotFound() throws IOException {
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());
        processServiceMockedStatic.when(() -> getProcessId(TESTMATRIKELNUMMER.toString(), TESTSTANDORT.toString()))
                .thenReturn(null);

        new TaskDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String actual = writer.toString().trim();
        assertEquals(ErrorEnum.PARAMMISSING.toString(), actual);
    }

    @Test
    void doPostDeleteTask() {
        fail();
    }
}
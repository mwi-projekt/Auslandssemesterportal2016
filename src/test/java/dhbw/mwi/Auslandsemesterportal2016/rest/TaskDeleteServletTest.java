package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.ProcessService;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
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

import static dhbw.mwi.Auslandsemesterportal2016.db.ProcessService.getProcessId;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTMATRIKELNUMMER;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTSTANDORT;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());

        writer = new StringWriter();
        response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentification = mockStatic(UserAuthentification.class);
        userAuthentification.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(2);

        processServiceMockedStatic = mockStatic(ProcessService.class);
    }

    @AfterEach
    void tearDown() throws SQLException, IOException {
        writer.close();
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

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
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

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(PARAMMISSING.toString(), writer.toString().trim());
    }

    @Test
    void doPostDeleteTask() throws IOException {
        when(request.getParameter("uni")).thenReturn(TESTSTANDORT.toString());

        String id = processEngine.getRuntimeService().startProcessInstanceByKey("standard").getId();
        processServiceMockedStatic.when(() -> getProcessId(TESTMATRIKELNUMMER.toString(), TESTSTANDORT.toString()))
                .thenReturn(id);

        new TaskDeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("", writer.toString().trim());
        verify(response, times(0)).sendError(anyInt());
        verify(response, times(0)).sendError(anyInt(), anyString());
    }
}
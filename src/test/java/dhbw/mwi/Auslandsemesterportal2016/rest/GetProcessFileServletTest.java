package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.*;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetProcessFileServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentification;
    private ServletOutputStream outputStream;
    private ProcessEngine processEngine;
    private CamundaHelper camundaHelper;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("key")).thenReturn("daadFormularEnglisch");

        response = mock(HttpServletResponse.class);
        outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        userAuthentification = mockStatic(UserAuthentification.class);
        userAuthentification.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        camundaHelper = new CamundaHelper(processEngine);
    }

    @AfterEach
    void tearDown() {
        userAuthentification.close();
        processEngine.close();
    }

    @Test
    void doHeadParamMissing() throws IOException {
        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doHead(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doHeadFileExists() throws IOException {
        String instanceId = camundaHelper.startProcess("standard");
        camundaHelper.processHasDualisDocument(instanceId);

        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("key")).thenReturn("dualisHochladen");

        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doHead(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_OK);
    }

    @Test
    void doHeadFileNotFound() throws IOException {
        String instanceId = camundaHelper.startProcess("standard");
        when(request.getParameter("instance_id")).thenReturn(instanceId);

        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doHead(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_NOT_FOUND);
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentification.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(0);

        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doGetParamMissing() throws IOException {
        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doGetFileNotFound() throws IOException {
        String instanceId = camundaHelper.startProcess("standard");
        when(request.getParameter("instance_id")).thenReturn(instanceId);

        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_NOT_FOUND);
    }

    @Test
    void doGetFile() throws IOException {
        String instanceId = camundaHelper.startProcess("standard");
        camundaHelper.processHasDualisDocument(instanceId);

        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("key")).thenReturn("dualisHochladen");

        new GetProcessFileServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setContentType(null);

        verify(outputStream, times(14)).write(any(), eq(0), anyInt());
    }
}
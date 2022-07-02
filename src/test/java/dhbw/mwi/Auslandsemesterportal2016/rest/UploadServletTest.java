package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static java.util.Collections.singletonList;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class UploadServletTest {

    ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Writer writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private Part part;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        part = mock(Part.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        userAuthentificationMockedStatic.close();
        processEngine.close();
    }

    @Test
    void doPostUnauthorizedUser() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(0);

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doPostInstanceIdIsLeer() throws IOException {
        when(request.getParameter("action")).thenReturn("anyAction");
        when(request.getParameter("instance")).thenReturn("leer");

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("Error: can not find process id", writer.toString());
    }

    @Test
    void doPostActionParamMissingOrEmpty() throws IOException {
        when(request.getParameter("action")).thenReturn("");
        when(request.getParameter("instance")).thenReturn("anyInstanceId");

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("Error: wrong action", writer.toString());
    }

    @Test
    void doPostFileMissing() throws IOException, ServletException {
        when(request.getParameter("action")).thenReturn("anyAction");
        when(request.getParameter("instance")).thenReturn("anyInstanceId");
        when(request.getPart("file")).thenReturn(null);

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("Error: wrong file - File is null", writer.toString());
        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void doPostSaveFileSuccessfully() throws ServletException, IOException {
        CamundaHelper camundaHelper = new CamundaHelper(processEngine);
        String instanceId = camundaHelper.startProcess("standard");
        when(request.getParameter("instance")).thenReturn(instanceId);
        when(request.getParameter("action")).thenReturn("anyAction");
        when(request.getPart("file")).thenReturn(part);

        // in dem neuen File steht "dummy text"
        File fileWithInput = new File("abc.txt");
        Files.write(fileWithInput.toPath(), singletonList("dummy text"));
        when(part.getInputStream()).thenReturn(Files.newInputStream(fileWithInput.toPath()));

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        InputStream actualVariableValue = (ByteArrayInputStream) processEngine.getRuntimeService().getVariable(instanceId, "anyAction");
        assertEquals("[dummy text]", IOUtils.readLines(actualVariableValue, StandardCharsets.UTF_8).toString());
        assertEquals("jop", writer.toString());
        verify(response, times(0)).setStatus(anyInt());
        verify(response, times(0)).sendError(anyInt());
    }

    @Test
    void doPostExceptionWhileWritingFileToDb() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("anyAction");
        when(request.getParameter("instance")).thenReturn("anyInstanceId");
        when(request.getPart("file")).thenThrow(new RuntimeException());

        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("Error: wrong file - Exception", writer.toString());
        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void doOptionsReturnsEmptyJson() {
        new UploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) {
                doOptions(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("{}", writer.toString());
    }
}
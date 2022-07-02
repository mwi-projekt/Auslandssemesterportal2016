package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.UpdateInstanceServlet;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum.UPDATEINSTANCE;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class UpdateInstanceServletTest {

    private ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;

    @BeforeEach
    public void init() throws SQLException, IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(3);
    }

    @AfterEach
    public void close() throws IOException {
        userAuthentificationMockedStatic.close();
        writer.close();
        processEngine.close();
    }

    @Test
    void doPost() throws IOException, ServletException {
        String instanceId = processEngine.getRuntimeService().startProcessInstanceByKey("standard").getId();
        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("value")).thenReturn(TestEnum.TESTVALSTRING.toString());
        when(request.getParameter("type")).thenReturn(TestEnum.TESTTYPESTRING.toString());
        when(request.getParameter("key")).thenReturn(TestEnum.TESTKEYSTRING.toString());

        new UpdateInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("universitaetAuswaehlen",
                processEngine.getRuntimeService().getActiveActivityIds(instanceId).get(0));
        assertEquals(UPDATEINSTANCE.toString(), writer.toString().trim());
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        int rolle = 0;
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(rolle);

        new UpdateInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doPostKeyOrValueNull() throws IOException {
        new UpdateInstanceServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = writer.toString().trim();
        assertEquals("Variables not set", result);
        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
    }
}

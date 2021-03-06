package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.UpdateInstanceServlet;

public class UpdateInstanceServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    Task task = mock(Task.class);
    ResultSet resultSet = mock(ResultSet.class);
    TaskQuery taskQuery = mock(TaskQuery.class);
    TaskService taskService = mock(TaskService.class);
    ProcessEngine processEngine = mock(ProcessEngine.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<SQL_queries> sql_queries;
    MockedStatic<ProcessEngines> processEngines;
    MockedStatic<ProcessEngineServices> processEngineServices;

    // Initialization of necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", TestEnum.TESTEMAIL.toString());
    Cookie c2 = new Cookie("sessionID", TestEnum.TESTSESSIONID.toString());
    Cookie[] cookies = { c1, c2 };

    @BeforeMethod
    public void init() throws SQLException, IOException {
        // Define necessary mock objects for mocking static methods
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        processEngines = Mockito.mockStatic(ProcessEngines.class);
        processEngineServices = Mockito.mockStatic(ProcessEngineServices.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();

        processEngines.when(() -> ProcessEngines.getDefaultProcessEngine()).thenReturn(processEngine);

        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.processInstanceId(TestEnum.TESTINSTANCEID.toString())).thenReturn(taskQuery);
        when(taskQuery.singleResult()).thenReturn(task);

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("instance_id")).thenReturn(TestEnum.TESTINSTANCEID.toString());
        when(request.getParameter("key")).thenReturn(TestEnum.TESTKEYSTRING.toString());
        when(request.getParameter("value")).thenReturn(TestEnum.TESTVALSTRING.toString());
        when(request.getParameter("type")).thenReturn(TestEnum.TESTTYPESTRING.toString());

        // TODO: für jede Rolle?
        when(resultSet.getInt(anyInt())).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();
        processEngines.close();
        processEngineServices.close();

        // Close instances
        writer.close();
    }

    @Test
    public void testDoPost() throws IOException, ServletException {

        new UpdateInstanceServlet() {
            public UpdateInstanceServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEINSTANCE.toString(), result);
    }
}

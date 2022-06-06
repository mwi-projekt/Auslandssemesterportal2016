package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetOverviewServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private StringWriter stringWriter;
    private String processDefinitionKey;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);

        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        userAuthentificationMockedStatic.when(() ->
                UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(1);

        processDefinitionKey = "anyProcessDefinitionKey";
        when(request.getParameter("definition")).thenReturn(processDefinitionKey);
    }

    @AfterEach
    void tearDown() throws IOException {
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
        stringWriter.close();
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() ->
                UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(0);

        new GetOverviewServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401);
    }

    @Test
    void doGetNoActivitesExisting() throws IOException {
        sqlQueriesMockedStatic.when(() ->SQLQueries.getAllActivities(processDefinitionKey)).thenReturn("");

        new GetOverviewServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doGetOverview() throws IOException, SQLException {
        String expected = "{\"data\":[{\"activity\":\"1\",\"data\":\"anyData\"},{\"activity\":\"2\",\"data\":\"anyData\"},{\"activity\":\"3\",\"data\":\"anyData\"}]}";

        sqlQueriesMockedStatic.when(() -> SQLQueries.getAllActivities(processDefinitionKey)).thenReturn("1;2;3;");

        ResultSet resultSet = mock(ResultSet.class);
        sqlQueriesMockedStatic.when(() -> SQLQueries.getProcessModelJson(any(), eq(processDefinitionKey)))
                .thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("json")).thenReturn("anyData");

        new GetOverviewServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = stringWriter.toString().trim();
        assertEquals(expected, result);
    }
}
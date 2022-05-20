package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.AAADeleteServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AAADeleteServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<SQLQueries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", TestEnum.TESTEMAIL.toString());
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };

    @BeforeEach
    public void init() throws SQLException, IOException {
        // Define necessary mock objects for mocking static methods
        sql_queries = Mockito.mockStatic(SQLQueries.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQLQueries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.executeStatement(any(), any(), any())).thenReturn(resultSet);

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    void doPost() throws SQLException, IOException {
        // Define what happens when mocked method is called
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());
        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        new AAADeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.USERDELETE.toString(), result);
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        int rolle = 2;
        MockedStatic<UserAuthentification> userAuthentificationMock = Mockito.mockStatic(UserAuthentification.class);
        userAuthentificationMock.when(() -> UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(rolle);

        new AAADeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401, "Rolle: " + rolle);

        userAuthentificationMock.close();
    }

    @Test
    void doPostWithoutMail() throws IOException {
        // Define what happens when mocked method is called
        when(request.getParameter("mail")).thenReturn(null);
        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        new AAADeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.PARAMMISSING.toString(), result);
    }

    @Test
    void doPostWithoutResult() throws IOException {
        // Define what happens when mocked method is called
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());
        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(0);

        // call protected doPost-Method of AAADeleteServlet.class
        new AAADeleteServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.USERNOTDELETED.toString(), result);
    }
}

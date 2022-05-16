package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.SGLDeleteServlet;
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

class SGLDeleteServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    ResultSet resultSet = mock(ResultSet.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<SQL_queries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", TestEnum.TESTEMAIL.toString());
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };

    @BeforeEach
    public void init() throws SQLException, IOException {
        // Define necessary mock objects for mocking static methods
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);

        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    void doPostForRoleAdmin() throws IOException {
        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());

        // call protected doPost()-Method of RegisterServlet.class
        new SGLDeleteServlet() {
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

        new SGLDeleteServlet() {
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
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(request.getParameter("mail")).thenReturn(null);

        // call protected doPost()-Method of RegisterServlet.class
        new SGLDeleteServlet() {
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
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());

        // call protected doPost()-Method of RegisterServlet.class
        new SGLDeleteServlet() {
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

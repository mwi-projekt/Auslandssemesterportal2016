package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.UserUpdateServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.mail.Transport;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserUpdateServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    ResultSet resultSet = mock(ResultSet.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Transport> transport;
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
        transport = Mockito.mockStatic(Transport.class);
        sql_queries = Mockito.mockStatic(SQLQueries.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQLQueries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQLQueries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateMA(any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateMA(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateStud(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateStud(any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateUser(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.updateUser(any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("vorname")).thenReturn(TestEnum.TESTVORNAME.toString());
        when(request.getParameter("nachname")).thenReturn(TestEnum.TESTNACHNAME.toString());
        when(request.getParameter("standort")).thenReturn(TestEnum.TESTSTANDORT.toString());
        when(request.getParameter("studgang")).thenReturn(TestEnum.TESTSTUDIENGANG.toString());
        when(request.getParameter("kurs")).thenReturn(TestEnum.TESTKURS.toString());

        when(resultSet.next()).thenReturn(true);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        transport.close();
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        int rolle = 2;
        MockedStatic<UserAuthentification> userAuthentificationMock = Mockito.mockStatic(UserAuthentification.class);
        userAuthentificationMock.when(() -> UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(rolle);

        new UserUpdateServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(401, "Rolle: " + rolle);

        userAuthentificationMock.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", "4", "6"})
    void doPostWithOldmail0(String role) throws SQLException, IOException {
        String oldmail = "0";

        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", "4", "6"})
    void doPostWithOldmail2(String role) throws SQLException, IOException {
        String oldmail = "2";

        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    /*
     * Test doPost()-Method for rolle=3 (Student)
     */
    @Test
    void doPostForStudent() throws IOException, SQLException {
        // Define what happens when mocked method is called
        when(resultSet.getInt(1)).thenReturn(3);

        Mockito.doAnswer((Answer<Object>) invocation -> {
            writer.print("Error 401 - Rolle: Student");
            return null;
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Error 401 - Rolle: Student", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "2"})
    void doPostUpdateUserFails(String oldMail) throws SQLException, IOException {
        String role = "6";

        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(0);
        when(resultSet.getInt(1)).thenReturn(2);
        when(request.getParameter("oldmail")).thenReturn(oldMail);
        when(request.getParameter("role")).thenReturn(role);

        Mockito.doAnswer((Answer<Object>) invocation -> {
            writer.print(ErrorEnum.USERUPDATE);
            return null;
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.USERUPDATE.toString(), result);
    }
}
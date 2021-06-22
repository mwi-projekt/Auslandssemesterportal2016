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

import javax.mail.Transport;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.UserUpdateServlet;

public class UserUpdateServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    ResultSet resultSet = mock(ResultSet.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Transport> transport;
    MockedStatic<SQL_queries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", TestEnum.TESTEMAIL.toString());
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };

    @BeforeMethod
    public void init() throws SQLException, IOException {
        // Define necessary mock objects for mocking static methods
        transport = Mockito.mockStatic(Transport.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateMA(any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateMA(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateStud(any(), any(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateStud(any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateUser(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.updateUser(any(), any(), any(), any(), any(), any(), any()))
                .thenCallRealMethod();

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("vorname")).thenReturn(TestEnum.TESTVNAME.toString());
        when(request.getParameter("nachname")).thenReturn(TestEnum.TESTNNAME.toString());
        when(request.getParameter("standort")).thenReturn(TestEnum.TESTSTANDORT.toString());
        when(request.getParameter("studgang")).thenReturn(TestEnum.TESTSTUGANG.toString());
        when(request.getParameter("kurs")).thenReturn(TestEnum.TESTKURS.toString());

        when(resultSet.next()).thenReturn(true);
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        transport.close();
        sql_queries.close();

        // Close instances
        writer.close();
    }

    /*
     * Test doPost()-Method for rolle=1 (Admin)
     */
    @Test
    public void testDoPostForAdminWithOldmail0AndRole2() throws IOException, SQLException {
        // set up request data
        String oldmail = "0";
        String role = "2";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @Test
    public void testDoPostForAdminWithOldmail0AndRole4() throws IOException, SQLException {
        // set up request data
        String oldmail = "0";
        String role = "4";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @Test
    public void testDoPostForAdminWithOldmail0AndRole6() throws IOException, SQLException {
        // set up request data
        String oldmail = "0";
        String role = "6";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole2() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "2";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole4() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "4";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.UPDATEUSER.toString(), result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole6() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "6";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
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
    public void testDoPostForStudent() throws IOException, SQLException {
        // Define what happens when mocked method is called
        when(resultSet.getInt(1)).thenReturn(3);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Error 401 - Rolle: Student");
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Error 401 - Rolle: Student", result);
    }

    @Test
    public void testDoPostWithOldmail0AndUpdateError() throws SQLException, IOException {
        // set up request data
        String oldmail = "0";
        String role = "6";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);
        when(resultSet.getInt(1)).thenReturn(2);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print(ErrorEnum.USERUPDATE.toString());
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.USERUPDATE.toString(), result);
    }

    @Test
    public void testDoPostWithOldmail2AndUpdateError() throws SQLException, IOException {
        // set up request data
        String oldmail = "2";
        String role = "6";

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);
        when(resultSet.getInt(1)).thenReturn(2);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print(ErrorEnum.USERUPDATE.toString());
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.USERUPDATE.toString(), result);
    }
}
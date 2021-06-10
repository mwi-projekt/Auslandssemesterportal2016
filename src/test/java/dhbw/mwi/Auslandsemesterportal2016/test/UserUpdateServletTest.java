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
import dhbw.mwi.Auslandsemesterportal2016.rest.UserUpdateServlet;

public class UserUpdateServletTest {
    // initalize all necessary mocks
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);

    // initialize all necessary static mocks
    MockedStatic<SQL_queries> sql_queries;
    MockedStatic<Transport> transport;

    // set up request data
    String email = "testusermwi@dhbw.de";
    String vorname = "Test";
    String nachname = "Test";
    String studgang = "Wirtschaftsinformatik";
    String kurs = "WWI18B5";
    String standort = "Karlsruhe";
    String tel = "1234567890";
    String mobil = "0987654321";
    String matnr = "1234567";

    // initialize all necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", email);
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };

    @BeforeMethod
    public void init() throws SQLException, IOException {
        // define all necessary static mock instances
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        transport = Mockito.mockStatic(Transport.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();

        when(resultSet.next()).thenReturn(true);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("vorname")).thenReturn(vorname);
        when(request.getParameter("nachname")).thenReturn(nachname);
        when(request.getParameter("standort")).thenReturn(standort);
        when(request.getParameter("studgang")).thenReturn(studgang);
        when(request.getParameter("kurs")).thenReturn(kurs);

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
    }

    @AfterMethod
    public void close() {
        // close all static mocks
        sql_queries.close();
        transport.close();
        // close writer instance
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

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("success", result);
    }

    @Test
    public void testDoPostForAdminWithOldmail0AndRole4() throws IOException, SQLException {
        // set up request data
        String oldmail = "0";
        String role = "4";

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet userUpdateServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("success", result);
    }

    @Test
    public void testDoPostForAdminWithOldmail0AndRole6() throws IOException, SQLException {
        // set up request data
        String oldmail = "0";
        String role = "6";

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("success", result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole2() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "2";

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Success", result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole4() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "4";

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Success", result);
    }

    @Test
    public void testDoPostForAdminWithOldmail2AndRole6() throws IOException, SQLException {
        // set up request data
        String oldmail = "2";
        String role = "6";

        when(resultSet.getInt(1)).thenReturn(1);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Success", result);
    }

    /*
     * Test doPost()-Method for rolle=3 (Student)
     */
    @Test
    public void testDoPostForStudent() throws IOException, SQLException {
        when(resultSet.getInt(1)).thenReturn(3);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Error 401 - Rolle: Student");
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
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

        when(resultSet.getInt(1)).thenReturn(2);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Update Error");
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Update Error", result);
    }

    @Test
    public void testDoPostWithOldmail2AndUpdateError() throws SQLException, IOException {
        // set up request data
        String oldmail = "2";
        String role = "6";

        when(resultSet.getInt(1)).thenReturn(2);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("role")).thenReturn(role);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);

        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Update Error");
                return null;
            }
        }).when(response).sendError(anyInt(), any());

        // call protected doPost()-Method of RegisterServlet.class
        UserUpdateServlet registerServlet = new UserUpdateServlet() {
            public UserUpdateServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Update Error", result);
    }
}
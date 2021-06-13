/*package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.rest.UserUpdateServlet;

public class UserUpdateServletTest {
    // initalize all necessary mocks
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);

    // initialize all necessary static mocks
    MockedStatic<Util> util;
    MockedStatic<SQL_queries> sql_queries;

    // set up request data
    String email = "testusermwi@dhbw.de";
    String oldmail = "0";
    String vorname = "Test";
    String nachname = "Test";
    String role = "4";
    String studiengang = "Wirtschaftsinformatik";
    String kurs = "WWI18B5";
    String standort = "Karlsruhe";

    // initialize all necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Cookie c1 = new Cookie("email", email);
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };

    @BeforeMethod
    public void init() throws SQLException, IOException {
        // define all necessary static mock instances
        util = Mockito.mockStatic(Util.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        when(resultSet.next()).thenReturn(true);
        // TODO: check with rolle 3=Student --> error!
        when(resultSet.getInt(1)).thenReturn(2);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("oldmail")).thenReturn(oldmail);
        when(request.getParameter("vorname")).thenReturn(vorname);
        when(request.getParameter("nachname")).thenReturn(nachname);
        when(request.getParameter("role")).thenReturn(role);
        when(request.getParameter("standort")).thenReturn(standort);
        when(request.getParameter("studgang")).thenReturn(studiengang);
        when(request.getParameter("kurs")).thenReturn(kurs);

        sql_queries.when(() -> SQL_queries.updateStud(any(), any(), any(), any(), any(), any())).thenReturn(1);

        when(response.getWriter()).thenReturn(writer);
    }

    @AfterMethod
    public void close() {
        // close all static mocks
        util.close();
        sql_queries.close();
    }

    @Test
    public void testDoPost() throws IOException {
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

}
*/
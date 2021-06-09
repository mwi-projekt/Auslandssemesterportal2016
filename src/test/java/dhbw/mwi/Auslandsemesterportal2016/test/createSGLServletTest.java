package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
import dhbw.mwi.Auslandsemesterportal2016.rest.createSGLServlet;

public class createSGLServletTest {
    // initalize all necessary mocks
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    // initialize all necessary static mocks
    MockedStatic<SQL_queries> sql_queries;

    // set up request data
    String email = "testusermwi@dhbw.de";
    String vorname = "Test";
    String nachname = "SGL";
    String studgang = "Wirtschaftsinformatik";
    String kurs = "WWI15B2";
    String standort = "Karlsruhe";

    // initialize all necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", email);
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };
    createSGLServlet sglServlet = new createSGLServlet();

    @BeforeMethod
    public void init() throws IOException, SQLException {
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.isEmailUsed(any())).thenReturn(false);
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        when(resultSet.next()).thenReturn(true);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("vorname")).thenReturn(vorname);
        when(request.getParameter("nachname")).thenReturn(nachname);
        when(request.getParameter("studgang")).thenReturn(studgang);
        when(request.getParameter("kurs")).thenReturn(kurs);
        when(request.getParameter("standort")).thenReturn(standort);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @AfterMethod
    public void close() {
        sql_queries.close();
        writer.close();
    }

    @Test
    public void testDoPostForRoleAdmin() throws SQLException, ServletException, IOException {
        // 1 = Admin, 2 = Mitarbeiter, 3 = Student (SQL_queries.getRoleForUser)
        when(resultSet.getInt(anyInt())).thenReturn(1);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Create SGL successfully");
                return null;
            }
        }).when(requestDispatcher).forward(any(), any());

        sglServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Create SGL successfully", result);
    }

    @Test
    public void testDoPostForRoleMitarbeiter() throws SQLException, ServletException, IOException {
        // 1 = Admin, 2 = Mitarbeiter, 3 = Student (SQL_queries.getRoleForUser)
        when(resultSet.getInt(anyInt())).thenReturn(2);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Create SGL successfully");
                return null;
            }
        }).when(requestDispatcher).forward(any(), any());

        sglServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Create SGL successfully", result);
    }

    @Test
    public void testDoPostForRoleStudent() throws SQLException, IOException {
        // 1 = Admin, 2 = Mitarbeiter, 3 = Student (SQL_queries.getRoleForUser)
        when(resultSet.getInt(anyInt())).thenReturn(3);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                writer.print("Error 401: Rolle 3 - Student - not allowed to create SGL");
                return null;
            }
        }).when(response).sendError(anyInt(), anyString());

        sglServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        System.out.println(result);
        assertEquals("Error 401: Rolle 3 - Student - not allowed to create SGL", result);
    }
}
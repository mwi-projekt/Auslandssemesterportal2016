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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.rest.createAAAServlet;

public class createAAAServletTest {
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
    String nachname = "Test";
    String tel = "0987654321";
    String mobil = "1234567890";

    // initialize all necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Cookie c1 = new Cookie("email", email);
    Cookie c2 = new Cookie("sessionID", "s1e5f2ge8gvs694g8vedsg");
    Cookie[] cookies = { c1, c2 };
    createAAAServlet aaaServlet = new createAAAServlet();

    @BeforeMethod
    public void init() throws IOException, SQLException {
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        when(response.getWriter()).thenReturn(writer);
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        sql_queries.when(() -> SQL_queries.isEmailUsed(any())).thenReturn(false);
        // ERROR
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("vorname")).thenReturn(vorname);
        when(request.getParameter("nachname")).thenReturn(nachname);
        when(request.getParameter("tel")).thenReturn(tel);
        when(request.getParameter("mobil")).thenReturn(mobil);
        when(request.getRequestDispatcher("resetPassword")).thenReturn(requestDispatcher);
        // doNothing().when(requestDispatcher.forward(request, response));
        // 1 = Admin, 2 = Mitarbeiter, 3 = Student (SQL_queries.getRoleForUser)
        when(resultSet.getInt(anyInt())).thenReturn(1);

    }

    @AfterMethod
    public void close() {
        sql_queries.close();
    }

    @Test
    public void testDoPostForAdmin() throws SQLException, IOException {

        aaaServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("registerSuccessful", result);
    }

}
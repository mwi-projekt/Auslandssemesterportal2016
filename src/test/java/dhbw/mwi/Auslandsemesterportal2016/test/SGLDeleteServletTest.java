package dhbw.mwi.Auslandsemesterportal2016.test;

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
import dhbw.mwi.Auslandsemesterportal2016.rest.SGLDeleteServlet;

public class SGLDeleteServletTest {
    // initalize all necessary mocks
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);

    // initialize all necessary static mocks
    MockedStatic<SQL_queries> sql_queries;

    // set up request data
    String email = "testusermwi@dhbw.de";

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
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(request.getCookies()).thenReturn(cookies);
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);
    }

    @AfterMethod
    public void close() {
        sql_queries.close();
        writer.close();
    }

    @Test
    public void testDoPostForRoleAdmin() throws SQLException, IOException {
        when(request.getParameter("mail")).thenReturn(email);
        when(resultSet.getInt(1)).thenReturn(1);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        SGLDeleteServlet sglDeleteServlet = new SGLDeleteServlet() {
            public SGLDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("User Deleted", result);
    }

    @Test
    public void testDoPostForRoleAAA() throws SQLException, IOException {
        when(request.getParameter("mail")).thenReturn(email);
        when(resultSet.getInt(1)).thenReturn(2);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        SGLDeleteServlet sglDeleteServlet = new SGLDeleteServlet() {
            public SGLDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("User Deleted", result);
    }

    @Test
    public void testDoPostForRoleStudent() throws SQLException, IOException {
        when(request.getParameter("mail")).thenReturn(email);
        when(resultSet.getInt(1)).thenReturn(3);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        SGLDeleteServlet sglDeleteServlet = new SGLDeleteServlet() {
            public SGLDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);
        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("User Deleted", result);
    }

    @Test
    public void testDoPostWithoutMail() throws SQLException, IOException {
        when(request.getParameter("mail")).thenReturn(null);
        when(resultSet.getInt(1)).thenReturn(1);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        // call protected doPost()-Method of RegisterServlet.class
        SGLDeleteServlet sglDeleteServlet = new SGLDeleteServlet() {
            public SGLDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("Error: parameter are missing", result);
    }

    @Test
    public void testDoPostWithoutResult() throws SQLException, IOException {
        when(request.getParameter("mail")).thenReturn(email);
        when(resultSet.getInt(1)).thenReturn(1);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);

        // call protected doPost()-Method of RegisterServlet.class
        SGLDeleteServlet sglDeleteServlet = new SGLDeleteServlet() {
            public SGLDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("User not found or could not be deleted", result);
    }

}

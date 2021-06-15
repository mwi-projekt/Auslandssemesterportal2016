package dhbw.mwi.Auslandsemesterportal2016.test;

import dhbw.mwi.Auslandsemesterportal2016.enums.*;
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

    @BeforeMethod
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

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    public void testDoPostForRoleAdmin() throws SQLException, IOException {
        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());

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
        assertEquals(SuccessEnum.USERDELETE.toString(), result);
    }

    @Test
    public void testDoPostWithoutMail() throws SQLException, IOException {
        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);
        when(request.getParameter("mail")).thenReturn(null);

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
        assertEquals(ErrorEnum.PARAMMISSING.toString(), result);
    }

    @Test
    public void testDoPostWithoutResult() throws SQLException, IOException {
        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(0);
        when(request.getParameter("mail")).thenReturn(TestEnum.TESTEMAIL.toString());

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
        assertEquals(ErrorEnum.USERNOTDELETED.toString(), result);
    }

}

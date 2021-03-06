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

import org.testng.annotations.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.UserDeleteServlet;

public class UserDeleteServletTest {
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
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("matrikelnummer")).thenReturn(TestEnum.TESTMATRNR.toString());
        when(request.getCookies()).thenReturn(cookies);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    public void testDoGetForRoleAdmin() throws SQLException, IOException {
        // call protected doPost()-Method of RegisterServlet.class
        new UserDeleteServlet() {
            public UserDeleteServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.USERDELETE.toString(), result);
    }

}

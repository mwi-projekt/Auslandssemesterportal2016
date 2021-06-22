package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.mail.Message;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.ResetPasswordServlet;

public class ResetPasswordServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    Message message = mock(Message.class);
    Connection connection = mock(Connection.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<DB> db;
    MockedStatic<Util> util;
    MockedStatic<Transport> transport;
    MockedStatic<SQL_queries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    @BeforeMethod
    public void init() throws IOException {
        // Define necessary mock objects for mocking static methods
        db = Mockito.mockStatic(DB.class);
        util = Mockito.mockStatic(Util.class);
        transport = Mockito.mockStatic(Transport.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.isEmailUsed(any())).thenReturn(true);
        sql_queries.when(() -> SQL_queries.forgetPassword(any())).thenCallRealMethod();

        util.when(() -> Util.getEmailMessage(any(), any())).thenReturn(message);

        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(response.getWriter()).thenReturn(writer);
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        db.close();
        util.close();
        transport.close();
        sql_queries.close();
    }

    @Test
    public void testDoPost() throws IOException {
        // call protected doPost()-Method of RegisterServlet.class
        new ResetPasswordServlet() {
            public ResetPasswordServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.RESETACC.toString() + TestEnum.TESTEMAIL.toString(), result);
    }

}
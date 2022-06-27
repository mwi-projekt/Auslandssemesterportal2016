package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.ResetPasswordServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.mail.Message;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResetPasswordServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    Message message = mock(Message.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<DB> db;
    MockedStatic<Util> util;
    MockedStatic<Transport> transport;
    MockedStatic<SQLQueries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    @BeforeEach
    public void init() throws IOException {
        // Define necessary mock objects for mocking static methods
        db = Mockito.mockStatic(DB.class);
        util = Mockito.mockStatic(Util.class);
        transport = Mockito.mockStatic(Transport.class);
        sql_queries = Mockito.mockStatic(SQLQueries.class);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQLQueries.forgetPassword(any())).thenCallRealMethod();

        util.when(() -> Util.getEmailMessage(any(), any())).thenReturn(message);

        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(response.getWriter()).thenReturn(writer);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        db.close();
        util.close();
        transport.close();
        sql_queries.close();
    }

    @Test
    void doPost() throws IOException {
        sql_queries.when(() -> SQLQueries.isEmailUsed(any())).thenReturn(true);
        // call protected doPost()-Method of RegisterServlet.class
        new ResetPasswordServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.RESETACC + TestEnum.TESTEMAIL.toString(), result);
    }

    @Test
    void doPostEmailUnused() {
        sql_queries.when(() -> SQLQueries.isEmailUsed(any())).thenReturn(false);

        new ResetPasswordServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) {
                assertThrows(RuntimeException.class, () ->doPost(request, response));
            }
        }.callProtectedMethod(request, response);

        String result = stringWriter.toString().trim();
        assertEquals("No account registered for this email adress", result);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
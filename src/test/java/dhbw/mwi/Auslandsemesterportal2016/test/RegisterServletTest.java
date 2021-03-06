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

import javax.mail.Message;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.RegisterServlet;

public class RegisterServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    Message message = mock(Message.class);
    ResultSet resultSet = mock(ResultSet.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Util> util;
    MockedStatic<SQL_queries> sql_queries;
    MockedStatic<Transport> transport;

    // Initialization of necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    @BeforeMethod
    public void init() throws IOException {
        // Define necessary mock objects for mocking static methods
        util = Mockito.mockStatic(Util.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        transport = Mockito.mockStatic(Transport.class);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.isEmailUsed(TestEnum.TESTEMAIL.toString())).thenReturn(false);
        sql_queries.when(() -> SQL_queries.isMatnrUsed(Integer.parseInt(TestEnum.TESTMATRNR.toString())))
                .thenReturn(false);
        sql_queries.when(() -> SQL_queries.userRegister(any(), any(), any(), any(), anyInt(), any(), any(), any(),
                anyInt(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        util.when(() -> Util.getEmailMessage(any(), any())).thenReturn(message);
        util.when(() -> Util.generateSalt()).thenCallRealMethod();
        util.when(() -> Util.HashSha256(any())).thenCallRealMethod();

        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("rolle")).thenReturn(TestEnum.TESTROLLESTRING.toString());
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("passwort")).thenReturn(TestEnum.TESTPW.toString());
        when(request.getParameter("vorname")).thenReturn(TestEnum.TESTVNAME.toString());
        when(request.getParameter("nachname")).thenReturn(TestEnum.TESTNNAME.toString());
        when(request.getParameter("studgang")).thenReturn(TestEnum.TESTSTUGANG.toString());
        when(request.getParameter("kurs")).thenReturn(TestEnum.TESTKURS.toString());
        when(request.getParameter("matnr")).thenReturn(TestEnum.TESTMATRNR.toString());
        when(request.getParameter("standort")).thenReturn(TestEnum.TESTSTANDORT.toString());
        when(request.getParameter("matrikelnummer")).thenReturn(TestEnum.TESTMATRNR.toString());
        when(request.getParameter("tel")).thenReturn(TestEnum.TESTTELNR.toString());
        when(request.getParameter("mobil")).thenReturn(TestEnum.TESTMOBILNR.toString());
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        util.close();
        sql_queries.close();
        transport.close();
    }

    @Test
    public void testDoPost() throws IOException {
        // call protected doPost()-Method of RegisterServlet.class
        new RegisterServlet() {
            public RegisterServlet callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("1", result);
    }
}

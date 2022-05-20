package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.RegisterServlet;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    Message message = mock(Message.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Util> util;
    MockedStatic<SQLQueries> sql_queries;
    MockedStatic<Transport> transport;

    // Initialization of necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    @BeforeEach
    public void init() throws IOException {
        // Define necessary mock objects for mocking static methods
        util = Mockito.mockStatic(Util.class);
        sql_queries = Mockito.mockStatic(SQLQueries.class);
        transport = Mockito.mockStatic(Transport.class);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQLQueries.userRegister(any(), any(), any(), any(), anyInt(), any(), any(), any(),
                anyInt(), any(), any(), any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQLQueries.executeUpdate(any(), any(), any())).thenReturn(1);

        util.when(() -> Util.getEmailMessage(any(), any())).thenReturn(message);
        util.when(Util::generateSalt).thenCallRealMethod();
        util.when(() -> Util.hashSha256(any())).thenCallRealMethod();

        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("rolle")).thenReturn(TestEnum.TESTROLLESTRING.toString());
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("passwort")).thenReturn(TestEnum.TESTPASSWORT.toString());
        when(request.getParameter("vorname")).thenReturn(TestEnum.TESTVORNAME.toString());
        when(request.getParameter("nachname")).thenReturn(TestEnum.TESTNACHNAME.toString());
        when(request.getParameter("studgang")).thenReturn(TestEnum.TESTSTUDIENGANG.toString());
        when(request.getParameter("kurs")).thenReturn(TestEnum.TESTKURS.toString());
        when(request.getParameter("matnr")).thenReturn(TestEnum.TESTMATRIKELNUMMER.toString());
        when(request.getParameter("standort")).thenReturn(TestEnum.TESTSTANDORT.toString());
        when(request.getParameter("matrikelnummer")).thenReturn(TestEnum.TESTMATRIKELNUMMER.toString());
        when(request.getParameter("tel")).thenReturn(TestEnum.TESTTELNR.toString());
        when(request.getParameter("mobil")).thenReturn(TestEnum.TESTMOBILNR.toString());
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        util.close();
        sql_queries.close();
        transport.close();
    }

    @Test
    void doPost() throws IOException {
        sql_queries.when(() -> SQLQueries.isEmailUsed(TestEnum.TESTEMAIL.toString())).thenReturn(false);
        sql_queries.when(() -> SQLQueries.isMatnrUsed(Integer.parseInt(TestEnum.TESTMATRIKELNUMMER.toString())))
                .thenReturn(false);

        // call protected doPost()-Method of RegisterServlet.class
        new RegisterServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals("1", result);
    }

    @Test
    void doPostEmailAlreadyUsed() throws IOException {
        sql_queries.when(() -> SQLQueries.isEmailUsed(any())).thenReturn(true);

        new RegisterServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.MAILERROR.toString(), result);
    }

    @Test
    void doPostMatrikelnummerAlreadyUsed() throws IOException {
        sql_queries.when(() -> SQLQueries.isMatnrUsed(Integer.parseInt(TestEnum.TESTMATRIKELNUMMER.toString())))
                .thenReturn(true);

        new RegisterServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String result = stringWriter.toString().trim();
        assertEquals("matnrError", result);
    }
}

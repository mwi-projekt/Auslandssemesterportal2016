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
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.RegisterServlet;

public class RegisterServletTest {
    // initalize all necessary mocks
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);
    Message message = mock(Message.class);
    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);

    // initialize all necessary instances
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    // initialize all necessary static mocks
    MockedStatic<Util> util;
    MockedStatic<SQL_queries> sql_queries;
    MockedStatic<Transport> transport;
    MockedStatic<DB> db;

    @BeforeMethod
    public void init() throws IOException {
        // define all necessary static mock instances
        util = Mockito.mockStatic(Util.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        transport = Mockito.mockStatic(Transport.class);
        db = Mockito.mockStatic(DB.class);

        // define what happens when a mocked method is called
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

        sql_queries.when(() -> SQL_queries.isEmailUsed(TestEnum.TESTEMAIL.toString())).thenReturn(false);
        sql_queries.when(() -> SQL_queries.isMatnrUsed(Integer.parseInt(TestEnum.TESTMATRNR.toString())))
                .thenReturn(false);
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenCallRealMethod();
        db.when(() -> DB.getInstance()).thenReturn(connection);
        sql_queries.when(() -> connection.prepareStatement(any())).thenReturn(preparedStatement);
        sql_queries.when(() -> preparedStatement.executeUpdate()).thenReturn(1);

        util.when(() -> Util.getEmailMessage(any(), any())).thenReturn(message);
        util.when(() -> Util.generateSalt()).thenCallRealMethod();
        util.when(() -> Util.HashSha256(any())).thenCallRealMethod();

        when(response.getWriter()).thenReturn(writer);
    }

    @AfterMethod
    public void close() {
        // close all static mocks
        util.close();
        sql_queries.close();
        transport.close();
        db.close();
    }

    @Test
    public void testDoPost() throws IOException {
        // call protected doPost()-Method of RegisterServlet.class
        RegisterServlet registerServlet = new RegisterServlet() {
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

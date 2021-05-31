package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import dhbw.mwi.Auslandsemesterportal2016.rest.RegisterServlet;

public class RegisterServletTest {
    //Method registerDoPost;
    RegisterServlet registerServlet = new RegisterServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ResultSet resultSet = mock(ResultSet.class);
    Message message = mock(Message.class);
    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    MockedStatic<Util> util;
    MockedStatic<SQL_queries> sql_queries;
    MockedStatic<Transport> transport;
    MockedStatic<DB> db;

    // set up request data
    String email = "testusermwi@dhbw.de";
    String matNr = "1234567";
    String rolle = "Studierender";
    String pw = "Test1234";
    String vorname = "Test";
    String nachname = "Test";
    String studiengang = "Wirtschaftsinformatik";
    String kurs = "WWI18B5";
    String tel = "01524875618";
    String mobil = "01524875618";
    String standort = "Karlsruhe";

    @BeforeMethod
    public void init() throws NoSuchMethodException, SecurityException, IOException {
        util = Mockito.mockStatic(Util.class);
        sql_queries = Mockito.mockStatic(SQL_queries.class);
        transport = Mockito.mockStatic(Transport.class);
        db = Mockito.mockStatic(DB.class);

        //when(registerServlet.doPost(request, response)).thenCallRealMethod();

        sql_queries.when(()->SQL_queries.isEmailUsed(email)).thenReturn(false);
        sql_queries.when(()->SQL_queries.isMatnrUsed(Integer.parseInt(matNr))).thenReturn(false);
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(), anyString(),
            anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenCallRealMethod();
        db.when(() -> DB.getInstance()).thenReturn(connection);
        sql_queries.when(() -> connection.prepareStatement(any())).thenReturn(preparedStatement);
        sql_queries.when(() -> preparedStatement.executeUpdate()).thenReturn(1);

        util.when(()->Util.getEmailMessage(any(), any())).thenReturn(message);
        //transport.when(()->Transport.send(any()));
        
        when(request.getParameter("rolle")).thenReturn(rolle);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("passwort")).thenReturn(pw);
        when(request.getParameter("vorname")).thenReturn(vorname);
        when(request.getParameter("nachname")).thenReturn(nachname);
        when(request.getParameter("studiengang")).thenReturn(studiengang);
        when(request.getParameter("kurs")).thenReturn(kurs);
        when(request.getParameter("matrikelnummer")).thenReturn(matNr);
        when(request.getParameter("tel")).thenReturn(tel);
        when(request.getParameter("mobil")).thenReturn(mobil);
        when(request.getParameter("standort")).thenReturn(standort);
        
        //registerDoPost = registerServlet.getClass().getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class );
        //registerDoPost.setAccessible(true);

        when(response.getWriter()).thenReturn(writer);
    }
    @AfterMethod
    public void close() {
        //registerDoPost.setAccessible(false);

        util.close();
        sql_queries.close();
        transport.close();
        db.close();
    }

    @Test
    public void testDoPost() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        
        System.out.println("test");
        registerServlet.doPost(request, response);
        String result = stringWriter.toString().trim();
        // change to 1 because mocked method above
        assertEquals("0", result);
    }
}

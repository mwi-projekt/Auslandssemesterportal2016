package dhbw.mwi.Auslandsemesterportal2016.test;

import org.junit.*;
import static org.junit.Assert.*;
import dhbw.mwi.Auslandsemesterportal2016.db.*;
import dhbw.mwi.Auslandsemesterportal2016.rest.LoginServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import javax.servlet.http.*;


public class LoginServletTest {
    String[] testUser = TestUser.getUser();
    String[] testAAMA = TestUser.getAAMA();
    String[] testAdmin = TestUser.getAdmin();
    
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse res = mock(HttpServletResponse.class);
    
    /*
    @Test
    public void testWrongCredentials() {
        try {
            when(req.getParameter("email")).thenReturn(testUser[0]);
            when(req.getParameter("pw")).thenReturn("123456789");
            StringWriter sW = new StringWriter();
            when(res.getWriter()).thenReturn(new PrintWriter(sW));
            
            (new LoginServlet()).doPost(req, res);
            String[] result = sW.toString().split(";", -1);
            assertEquals(4, result.length);
            assertEquals("2", result[0]);
        } catch (IOException ex) {
            fail();
        }
     
    }
    
    @Test
    public void testCorrectUser() {
        try {
            when(req.getParameter("email")).thenReturn(testUser[0]);
            when(req.getParameter("pw")).thenReturn(testUser[1]);
            StringWriter sW = new StringWriter();
            when(res.getWriter()).thenReturn(new PrintWriter(sW));
            
            (new LoginServlet()).doPost(req, res);
            String[] result = sW.toString().split(";");
            assertEquals(4, result.length);
            assertEquals("1", result[0]);
            assertEquals("3", result[3]);
        } catch (IOException ex) {
            fail();
        }
     
    }
    
    @Test
    public void testCorrectAAMA() {
        try {
            when(req.getParameter("email")).thenReturn(testAAMA[0]);
            when(req.getParameter("pw")).thenReturn(testAAMA[1]);
            StringWriter sW = new StringWriter();
            when(res.getWriter()).thenReturn(new PrintWriter(sW));
            
            (new LoginServlet()).doPost(req, res);
            String[] result = sW.toString().split(";");
            assertEquals(4, result.length);
            assertEquals("1", result[0]);
            assertEquals("2", result[3]);
        } catch (IOException ex) {
            fail();
        }
     
    }
    
    @Test
    public void testCorrectAdmin() {
        try {
            when(req.getParameter("email")).thenReturn(testAdmin[0]);
            when(req.getParameter("pw")).thenReturn(testAdmin[1]);
            StringWriter sW = new StringWriter();
            when(res.getWriter()).thenReturn(new PrintWriter(sW));
            
            (new LoginServlet()).doPost(req, res);
            String[] result = sW.toString().split(";");
            assertEquals(4, result.length);
            assertEquals("1", result[0]);
            assertEquals("1", result[3]);
        } catch (IOException ex) {
            fail();
        }
     
    }*/
    

}

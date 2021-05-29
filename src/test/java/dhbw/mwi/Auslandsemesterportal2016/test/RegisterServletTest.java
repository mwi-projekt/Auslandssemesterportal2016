package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mock;
import org.mockito.verification.VerificationMode;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.rest.RegisterServlet;

public class RegisterServletTest {

    /*
    @Mock
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RegisterServletMock registerServletMock = mock(RegisterServletMock.class);
    RegisterServlet registerServlet = mock(RegisterServlet.class);

    
    // add anonymous class to access protected doPost() in RegisterServlet.class
    class RegisterServletMock extends RegisterServlet {
        
        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            registerServlet.doPost(request, response);
        }
    }
    */
    

    /*
    *   method verifies that doPost() is called
    *   
    *   method includes database calls in external class (SQL_queries), which are not part of unit-testing
    *   that's why RegisterServlet is mocked
    */

    /*
    @Test
    public void verifyDoPostMethod () throws IOException {
        doNothing().when(registerServletMock).doPost(request, response);

        registerServletMock.doPost(request, response);
        verify(registerServletMock,times(1)).doPost(request, response);
    }
    */
}

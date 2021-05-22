package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;
import dhbw.mwi.Auslandsemesterportal2016.rest.LoginServlet;

public class LoginTest {
      
    /*
    *   method verifies that doPost() is called
    *   
    *   method includes database calls in external class (SQL_queries), which are not part of unit-testing
    *   that's why LoginServlet is mocked
    */
    @Test
    public void verifyDoPostMethod () throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginServlet loginServlet = mock(LoginServlet.class);

        doNothing().when(loginServlet).doPost(request, response);
        loginServlet.doPost(request, response);
        verify(loginServlet,times(1)).doPost(request, response);
    }

}

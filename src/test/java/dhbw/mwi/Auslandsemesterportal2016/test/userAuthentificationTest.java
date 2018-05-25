package dhbw.mwi.Auslandsemesterportal2016.test;

import org.junit.*;
import static org.junit.Assert.*;
import dhbw.mwi.Auslandsemesterportal2016.db.*;
import static org.mockito.Mockito.*;
import javax.servlet.http.*;


public class userAuthentificationTest {
    String testUser_mail = "testusermwi@dhbw.de";
    String testUser_pw = "Password1234";
    int testUser_id = 368;
    int testUser_rolle = 3;
    
    String testAAMA_mail = "testaamamwi@dhbw.de";
    String testAAMA_pw = "Password1234";
    int testAAMA_id = 370;
    int testAAMA_rolle = 2;
    
    String testAdmin_mail = "testadminmwi@dhbw.de";
    String testAdmin_pw = "Password1234";
    int testAdmin_id = 371;
    int testAdmin_rolle = 1;

    
    
    HttpServletRequest req = mock(HttpServletRequest.class);
    
    
    

    @Test
    public void testNoCookie() {
       assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), -1);
    }
    
    @Test
    public void testNoValidSession() {
        Cookie cookie_mail = new Cookie("email", testUser_mail);
        Cookie cookie_sessionID = new Cookie("sessionID", "123456789abc");
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
       assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), 0);
    }
    
    @Test
    public void testValidSession() {
        String accessToken = SQL_queries.createUserSession(testUser_id);
        Cookie cookie_mail = new Cookie("email", testUser_mail);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), 3);
    }
    
    @Test
    public void testAAMASession() {
        String accessToken = SQL_queries.createUserSession(testAAMA_id);
        Cookie cookie_mail = new Cookie("email", testAAMA_mail);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), 2);
    }
    
      @Test
    public void testAdminSession() {
        String accessToken = SQL_queries.createUserSession(testAdmin_id);
        Cookie cookie_mail = new Cookie("email", testAdmin_mail);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), 1);
    }

}

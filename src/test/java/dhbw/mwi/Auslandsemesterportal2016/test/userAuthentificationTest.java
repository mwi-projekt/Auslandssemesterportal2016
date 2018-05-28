package dhbw.mwi.Auslandsemesterportal2016.test;

import org.junit.*;
import static org.junit.Assert.*;
import dhbw.mwi.Auslandsemesterportal2016.db.*;
import static org.mockito.Mockito.*;
import javax.servlet.http.*;


public class userAuthentificationTest {
    String[] testUser = TestUser.getUser();
    String[] testAAMA = TestUser.getAAMA();
    String[] testAdmin = TestUser.getAdmin();
    
    HttpServletRequest req = mock(HttpServletRequest.class);
    
    
    

    @Test
    public void testNoCookie() {
       assertEquals(-1, userAuthentification.isUserAuthentifiedByCookie(req));
    }
    
    @Test
    public void testNoValidSession() {
        Cookie cookie_mail = new Cookie("email", testUser[0]);
        Cookie cookie_sessionID = new Cookie("sessionID", "123456789abc");
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
       assertEquals(userAuthentification.isUserAuthentifiedByCookie(req), 0);
    }
    
    @Test
    public void testValidSession() {
        String accessToken = SQL_queries.createUserSession(Integer.parseInt(testUser[2]));
        Cookie cookie_mail = new Cookie("email", testUser[0]);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(3, userAuthentification.isUserAuthentifiedByCookie(req));
    }
    
    @Test
    public void testAAMASession() {
        String accessToken = SQL_queries.createUserSession(Integer.parseInt(testAAMA[2]));
        Cookie cookie_mail = new Cookie("email", testAAMA[0]);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(2, userAuthentification.isUserAuthentifiedByCookie(req));
    }
    
      @Test
    public void testAdminSession() {
        String accessToken = SQL_queries.createUserSession(Integer.parseInt(testAdmin[2]));
        Cookie cookie_mail = new Cookie("email", testAdmin[0]);
        Cookie cookie_sessionID = new Cookie("sessionID", accessToken);
        Cookie[] cookies = new Cookie[2];
        cookies[0] = cookie_mail;
        cookies[1] = cookie_sessionID;
        
        when(req.getCookies()).thenReturn(cookies);
        assertEquals(1, userAuthentification.isUserAuthentifiedByCookie(req));
    }

}

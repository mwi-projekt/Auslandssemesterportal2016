package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.User;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTEMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthentificationTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    MockedStatic<SQLQueries> sql_queriesMockedStatic = mockStatic(SQLQueries.class);

    @AfterEach
    void tearDown() {
        sql_queriesMockedStatic.close();
    }

    @Test
    void isUserAuthentifiedByCookie_noCookiesPresent() {
        when(request.getCookies()).thenReturn(null);

        int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

        assertEquals(-1, rolle);
    }

    @Test
    void isUserAuthentifiedByCookie_oneCookieMissing() {
        Cookie cookie = new Cookie("email", TESTEMAIL.toString());
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        int rolle = UserAuthentification.isUserAuthentifiedByCookie(request);

        assertEquals(0, rolle);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void isUserAuthentifiedByCookie_CookiesOK(int rolle) {
        Cookie cookieMail = new Cookie("email", TESTEMAIL.toString());
        Cookie cookieSessionId = new Cookie("sessionID", "randomString");
        when(request.getCookies()).thenReturn(new Cookie[]{cookieMail, cookieSessionId});

        sql_queriesMockedStatic.when(() -> SQLQueries.checkUserSession(cookieSessionId.getValue(), cookieMail.getValue()))
                        .thenReturn(true);
        sql_queriesMockedStatic.when(() -> SQLQueries.getRoleForUser(cookieMail.getValue())).thenReturn(rolle);

        int actualRolle = UserAuthentification.isUserAuthentifiedByCookie(request);

        assertEquals(rolle, actualRolle);
    }

    @Test
    void getUserInfo_noCookiesPresent() {
        when(request.getCookies()).thenReturn(null);

        User userInfo = UserAuthentification.getUserInfo(request);

        assertNull(userInfo);
    }

    @Test
    void getUserInfo_oneCookieMissing() {
        Cookie cookie = new Cookie("email", TESTEMAIL.toString());
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        User userInfo = UserAuthentification.getUserInfo(request);

        assertNull(userInfo);
    }

    @Test
    void getUserInfo_returnUserInfo() {
        Cookie cookieMail = new Cookie("email", TESTEMAIL.toString());
        Cookie cookieSessionId = new Cookie("sessionID", "randomString");
        when(request.getCookies()).thenReturn(new Cookie[]{cookieMail, cookieSessionId});

        User user = new User();
        user.id = 1234;
        user.rolle = 1;
        user.matrikelnummer = "1901901";
        sql_queriesMockedStatic.when(() -> SQLQueries.checkUserSession(cookieSessionId.getValue(), cookieMail.getValue()))
                .thenReturn(true);
        sql_queriesMockedStatic.when(() -> SQLQueries.getUserInfo(cookieMail.getValue())).thenReturn(user);

        User userInfo = UserAuthentification.getUserInfo(request);

        assertEquals(user, userInfo);
    }

    @Test
    void isTestuserIsTrue() {
        Cookie[] cookies = new Cookie[] {
                new Cookie("email", "test@student.dhbw-karlsruhe.de"),
                new Cookie("sessionID", "anyString")
        };

        boolean isTestUser = UserAuthentification.isTestUser(cookies);

        assertTrue(isTestUser);
    }

    @Test
    void isTestuserIsFalse() {
        Cookie[] cookies = new Cookie[] {
                new Cookie("email", "hahn.karen@student.dhbw-karlsruhe.de"),
                new Cookie("sessionID", "anyString")
        };

        boolean isTestUser = UserAuthentification.isTestUser(cookies);

        assertFalse(isTestUser);
    }

    @Test
    void isTestuserByEmail() {
        assertTrue(UserAuthentification.isTestUser("test@student.dhbw-karlsruhe.de"));
    }
}
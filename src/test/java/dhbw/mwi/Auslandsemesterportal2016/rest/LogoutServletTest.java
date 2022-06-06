package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTEMAIL;
import static org.mockito.Mockito.*;

class LogoutServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
    }

    @AfterEach
    void tearDown() {
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
    }

    @Test
    void doGetUnauthorizedUser() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(0);

        new LogoutServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(request, times(0)).getCookies();
    }

    @Test
    void doGetCookiesSuccessfullyCancelled() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(2);

        Cookie emailCookie = new Cookie("email", TESTEMAIL.toString());
        Cookie sessionIdCookie = new Cookie("sessionID", "anyId");
        when(request.getCookies()).thenReturn(new Cookie[] {emailCookie, sessionIdCookie});

        new LogoutServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(2)).addCookie(any());
    }
}
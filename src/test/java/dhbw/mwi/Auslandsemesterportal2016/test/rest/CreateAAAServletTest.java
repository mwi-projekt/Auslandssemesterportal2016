package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.CreateAAAServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CreateAAAServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    ResultSet resultSet = mock(ResultSet.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<SQL_queries> sql_queries;

    // Initialization of necessary instances
    StringWriter stringWriter;
    PrintWriter writer;
    Cookie c1 = new Cookie("email", TestEnum.TESTEMAIL.toString());
    Cookie c2 = new Cookie("sessionID", TestEnum.TESTSESSIONID.toString());
    Cookie[] cookies = { c1, c2 };
    CreateAAAServlet aaaServlet = new CreateAAAServlet();

    @BeforeEach
    public void init() throws IOException, SQLException {
        // Define necessary mock objects for mocking static methods
        sql_queries = Mockito.mockStatic(SQL_queries.class);

        // Define necessary instances
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        // Define what happens when mocked method is called
        sql_queries.when(() -> SQL_queries.checkUserSession(any(), any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.getRoleForUser(any())).thenCallRealMethod();
        sql_queries.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
        sql_queries.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenReturn(1);

        when(response.getWriter()).thenReturn(writer);

        when(request.getCookies()).thenReturn(cookies);
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("vorname")).thenReturn(TestEnum.TESTVNAME.toString());
        when(request.getParameter("nachname")).thenReturn(TestEnum.TESTNNAME.toString());
        when(request.getParameter("tel")).thenReturn(TestEnum.TESTTELNR.toString());
        when(request.getParameter("mobil")).thenReturn(TestEnum.TESTMOBILNR.toString());

        // 1 = Admin
        when(resultSet.getInt(anyInt())).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        sql_queries.close();

        // Close instances
        writer.close();
    }

    @Test
    void doPostForRoleAdmin() throws IOException, ServletException {
        sql_queries.when(() -> SQL_queries.isEmailUsed(any())).thenReturn(false);
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                        anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenCallRealMethod();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Mockito.doAnswer((Answer<Object>) invocation -> {
            writer.print(SuccessEnum.CREATEUSER);
            return null;
        }).when(requestDispatcher).forward(any(), any());

        aaaServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString().trim();
        assertEquals(SuccessEnum.CREATEUSER.toString(), result);
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        int rolle = 2;
        MockedStatic<UserAuthentification> userAuthentificationMock = Mockito.mockStatic(UserAuthentification.class);
        userAuthentificationMock.when(() -> UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(rolle);

        aaaServlet.doPost(request, response);

        verify(response, times(1)).sendError(401, "Rolle: " + rolle);

        userAuthentificationMock.close();
    }

    @Test
    void doPostEmailAlreadyUsed() throws IOException {
        sql_queries.when(() -> SQL_queries.isEmailUsed(any())).thenReturn(true);

        aaaServlet.doPost(request, response);

        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.MAILERROR.toString(), result);
    }

    @Test
    void doPostRegistrationFails() throws IOException {
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                        anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(0);

        aaaServlet.doPost(request, response);

        String result = stringWriter.toString().trim();
        assertEquals(ErrorEnum.USERREGISTER.toString(), result);
    }

    @Test
    void doPostInterruptedByException() throws IOException {
        sql_queries.when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(),
                        anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenCallRealMethod();
        // throw any Exception in try-Block
        when(request.getRequestDispatcher(anyString())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> aaaServlet.doPost(request, response));
        //TODO check why e.getMessage() is always null
        verify(response, times(1)).sendError(500, ErrorEnum.USERCREATE + "null");
    }
}
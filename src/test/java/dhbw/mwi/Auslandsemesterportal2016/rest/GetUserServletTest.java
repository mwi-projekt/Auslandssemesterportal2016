package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTNACHNAME;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTVORNAME;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetUserServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ResultSet resultSet;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        resultSet = mock(ResultSet.class);

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        sqlQueriesMockedStatic.when(() -> SQLQueries.executeStatement(anyString())).thenReturn(resultSet);

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);
    }

    @AfterEach
    void tearDown() throws SQLException, IOException {
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
        resultSet.close();
        writer.close();
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(3);

        new GetUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doGetMissingRoleParam() throws IOException {
        new GetUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doGetMatchingUsers() throws IOException, SQLException {
        when(request.getParameter("rolle")).thenReturn("3");

        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn("vorname");
        when(resultSetMetaData.getColumnName(2)).thenReturn("nachname");
        when(resultSet.getString(1)).thenReturn(TESTVORNAME.toString());
        when(resultSet.getString(2)).thenReturn(TESTNACHNAME.toString());

        new GetUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"data\":[{\"vorname\":\"Test\",\"nachname\":\"Admin\"},{\"vorname\":\"Test\",\"nachname\":\"Admin\"}]}";
        assertEquals(expected, writer.toString().trim());
    }
}
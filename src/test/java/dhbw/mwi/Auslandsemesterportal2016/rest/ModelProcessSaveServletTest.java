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
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.executeStatement;
import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.executeUpdate;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.DBERROR;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ModelProcessSaveServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn("anyString");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        resultSet = mock(ResultSet.class);
    }

    @AfterEach
    void tearDown() throws SQLException {
        userAuthentificationMockedStatic.close();
        sqlQueriesMockedStatic.close();
        resultSet.close();
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(2);

        new ModelProcessSaveServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doPostParamMissing() throws IOException {
        when(request.getParameter(anyString())).thenReturn(null);

        new ModelProcessSaveServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(PARAMMISSING.toString(), writer.toString().trim());
    }

    @Test
    void doPostProblemWhileProcessingResultSet() throws SQLException, IOException {
        sqlQueriesMockedStatic.when(() -> executeStatement(anyString(), any(), any())).thenReturn(resultSet);
        when(resultSet.next()).thenThrow(new RuntimeException("anyMessage"));

        new ModelProcessSaveServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(DBERROR + "\nanyMessage", writer.toString().trim());
    }

    @Test
    void doPostUpdateStepSuccessfully() throws SQLException, IOException {
        sqlQueriesMockedStatic.when(() -> executeStatement(anyString(), any(), any())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("id")).thenReturn("unique");

        new ModelProcessSaveServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        sqlQueriesMockedStatic.verify(() -> executeUpdate("UPDATE processModel SET model = ?, step = ?, json = ?, stepNumber = ? WHERE id = ?",
                new String[] {"anyString", "anyString", "anyString", "anyString", "unique"},
                new String[] {"String", "String", "String", "int", "int"}),
                times(1));
    }

    @Test
    void doPostInsertNewStepSuccessfully() throws SQLException, IOException {
        sqlQueriesMockedStatic.when(() -> executeStatement(anyString(), any(), any())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        new ModelProcessSaveServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        sqlQueriesMockedStatic.verify(() -> executeUpdate("INSERT INTO processModel (model, step, json, stepNumber) VALUES (?,?,?,?)",
                new String[] {"anyString", "anyString", "anyString", "anyString"},
                new String[] {"String", "String", "String", "int"}), times(1));
    }
}
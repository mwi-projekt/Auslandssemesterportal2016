package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.*;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelProcessGetServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("model")).thenReturn("anyModel");
        when(request.getParameter("step")).thenReturn("anyStep");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        resultSet = mock(ResultSet.class);
        sqlQueriesMockedStatic.when(() -> getProcessModelJson(anyString(), anyString())).thenReturn(resultSet);
    }

    @AfterEach
    void tearDown() throws IOException, SQLException {
        writer.close();
        sqlQueriesMockedStatic.close();
        resultSet.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"model", "step"})
    void doGetParamMissing(String missingParam) throws IOException {
        when(request.getParameter(missingParam)).thenReturn(null);

        new ModelProcessGetServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(PARAMMISSING.toString(), writer.toString().trim());
    }

    @Test
    void doGetReceiveJsonModelSuccessfully() throws SQLException, IOException {
        when(resultSet.next()).thenReturn(true);
        String expected = "{\"this\":\"is a json\"}";
        when(resultSet.getString("json")).thenReturn(expected);

        new ModelProcessGetServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(expected, writer.toString().trim());
    }

    @Test
    void doGetNoEntryPresent() throws SQLException, IOException {
        when(resultSet.next()).thenReturn(false);

        new ModelProcessGetServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals("Error: can not find entry", writer.toString().trim());
    }

    @Test
    void doGetExceptionWhileProcessingWithResultSet() throws IOException, SQLException {
        when(resultSet.next()).thenThrow(new RuntimeException("anyMessage"));

        new ModelProcessGetServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(ErrorEnum.DBERROR + "\nanyMessage", writer.toString().trim());
    }
}
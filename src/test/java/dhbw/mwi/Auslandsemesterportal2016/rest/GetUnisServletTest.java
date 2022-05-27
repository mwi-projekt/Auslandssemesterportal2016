package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
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

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.executeStatement;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetUnisServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ResultSet resultSet;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        resultSet = mock(ResultSet.class);
    }

    @AfterEach
    void tearDown() throws SQLException, IOException {
        writer.close();
        sqlQueriesMockedStatic.close();
        resultSet.close();
    }

    @Test
    void doGetParamMissing() throws IOException {
        when(request.getParameter("studiengang")).thenReturn(null);

        new GetUnisServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doGetUnisForMatchingSubject() throws IOException, SQLException {
        when(request.getParameter("studiengang")).thenReturn("Wirtschaftsinformatik");
        sqlQueriesMockedStatic.when(() -> executeStatement("SELECT uniTitel FROM cms_auslandsAngeboteInhalt WHERE studiengang ='Wirtschaftsinformatik' "))
                .thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSetMetaData.getColumnName(1)).thenReturn("uni");
        when(resultSet.getString(1)).thenReturn("USA");

        new GetUnisServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("{\"data\":[{\"uni\":\"USA\"}]}", writer.toString().trim());
    }
}
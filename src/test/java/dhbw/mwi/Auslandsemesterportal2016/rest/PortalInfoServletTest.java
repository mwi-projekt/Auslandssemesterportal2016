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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class PortalInfoServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        resultSet = mock(ResultSet.class);
        resultSetMetaData = mock(ResultSetMetaData.class);
    }

    @AfterEach
    void tearDown() throws IOException, SQLException {
        writer.close();
        sqlQueriesMockedStatic.close();
        resultSet.close();
    }

    @Test
    void doGetInfoMaterialSuccessfully() throws IOException, SQLException {
        sqlQueriesMockedStatic.when(() -> executeStatement(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn("titel");
        when(resultSetMetaData.getColumnName(2)).thenReturn("inhalt");
        when(resultSet.getString(1)).thenReturn("Infomaterial");
        when(resultSet.getString(2)).thenReturn("anyPdf");

        new InfoMaterialServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"data\":[{\"titel\":\"Infomaterial\",\"inhalt\":\"anyPdf\"}]}";
        assertEquals(expected, writer.toString().trim());
    }
}
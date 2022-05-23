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
import static org.mockito.Mockito.*;

class AuslandsAngebotsInhalteTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private StringWriter writer;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
        resultSet = mock(ResultSet.class);
        sqlQueriesMockedStatic.when(() -> executeStatement(anyString())).thenReturn(resultSet);
    }

    @AfterEach
    void tearDown() {
        sqlQueriesMockedStatic.close();
    }

    @Test
    void doGetHappyPath() throws SQLException, IOException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(3);
        when(resultSetMetaData.getColumnName(1)).thenReturn("ID");
        when(resultSetMetaData.getColumnName(2)).thenReturn("studiengang");
        when(resultSetMetaData.getColumnName(3)).thenReturn("uniTitel");
        when(resultSet.getString(1)).thenReturn("1");
        when(resultSet.getString(2)).thenReturn("Wirtschaftsinformatik");
        when(resultSet.getString(3)).thenReturn("Abertay University of Dundee (Schottland)");

        new AuslandsAngebotsInhalte() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"data\":[{\"ID\":\"1\",\"studiengang\":\"Wirtschaftsinformatik\",\"uniTitel\":\"Abertay University of Dundee (Schottland)\"}]}";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }
}
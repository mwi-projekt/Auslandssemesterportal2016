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

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuslandsAngeboteTest {

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
    void doGetHappyPath() throws IOException, SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSetMetaData.getColumnName(1)).thenReturn("ID");
        when(resultSetMetaData.getColumnName(2)).thenReturn("studiengang");
        when(resultSet.getString(1)).thenReturn("Wirtschaftsinformatik");
        when(resultSet.getString(2)).thenReturn("Wirtschaftsingenieurswesen");

        new AuslandsAngebote() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "{\"data\":[{\"ID\":\"Wirtschaftsinformatik\",\"studiengang\":\"Wirtschaftsingenieurswesen\"}]}";
        String actual = writer.toString().trim();
        assertEquals(expected, actual);
    }
}
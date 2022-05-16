package dhbw.mwi.Auslandsemesterportal2016.test;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.rest.ActivateUserServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static org.mockito.Mockito.*;

class ActivateUserServletTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    MockedStatic<DB> dbMockedStatic = mockStatic(DB.class);
    Connection connection = mock(Connection.class);
    Statement statement = mock(Statement.class);
    ResultSet resultSet = mock(ResultSet.class);
    ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);

    @BeforeEach
    void setUp() throws SQLException {
        dbMockedStatic.when(DB::getInstance).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dbMockedStatic.close();
        connection.close();
        statement.close();
        resultSet.close();
    }

    @Test
    void happyPath() throws IOException, SQLException {
        // given
        when(request.getParameter("code")).thenReturn("anyCode");
        when(resultSetMetaData.getColumnCount()).thenReturn(1);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("anyUserId");
        when(statement.executeUpdate(anyString())).thenReturn(1);

        // when
        new ActivateUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        verify(statement, times(1)).executeQuery("SELECT userID FROM user WHERE verifiziert='anyCode'");
        verify(statement, times(1)).executeUpdate("UPDATE user SET verifiziert = 1 WHERE userID=anyUserId");
    }

    @Test
    void doGetWithMultipleResults() throws SQLException, IOException {
        // given
        when(request.getParameter("code")).thenReturn("anyCode");
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("overwrittenUserId");
        when(resultSet.getString(2)).thenReturn("anyUserId");
        when(statement.executeUpdate(anyString())).thenReturn(1);

        // when
        new ActivateUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        verify(statement, times(1)).executeQuery("SELECT userID FROM user WHERE verifiziert='anyCode'");
        verify(statement, times(1)).executeUpdate("UPDATE user SET verifiziert = 1 WHERE userID=anyUserId");
    }

    @Test
    void doGetWithCodeEqualsNull() throws SQLException, IOException {
        // given
        when(request.getParameter("code")).thenReturn(null);

        // when
        new ActivateUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        verify(statement, times(0)).executeQuery(anyString());
        verify(statement, times(0)).executeUpdate(anyString());
    }

    @Test
    void doGetWithUserIdEquals_() throws SQLException, IOException {
        // given
        when(request.getParameter("code")).thenReturn("anyCode");
        when(resultSetMetaData.getColumnCount()).thenReturn(2);
        when(resultSet.next()).thenReturn(false);

        // when
        new ActivateUserServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        verify(statement, times(1)).executeQuery("SELECT userID FROM user WHERE verifiziert='anyCode'");
        verify(statement, times(0)).executeUpdate(anyString());
    }
}
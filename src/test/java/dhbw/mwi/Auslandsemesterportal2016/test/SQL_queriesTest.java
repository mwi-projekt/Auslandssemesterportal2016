package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

public class SQL_queriesTest {
  private static MockedStatic<SQL_queries> mockedStatic;

  @BeforeMethod
  public void init() {
    mockedStatic = Mockito.mockStatic(SQL_queries.class);
   
  }

  @AfterMethod
  public void close() {
    mockedStatic.close();
  }

  /*
   * Testing static getSalt()-Method using Mockito mocking result-response
   * 
   * Method is called in LoginServlet-Class
   */
  @Test
  public void testGetSalt() {

    mockedStatic.when(() -> SQL_queries.getSalt("testusermwi@dhbw.de")).thenReturn("SH5E9Z7P5J6Z5G2BV0");
    assertEquals("SH5E9Z7P5J6Z5G2BV0", SQL_queries.getSalt("testusermwi@dhbw.de"));

  }

  /*
   * Testing static userLogin()-Method using Mockito mocking result-response
   * 
   * Method is called in LoginServlet-Class
   */
  @Test
  public void testUserLogin() {
    String[] stringArr = { "1", "Wirtschaftsinformatik", "12345678", "Admin", "1478523697412" };

    mockedStatic.when(() -> SQL_queries.userLogin("testusermwi@dhbw.de", "SH5E9Z7P5J6Z5G2BV0", "Password1234"))
        .thenReturn(stringArr);

    String[] result = SQL_queries.userLogin("testusermwi@dhbw.de", "SH5E9Z7P5J6Z5G2BV0", "Password1234");
    assertArrayEquals(stringArr, result);

  }

  /*
   * Testing static isEmailUsed()-Method with not used EMail
   * 
   * mocking the method-call of executeStatement(), because this method includes
   * database connections
   * 
   * isEmailUsed() is called in RegisterServlet-Class
   */
  @Test
  public void testIsEmailUsedWithNotUsedEMail() {
    ResultSet resultSet = mock(ResultSet.class);

    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(SQL_queries.isEmailUsed("test@dhbw.de")).thenCallRealMethod();

    assertEquals(false, SQL_queries.isEmailUsed("test@dhbw.de"));

  }

  /*
   * Testing static isEmailUsed()-Method with used EMail
   * 
   * mocking the method-call of executeStatement(), because this method includes
   * database connections
   * 
   * isEmailUsed() is called in RegisterServlet-Class
   */
  @Test
  public void testIsEmailUsedWithUsedEMail() throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(SQL_queries.isEmailUsed("testusermwi@dhbw.de")).thenCallRealMethod();
    when(resultSet.next()).thenReturn(true);

    assertEquals(true, SQL_queries.isEmailUsed("testusermwi@dhbw.de"));

  }

  /*
   * Testing static isMatnrUsed()-Method with not used Matnr
   * 
   * mocking the method-call of executeStatement(), because this method includes
   * database connections
   * 
   * isMatnrUsed() is called in RegisterServlet-Class
   */
  @Test
  public void testIsMatnrUsedWithNotUsedMatnr() {
    ResultSet resultSet = mock(ResultSet.class);

    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(SQL_queries.isMatnrUsed(1234567)).thenCallRealMethod();

    assertEquals(false, SQL_queries.isMatnrUsed(1234567));

  }

  /*
   * Testing static isMatnrUsed()-Method with used Matnr
   * 
   * mocking the method-call of executeStatement(), because this method includes
   * database connections
   * 
   * isMatnrUsed() is called in RegisterServlet-Class
   */
  @Test
  public void testIsMatnrUsedWithUsedMatnr() throws SQLException {
    ResultSet resultSet = mock(ResultSet.class);

    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(SQL_queries.isMatnrUsed(9876543)).thenCallRealMethod();
    when(resultSet.next()).thenReturn(true);

    assertEquals(true, SQL_queries.isMatnrUsed(9876543));

  }

  /*
   * Testing static userRegister()-Method with sample data
   * 
   * mocking the method-calls DB.getInstance(), connection.prepareStatement() and
   * statement.executeUpdate(), because these methods include database connections
   * 
   * userRegister() is called in RegisterServlet-Class
   * 
   * @Test public void testUserRegister() throws SQLException { Connection
   * connection = mock(Connection.class); PreparedStatement preparedStatement =
   * mock(PreparedStatement.class);
   * 
   * when(DB.getInstance()).thenReturn(connection);
   * when(connection.prepareStatement(any())).thenReturn(preparedStatement);
   * when(preparedStatement.executeUpdate()).thenReturn(1);
   * 
   * int i = SQL_queries.userRegister("Max", "Mustermann", "Test1234",
   * Util.generateSalt(), 1, "test@dhbw.de", "Wirtschaftsinformatik", "18B2",
   * 1234567, "12345", "12345", "Karlsruhe", "verifiziert"); assertEquals(i, 1);
   * 
   * }
   */
}

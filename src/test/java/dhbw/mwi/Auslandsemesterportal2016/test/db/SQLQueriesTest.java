package dhbw.mwi.Auslandsemesterportal2016.test.db;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SQLQueriesTest {
  private static MockedStatic<SQL_queries> mockedStatic;
  private Connection connection;
  private PreparedStatement preparedStatement;
  private MockedStatic<DB> mockedStaticDb;

  @BeforeEach
  public void init() {
    mockedStatic = Mockito.mockStatic(SQL_queries.class);
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    mockedStaticDb = Mockito.mockStatic(DB.class);

    mockedStatic.when(() -> SQL_queries.executeUpdate(any(), any(), any())).thenCallRealMethod();
    mockedStatic.when(() -> SQL_queries.executeStatement(anyString(), any(), any())).thenCallRealMethod();
    mockedStaticDb.when(DB::getInstance).thenReturn(connection);
    mockedStatic.when(() -> connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    mockedStatic.when(preparedStatement::executeUpdate).thenReturn(1);
  }

  @AfterEach
  public void close() throws SQLException {
    mockedStatic.close();
    connection.close();
    preparedStatement.close();
    mockedStaticDb.close();
  }

  @Test // done
  void getSalt() throws SQLException {
    mockedStatic.when(() -> SQL_queries.getSalt(TESTEMAIL.toString())).thenCallRealMethod();

    String salt = "anySalt";
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString(1)).thenReturn(salt);

    String result = SQL_queries.getSalt(TESTEMAIL.toString());

    assertEquals(salt, result);
  }

  @Test //done
  void getSaltThrowsException() throws SQLException {
    mockedStatic.when(() -> SQL_queries.getSalt(TESTEMAIL.toString())).thenCallRealMethod();

    String salt = "";
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenThrow(RuntimeException.class);

    String result = SQL_queries.getSalt(TESTEMAIL.toString());

    assertEquals(salt, result);
  }

  @ParameterizedTest //done
  @ValueSource(ints = {1, 3})
  void userLogin(int resultCode) throws SQLException {
    // given
    mockedStatic.when(() -> SQL_queries.userLogin(anyString(), anyString(), anyString())).thenCallRealMethod();

    // Data of resultStringArray
    String studiengang = "anyStudiengang";
    String matrikelnummer = "anyMatrikelnummer";
    String rolle = "anyRolle";
    String accessToken;
    if (resultCode == 1) {
      accessToken = "357";
    } else {
      accessToken = "";
    }
    String[] expected = {("" + resultCode), studiengang, matrikelnummer, rolle, accessToken};

    // Mock Responses
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("studiengang")).thenReturn(studiengang);
    when(resultSet.getString("matrikelnummer")).thenReturn(matrikelnummer);
    when(resultSet.getString("rolle")).thenReturn(rolle);
    when(resultSet.getString("verifiziert")).thenReturn("" + resultCode);
    when(resultSet.getInt(anyInt())).thenReturn(357);
    mockedStatic.when(() -> SQL_queries.createUserSession(anyInt())).thenReturn(accessToken);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQL_queries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    assertArrayEquals(expected, result);
  }

  @Test //done
  void userLoginWrongData() throws SQLException {
    // given
    mockedStatic.when(() -> SQL_queries.userLogin(anyString(), anyString(), anyString())).thenCallRealMethod();
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQL_queries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    String[] expected = {"2", "", "", "", ""};
    assertArrayEquals(expected, result);
  }

  @Test // done
  void userLoginDbException() throws SQLException {
    mockedStatic.when(() -> SQL_queries.userLogin(anyString(), anyString(), anyString())).thenCallRealMethod();
    when(preparedStatement.executeQuery()).thenReturn(null);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQL_queries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    String[] expected = {"4", "", "", "", ""};
    assertArrayEquals(expected, result);
  }

  @Test //done
  void isEmailUsedWithUnusedEMail() {
    when(SQL_queries.isEmailUsed(TESTEMAIL.toString())).thenCallRealMethod();

    ResultSet resultSet = mock(ResultSet.class);
    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);

    assertFalse(SQL_queries.isEmailUsed(TESTEMAIL.toString()));
  }

  @Test
  void isEmailUsedWithUsedEMail() throws SQLException {
    when(SQL_queries.isEmailUsed(TESTEMAIL.toString())).thenCallRealMethod();

    ResultSet resultSet = mock(ResultSet.class);
    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    assertTrue(SQL_queries.isEmailUsed(TESTEMAIL.toString()));
  }

  @Test
  void isMatnrUsedWithUnusedMatnr() {
    when(SQL_queries.isMatnrUsed(1234567)).thenCallRealMethod();

    ResultSet resultSet = mock(ResultSet.class);
    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);

    assertFalse(SQL_queries.isMatnrUsed(1234567));
  }

  @Test
  void isMatnrUsedWithUsedMatnr() throws SQLException {
    when(SQL_queries.isMatnrUsed(9876543)).thenCallRealMethod();

    ResultSet resultSet = mock(ResultSet.class);
    mockedStatic.when(() -> SQL_queries.executeStatement(any(), any(), any())).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    assertTrue(SQL_queries.isMatnrUsed(9876543));
  }

  @Test
  void userRegister() throws SQLException {
    mockedStatic
        .when(() -> SQL_queries.userRegister(anyString(), anyString(), anyString(), anyString(), anyInt(), anyString(),
            anyString(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyString()))
        .thenCallRealMethod();

    // vorname, nachname, passwort, salt, rolle, email, studiengang, kurs,
    // matrikelnummer, tel, mobil, standort, verifiziert, resetToken
    int i = SQL_queries.userRegister(TESTVNAME.toString(), TESTNNAME.toString(), "Test1234",
        Util.generateSalt(), 1, TESTEMAIL.toString(), TESTSTUGANG.toString(),
        TESTKURS.toString(), 1234567, TESTTELNR.toString(), TESTMOBILNR.toString(),
        TESTSTANDORT.toString(), "verifiziert");

    // verify Query
    verify(preparedStatement, times(12)).setString(anyInt(), anyString());
    verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
    verify(preparedStatement, times(1)).executeUpdate();
    assertEquals(1, i);
  }

}

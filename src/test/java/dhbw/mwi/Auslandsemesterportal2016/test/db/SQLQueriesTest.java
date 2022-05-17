package dhbw.mwi.Auslandsemesterportal2016.test.db;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SQLQueriesTest {
  private Connection connection;
  private PreparedStatement preparedStatement;
  private MockedStatic<DB> mockedStaticDb;
  private MockedStatic<Util> utilMockedStatic;

  @BeforeEach
  public void init() throws SQLException {
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    mockedStaticDb = mockStatic(DB.class);
    utilMockedStatic = mockStatic(Util.class);

    mockedStaticDb.when(DB::getInstance).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);
  }

  @AfterEach
  public void close() throws SQLException {
    connection.close();
    preparedStatement.close();
    mockedStaticDb.close();
    utilMockedStatic.close();
  }

  @Test
  void getSalt() throws SQLException {
    // given
    String salt = "anySalt";
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString(1)).thenReturn(salt);

    // when
    String result = SQLQueries.getSalt(TESTEMAIL.toString());

    // then
    assertEquals(salt, result);
  }

  @Test
  void getSaltThrowsException() throws SQLException {
    // given
    String salt = "";
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenThrow(RuntimeException.class);

    // when
    String result = SQLQueries.getSalt(TESTEMAIL.toString());

    // then
    assertEquals(salt, result);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 3})
  void userLogin(int resultCode) throws SQLException {
    // given (Data of resultStringArray)
    String studiengang = "anyStudiengang";
    String matrikelnummer = "anyMatrikelnummer";
    String rolle = "anyRolle";
    String accessToken;
    if (resultCode == 1) {
      accessToken = "anySalt";
    } else {
      accessToken = "";
    }
    String[] expected = {("" + resultCode), studiengang, matrikelnummer, rolle, accessToken};

    // given (Mock Responses)
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    // 1. Mal true für Methode userLogin(), 2. Mal true für Methode userSessionExists() (nur für resultCode 1 relevant)
    when(resultSet.next()).thenReturn(true).thenReturn(true);
    when(resultSet.getString("studiengang")).thenReturn(studiengang);
    when(resultSet.getString("matrikelnummer")).thenReturn(matrikelnummer);
    when(resultSet.getString("rolle")).thenReturn(rolle);
    when(resultSet.getString("verifiziert")).thenReturn("" + resultCode);
    when(resultSet.getInt("userID")).thenReturn(357);
    utilMockedStatic.when(() -> Util.generateSalt()).thenReturn(accessToken);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    assertArrayEquals(expected, result);
    verify(connection, times(1))
            .prepareStatement("SELECT verifiziert, matrikelnummer, studiengang, rolle, userID FROM user WHERE email = ? AND passwort = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    String hashedPassword = Util.hashSha256(Util.hashSha256(TESTPW.toString()) + salt);
    verify(preparedStatement, times(1)).setString(anyInt(), eq(hashedPassword));
    if (resultCode == 1) {
      verify(connection, times(1))
              .prepareStatement("UPDATE loginSessions SET sessionID = ? WHERE userID = ?");
    }
  }

  @Test
  void userLoginWrongData() throws SQLException {
    // given
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    String[] expected = {"2", "", "", "", ""};
    assertArrayEquals(expected, result);
  }

  @Test
  void userLoginDbException() throws SQLException {
    // given
    when(preparedStatement.executeQuery()).thenReturn(null);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPW.toString());

    // then
    String[] expected = {"4", "", "", "", ""};
    assertArrayEquals(expected, result);
  }

  @Test
  void isEmailUsedWithUnusedEMail() throws SQLException {
    // given
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    // when
    boolean isUsed = SQLQueries.isEmailUsed(TESTEMAIL.toString());

    // then
    assertFalse(isUsed);
    verify(connection, times(1)).prepareStatement("SELECT 1 FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void isEmailUsedWithUsedEMail() throws SQLException {
    // given
    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    // when
    boolean isUsed = SQLQueries.isEmailUsed(TESTEMAIL.toString());

    // then
    assertTrue(isUsed);
    verify(connection, times(1)).prepareStatement("SELECT 1 FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void isMatnrUsedWithUnusedMatnr() throws SQLException {
    // given
    int matrikelnummer = Integer.parseInt(TESTMATRNR.toString());

    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);

    // when
    boolean isUsed = SQLQueries.isMatnrUsed(matrikelnummer);

    // then
    assertFalse(isUsed);
    verify(connection, times(1))
            .prepareStatement("SELECT 1 FROM user WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(matrikelnummer));
  }

  @Test
  void isMatnrUsedWithUsedMatnr() throws SQLException {
    // given
    int matrikelnummer = Integer.parseInt(TESTMATRNR.toString());

    ResultSet resultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    // when
    boolean isUsed = SQLQueries.isMatnrUsed(matrikelnummer);

    // then
    assertTrue(isUsed);
    verify(connection, times(1))
            .prepareStatement("SELECT 1 FROM user WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(matrikelnummer));
  }

  @Test
  void userRegister() throws SQLException {
    // when
    int i = SQLQueries.userRegister(TESTVNAME.toString(), TESTNNAME.toString(), TESTPW.toString(),
        Util.generateSalt(), 1, TESTEMAIL.toString(), TESTSTUGANG.toString(),
        TESTKURS.toString(), 1234567, TESTTELNR.toString(), TESTMOBILNR.toString(),
        TESTSTANDORT.toString(), "verifiziert");

    // then
    verify(preparedStatement, times(11)).setString(anyInt(), anyString());
    verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
    verify(preparedStatement, times(1)).executeUpdate();
    assertEquals(1, i);
  }
}

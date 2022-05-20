package dhbw.mwi.Auslandsemesterportal2016.test.db;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.db.User;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SQLQueriesTest {
  private Connection connection;
  private PreparedStatement preparedStatement;

  private ResultSet resultSet;
  private MockedStatic<DB> mockedStaticDb;

  @BeforeEach
  public void init() throws SQLException {
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);
    mockedStaticDb = mockStatic(DB.class);

    mockedStaticDb.when(DB::getInstance).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(preparedStatement.executeUpdate()).thenReturn(1);
  }

  @AfterEach
  public void close() throws SQLException {
    connection.close();
    preparedStatement.close();
    resultSet.close();
    mockedStaticDb.close();
  }

  @Test
  void getSalt() throws SQLException {
    // given
    String salt = "anySalt";
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
    when(resultSet.next()).thenThrow(RuntimeException.class);

    // when
    String result = SQLQueries.getSalt(TESTEMAIL.toString());

    // then
    assertEquals(salt, result);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void userLoginWithVerifiedUser(boolean userSessionExists) throws SQLException {
    // given (Data of resultStringArray)
    int resultCode = 1;
    String studiengang = "anyStudiengang";
    String matrikelnummer = "anyMatrikelnummer";
    String rolle = "anyRolle";
    String accessToken = "anySalt";
    String[] expected = {("" + resultCode), studiengang, matrikelnummer, rolle, accessToken};
    String salt = "SH5E9Z7P5J6Z5G2BV0";

    // given (Mock Responses)
    // testet createUserSession() durch unterschiedliche Ergebnisse in userSessionExists()
    if (userSessionExists) {
      when(resultSet.next()).thenReturn(true).thenReturn(true);
    } else {
      when(resultSet.next()).thenReturn(true).thenReturn(false);
    }
    when(resultSet.getString("studiengang")).thenReturn(studiengang);
    when(resultSet.getString("matrikelnummer")).thenReturn(matrikelnummer);
    when(resultSet.getString("rolle")).thenReturn(rolle);
    when(resultSet.getString("verifiziert")).thenReturn("" + resultCode);
    when(resultSet.getInt("userID")).thenReturn(357);
    MockedStatic<Util> utilMockedStatic = mockStatic(Util.class);
    utilMockedStatic.when(Util::generateSalt).thenReturn(accessToken);

    // when
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPASSWORT.toString());

    // then
    assertArrayEquals(expected, result);
    verify(connection, times(1))
            .prepareStatement("SELECT verifiziert, matrikelnummer, studiengang, rolle, userID FROM user WHERE email = ? AND passwort = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    String hashedPassword = Util.hashSha256(Util.hashSha256(TESTPASSWORT.toString()) + salt);
    verify(preparedStatement, times(1)).setString(anyInt(), eq(hashedPassword));
    if (userSessionExists) {
      verify(connection, times(1))
              .prepareStatement("UPDATE loginSessions SET sessionID = ? WHERE userID = ?");
    } else {
      verify(connection, times(1))
              .prepareStatement("INSERT INTO loginSessions (sessionID, userID) VALUES " + "(?,?)");
    }

    // finally
    utilMockedStatic.close();
  }

  @Test
  void userLoginWithUnverifiedUser() throws SQLException {
    // given (Data of resultStringArray)
    int resultCode = 3;
    String studiengang = "anyStudiengang";
    String matrikelnummer = "anyMatrikelnummer";
    String rolle = "anyRolle";
    String[] expected = {("" + resultCode), studiengang, matrikelnummer, rolle, ""};
    String salt = "SH5E9Z7P5J6Z5G2BV0";

    // given (Mock Responses)
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("studiengang")).thenReturn(studiengang);
    when(resultSet.getString("matrikelnummer")).thenReturn(matrikelnummer);
    when(resultSet.getString("rolle")).thenReturn(rolle);
    when(resultSet.getString("verifiziert")).thenReturn("" + resultCode);
    when(resultSet.getInt("userID")).thenReturn(357);

    // when
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPASSWORT.toString());

    // then
    assertArrayEquals(expected, result);
    verify(connection, times(1))
            .prepareStatement("SELECT verifiziert, matrikelnummer, studiengang, rolle, userID FROM user WHERE email = ? AND passwort = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    String hashedPassword = Util.hashSha256(Util.hashSha256(TESTPASSWORT.toString()) + salt);
    verify(preparedStatement, times(1)).setString(anyInt(), eq(hashedPassword));
  }

  @Test
  void userLoginWrongData() throws SQLException {
    // given
    when(resultSet.next()).thenReturn(false);

    // when
    String salt = "SH5E9Z7P5J6Z5G2BV0";
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPASSWORT.toString());

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
    String[] result = SQLQueries.userLogin(TESTEMAIL.toString(), salt, TESTPASSWORT.toString());

    // then
    String[] expected = {"4", "", "", "", ""};
    assertArrayEquals(expected, result);
  }

  @Test
  void isEmailUsedWithUnusedEMail() throws SQLException {
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
    int matrikelnummer = Integer.parseInt(TESTMATRIKELNUMMER.toString());

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
    int matrikelnummer = Integer.parseInt(TESTMATRIKELNUMMER.toString());
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
    int i = SQLQueries.userRegister(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTPASSWORT.toString(),
        Util.generateSalt(), 1, TESTEMAIL.toString(), TESTSTUDIENGANG.toString(),
        TESTKURS.toString(), Integer.parseInt(TESTMATRIKELNUMMER.toString()), TESTTELNR.toString(), TESTMOBILNR.toString(),
        TESTSTANDORT.toString(), "verifiziert");

    // then
    verify(preparedStatement, times(12)).setString(anyInt(), anyString());
    verify(preparedStatement, times(2)).setInt(anyInt(), anyInt());
    verify(preparedStatement, times(1)).executeUpdate();
    assertEquals(1, i);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void checkUserSession(boolean isValid) throws SQLException {
    // given
    String sessionId = "anySessionId";
    String mail = TESTEMAIL.toString();
    when(resultSet.next()).thenReturn(isValid);

    // when
    boolean actualResult = SQLQueries.checkUserSession(sessionId, mail);

    // then
    assertEquals(isValid, actualResult);
    verify(connection, times(1))
            .prepareStatement("SELECT loginSessions.sessionID, user.email FROM loginSessions,user WHERE user.userID = loginSessions.userID and user.email = ? and loginSessions.sessionID = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(sessionId));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(mail));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4})
  void getRoleForUsers(int expectedRole) throws SQLException {
    // given
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt(1)).thenReturn(expectedRole);

    // when
    int actualRole = SQLQueries.getRoleForUser(TESTEMAIL.toString());

    // then
    assertEquals(expectedRole, actualRole);
    verify(connection, times(1)).prepareStatement("SELECT rolle FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void getRoleForUserDoesNotExist() throws SQLException {
    // given
    when(resultSet.next()).thenReturn(false);

    // when
    int actualRole = SQLQueries.getRoleForUser(TESTEMAIL.toString());

    // then
    assertEquals(0, actualRole);
    verify(connection, times(1)).prepareStatement("SELECT rolle FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void getUserInfoOfExistingUser() throws SQLException {
    // given
    User expectedUser = new User();
    expectedUser.id = 753;
    expectedUser.rolle = 1;
    expectedUser.vorname = TESTVORNAME.toString();
    expectedUser.nachname = TESTNACHNAME.toString();
    expectedUser.matrikelnummer = TESTMATRIKELNUMMER.toString();

    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("userID")).thenReturn(753);
    when(resultSet.getInt("rolle")).thenReturn(1);
    when(resultSet.getString("vorname")).thenReturn(TESTVORNAME.toString());
    when(resultSet.getString("nachname")).thenReturn(TESTNACHNAME.toString());
    when(resultSet.getString("matrikelnummer")).thenReturn(TESTMATRIKELNUMMER.toString());

    // when
    User actualUser = SQLQueries.getUserInfo(TESTEMAIL.toString());

    // then
    assertEquals(expectedUser, actualUser);
    verify(connection, times(1)).prepareStatement("SELECT userID,rolle,vorname,nachname,matrikelnummer FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void getUserInfoNoSuchUser() throws SQLException {
    // given
    when(resultSet.next()).thenReturn(false);

    // when
    User actualUser = SQLQueries.getUserInfo(TESTEMAIL.toString());

    // then
    assertNull(actualUser);
    verify(connection, times(1)).prepareStatement("SELECT userID,rolle,vorname,nachname,matrikelnummer FROM user WHERE email = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void userLogout() throws SQLException {
    // when
    SQLQueries.userLogout(TESTSESSIONID.toString());

    // then
    verify(connection, times(1)).prepareStatement("DELETE FROM loginSessions WHERE sessionID = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSESSIONID.toString()));
  }

  @Test
  void updateUser() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    int result = SQLQueries.updateUser(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(), TESTSTUDIENGANG.toString(),
            TESTKURS.toString(), TESTMATRIKELNUMMER.toString(), TESTSTANDORT.toString());

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, studiengang = ?, kurs = ?, matrikelnummer = ?, standort = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTUDIENGANG.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTKURS.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTANDORT.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void updateUserWithNewMail() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    String newMail = "new@mail.de";
    int result = SQLQueries.updateUser(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(), TESTSTUDIENGANG.toString(),
            TESTKURS.toString(), TESTMATRIKELNUMMER.toString(), newMail, TESTSTANDORT.toString());

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, email = ?, studiengang = ?, kurs = ?, matrikelnummer = ?, standort = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTUDIENGANG.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTKURS.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTANDORT.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(newMail));
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void updateStudent() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    int result = SQLQueries.updateStud(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(),
            TESTSTANDORT.toString(), TESTSTUDIENGANG.toString(), TESTKURS.toString());

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, standort = ?, studiengang = ?, kurs = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTUDIENGANG.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTKURS.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTANDORT.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void updateStudentWithNewMail() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    String newMail = "new@mail.de";
    int result = SQLQueries.updateStud(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(),
            TESTSTANDORT.toString(), TESTSTUDIENGANG.toString(), TESTKURS.toString(), newMail);

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, email = ?, standort = ?, studiengang = ?, kurs = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTUDIENGANG.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTKURS.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTSTANDORT.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(newMail));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
  }

  @Test
  void updateMA() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    int result = SQLQueries.updateMA(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(),
            TESTTELNR.toString(), TESTMOBILNR.toString());

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, tel = ?, mobil = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTTELNR.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTMOBILNR.toString()));
  }

  @Test
  void updateMAWithNewMail() throws SQLException {
    // no Test for Failure because returned int is mocked anyway. Interpretation of value is done by servlet
    // when
    String newMail = "new@mail.de";
    int result = SQLQueries.updateMA(TESTVORNAME.toString(), TESTNACHNAME.toString(), TESTEMAIL.toString(),
            TESTTELNR.toString(), TESTMOBILNR.toString(), newMail);

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET vorname = ?, nachname = ?, email = ?, tel = ?, mobil = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTVORNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTNACHNAME.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTTELNR.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTMOBILNR.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(newMail));
  }

  @Test
  void getProcessModelJson() throws SQLException {
    // given
    String step = "anyStep";
    String model = "anyModel";

    // when
    ResultSet processModelJson = SQLQueries.getProcessModelJson(step, model);

    // then
    assertEquals(resultSet, processModelJson);
    verify(connection, times(1)).prepareStatement("SELECT json FROM processModel WHERE step = ? AND model = ?;");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(step));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(model));
  }

  @Test
  void getInstanceIdReturnsInstance() throws SQLException {
    // given
    String uni = "California State University San Marcos (USA)";
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("processInstance")).thenReturn("anyProcessInstance");

    // when
    String processInstance = SQLQueries.getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), uni);

    // then
    assertNotEquals("", processInstance);
    verify(connection, times(1))
            .prepareStatement("SELECT processInstance FROM MapUserInstanz WHERE matrikelnummer = ? AND uni = ?");
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(uni));
  }

  @Test
  void getInstanceNoSuchInstance() throws SQLException {
    // given
    String uni = "California State University San Marcos (USA)";
    when(resultSet.next()).thenReturn(false);

    // when
    String processInstance = SQLQueries.getInstanceId(Integer.parseInt(TESTMATRIKELNUMMER.toString()), uni);

    // then
    assertEquals("", processInstance);
    verify(connection, times(1))
            .prepareStatement("SELECT processInstance FROM MapUserInstanz WHERE matrikelnummer = ? AND uni = ?");
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(uni));
  }

  @Test
  void createInstance() throws SQLException {
    // given
    String uni = "California State University San Marcos (USA)";
    int prio = 1;
    int stepCount = 2;
    Date date = Date.valueOf(LocalDate.now().toString());

    // when
    SQLQueries.createInstance(TESTINSTANCEID.toString(), uni, Integer.parseInt(TESTMATRIKELNUMMER.toString()), prio, stepCount);

    // then
    verify(connection, times(1))
            .prepareStatement("INSERT INTO MapUserInstanz (matrikelnummer, uni, processInstance, status, prioritaet) VALUES (?,?,?,?,?)");
    verify(connection, times(1))
            .prepareStatement("INSERT INTO bewerbungsprozess (matrikelnummer, uniName, startDatum, Schritte_aktuell, Schritte_gesamt) VALUES (?,?,?,?,?)");
    verify(preparedStatement, times(2)).setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
    verify(preparedStatement, times(2)).setString(anyInt(), eq(uni));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTINSTANCEID.toString()));
    verify(preparedStatement, times(1)).setString(anyInt(), eq("1"));
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(prio));
    verify(preparedStatement, times(1)).setDate(anyInt(), eq(date));
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(0));
    verify(preparedStatement, times(1)).setInt(anyInt(), eq(stepCount));
  }

  @Test
  void getAllActivitiesWithOneActivity() throws SQLException {
    // given
    String expectedActivities = "anyActivity;";
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("step")).thenReturn("anyActivity");

    // when
    String allActivities = SQLQueries.getAllActivities("anyKey");

    // then
    assertEquals(expectedActivities, allActivities);
    verify(connection, times(1))
            .prepareStatement("SELECT `step` FROM `processModel` WHERE `model` = ? ORDER BY `stepNumber`");
    verify(preparedStatement, times(1)).setString(anyInt(), eq("anyKey"));
  }

  @Test
  void getAllActivitiesWithMultipleActivities() throws SQLException {
    // given
    String expectedActivities = "anyActivity;anyActivity;";
    when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(resultSet.getString("step")).thenReturn("anyActivity");

    // when
    String allActivities = SQLQueries.getAllActivities("anyKey");

    // then
    assertEquals(expectedActivities, allActivities);
    verify(connection, times(1))
            .prepareStatement("SELECT `step` FROM `processModel` WHERE `model` = ? ORDER BY `stepNumber`");
    verify(preparedStatement, times(1)).setString(anyInt(), eq("anyKey"));
  }

  @Test
  void getAllActivitiesWithoutActivities() throws SQLException {
    // given
    when(resultSet.next()).thenReturn(false);

    // when
    String allActivities = SQLQueries.getAllActivities("anyKey");

    // then
    assertEquals("", allActivities);
    verify(connection, times(1))
            .prepareStatement("SELECT `step` FROM `processModel` WHERE `model` = ? ORDER BY `stepNumber`");
    verify(preparedStatement, times(1)).setString(anyInt(), eq("anyKey"));
  }

  @Test
  void getUserDataForExistingMatrikelnummer() throws SQLException {
    // given
    String standortWith_ = "Karlsruhe_";
    String standortWithout_ = "Karlsruhe ";
    String[] expected = new String[] {TESTNACHNAME.toString(), TESTVORNAME.toString(), TESTEMAIL.toString(),
            standortWithout_, TESTSTUDIENGANG.toString(), TESTKURS.toString()};
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("nachname")).thenReturn(TESTNACHNAME.toString());
    when(resultSet.getString("vorname")).thenReturn(TESTVORNAME.toString());
    when(resultSet.getString("email")).thenReturn(TESTEMAIL.toString());
    when(resultSet.getString("standort")).thenReturn(standortWith_);
    when(resultSet.getString("studiengang")).thenReturn(TESTSTUDIENGANG.toString());
    when(resultSet.getString("kurs")).thenReturn(TESTKURS.toString());

    // when
    String[] actualUserData = SQLQueries.getUserData(Integer.parseInt(TESTMATRIKELNUMMER.toString()));

    // then
    assertArrayEquals(expected, actualUserData);
    verify(connection, times(1))
            .prepareStatement("SELECT nachname,vorname,email,standort,studiengang,kurs FROM user WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1))
            .setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void getUserDataOfInvalidMatrikelnummer() throws SQLException {
    // given
    when(resultSet.next()).thenReturn(false);

    // when
    String[] actualUserData = SQLQueries.getUserData(Integer.parseInt(TESTMATRIKELNUMMER.toString()));

    // then
    assertArrayEquals(new String[0], actualUserData);
    verify(connection, times(1))
            .prepareStatement("SELECT nachname,vorname,email,standort,studiengang,kurs FROM user WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1))
            .setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void getModelOfExistingUni() throws SQLException {
    // given
    String expectedModel = "anyModel";
    String uni = "California State University San Marcos (USA)";
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getString("model")).thenReturn(expectedModel);

    // when
    String actualModel = SQLQueries.getModel(uni);

    // then
    assertEquals(expectedModel, actualModel);
    verify(connection, times(1))
            .prepareStatement("SELECT model FROM cms_auslandsAngeboteInhalt WHERE uniTitel=?");
    verify(preparedStatement, times(1))
            .setString(anyInt(), eq(uni));
  }

  @Test
  void getModelOfInvalidUni() throws SQLException {
    // given
    String uni = "California State University San Marcos (USA)";
    when(resultSet.next()).thenReturn(false);

    // when
    String actualModel = SQLQueries.getModel(uni);

    // then
    assertEquals("", actualModel);
    verify(connection, times(1))
            .prepareStatement("SELECT model FROM cms_auslandsAngeboteInhalt WHERE uniTitel=?");
    verify(preparedStatement, times(1))
            .setString(anyInt(), eq(uni));
  }

  @Test
  void getUserInstancesWithOneInstance() throws SQLException {
    // given
    String uni = "anyUni";
    String instance = "anyInstance";
    String prio = "1";
    ArrayList<String[]> expectedUserInstances = new ArrayList<>();
    expectedUserInstances.add(new String[]{uni, instance, prio});
    when(resultSet.next()).thenReturn(true).thenReturn(false);
    when(resultSet.getString("uni")).thenReturn(uni);
    when(resultSet.getString("processInstance")).thenReturn(instance);
    when(resultSet.getString("prioritaet")).thenReturn(prio);

    // when
    ArrayList<String[]> actualUserInstances = SQLQueries.getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString()));

    // then
    for (int i=0; i<3;i++) {
      System.out.println(expectedUserInstances.get(0)[i]);
      System.out.println(actualUserInstances.get(0)[i]);
    }
    assertArrayEquals(expectedUserInstances.get(0), actualUserInstances.get(0));
    verify(connection, times(1))
            .prepareStatement("SELECT uni,processInstance,prioritaet FROM MapUserInstanz WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1))
            .setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void getUserInstancesWithMultipleInstances() throws SQLException {
    // given
    String uni = "anyUni";
    String instance = "anyInstance";
    String prio = "1";
    ArrayList<String[]> expectedUserInstances = new ArrayList<>();
    expectedUserInstances.add(new String[]{uni, instance, prio});
    expectedUserInstances.add(new String[]{uni, instance, prio});
    when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(resultSet.getString("uni")).thenReturn(uni);
    when(resultSet.getString("processInstance")).thenReturn(instance);
    when(resultSet.getString("prioritaet")).thenReturn(prio);

    // when
    ArrayList<String[]> actualUserInstances = SQLQueries.getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString()));

    // then
    assertArrayEquals(expectedUserInstances.get(0), actualUserInstances.get(0));
    assertArrayEquals(expectedUserInstances.get(1), actualUserInstances.get(1));
    verify(connection, times(1))
            .prepareStatement("SELECT uni,processInstance,prioritaet FROM MapUserInstanz WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1))
            .setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void getUserInstancesWithoutInstances() throws SQLException {
    // given
    String uni = "anyUni";
    String instance = "anyInstance";
    String prio = "1";
    ArrayList<String[]> expectedUserInstances = new ArrayList<>();
    when(resultSet.next()).thenReturn(false);

    // when
    ArrayList<String[]> actualUserInstances = SQLQueries.getUserInstances(Integer.parseInt(TESTMATRIKELNUMMER.toString()));

    // then
    assertEquals(expectedUserInstances, actualUserInstances);
    verify(connection, times(1))
            .prepareStatement("SELECT uni,processInstance,prioritaet FROM MapUserInstanz WHERE matrikelnummer = ?;");
    verify(preparedStatement, times(1))
            .setInt(anyInt(), eq(Integer.parseInt(TESTMATRIKELNUMMER.toString())));
  }

  @Test
  void getStepCounterOfExistingModelAndStep() throws SQLException {
    // given
    String expected = "Schritt 17 von 42";
    String step = "anyStep";
    String model = "anyModel";
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getInt("stepNumber")).thenReturn(17);
    when(resultSet.getInt("max(stepNumber)")).thenReturn(42);

    // when
    String actual = SQLQueries.getStepCounter(step, model);

    // then
    assertEquals(expected, actual);
    verify(connection, times(1))
            .prepareStatement("SELECT stepNumber FROM processModel WHERE model = ? AND step = ?");
    verify(connection, times(1))
            .prepareStatement("SELECT max(stepNumber) FROM processModel WHERE model = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(step));
    verify(preparedStatement, times(2)).setString(anyInt(), eq(model));
  }

  @Test
  void getStepCounterOfInvalidModelAndStep() throws SQLException {
    // given
    String expected = "Fehler";
    String step = "anyStep";
    String model = "anyModel";
    when(resultSet.next()).thenReturn(false);

    // when
    String actual = SQLQueries.getStepCounter(step, model);

    // then
    assertEquals(expected, actual);
    verify(connection, times(1))
            .prepareStatement("SELECT stepNumber FROM processModel WHERE model = ? AND step = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(step));
    verify(preparedStatement, times(1)).setString(anyInt(), eq(model));
  }

  @Test
  void forgetPassword() throws SQLException {
    // when
    String uuid = SQLQueries.forgetPassword(TESTEMAIL.toString());

    // then
    assertNotNull(uuid);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET resetToken = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(2)).setString(anyInt(), anyString());
  }

  @Test
  void deactivateUser() throws SQLException {
    // when
    String uuid = SQLQueries.deactivateUser(TESTEMAIL.toString());

    // then
    assertNotNull(uuid);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET verifiziert = ? WHERE email = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(TESTEMAIL.toString()));
    verify(preparedStatement, times(2)).setString(anyInt(), anyString());
  }

  @Test
  void setPassword() throws SQLException {
    // given
    String uuid = "anyUUID";

    // when
    int result = SQLQueries.setPassword(uuid, TESTPASSWORT.toString());

    // then
    assertEquals(1, result);
    verify(connection, times(1))
            .prepareStatement("UPDATE user SET passwort = ?, salt = ?, resetToken = ''  WHERE resetToken = ?");
    verify(preparedStatement, times(1)).setString(anyInt(), eq(uuid));
    verify(preparedStatement, times(3)).setString(anyInt(), anyString());
  }
}

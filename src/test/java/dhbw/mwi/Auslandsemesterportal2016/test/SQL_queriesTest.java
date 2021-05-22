package dhbw.mwi.Auslandsemesterportal2016.test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class SQL_queriesTest {

  /*
   * Testing static getSalt()-Method using Mockito mocking result-response Method
   * is called in LoginServlet-Class
   */
  @Test
  public void testGetSalt() {
    try (MockedStatic<SQL_queries> mockedStatic = Mockito.mockStatic(SQL_queries.class)) {

      mockedStatic.when(() -> SQL_queries.getSalt("testusermwi@dhbw.de")).thenReturn("SH5E9Z7P5J6Z5G2BV0");

      String result = SQL_queries.getSalt("testusermwi@dhbw.de");

      assertEquals("SH5E9Z7P5J6Z5G2BV0", result);
    }
  }

  /*
   * Testing static userLogin()-Method using Mockito mocking result-response
   * Method is called in LoginServlet-Class
   */
  @Test
  public void testUserLogin() {
    String[] stringArr = { "1", "Wirtschaftsinformatik", "12345678", "Admin", "1478523697412" };

    try (MockedStatic<SQL_queries> mockedStatic = Mockito.mockStatic(SQL_queries.class)) {

      mockedStatic.when(() -> SQL_queries.userLogin("testusermwi@dhbw.de", "SH5E9Z7P5J6Z5G2BV0", "Password1234"))
          .thenReturn(stringArr);

      String[] result = SQL_queries.userLogin("testusermwi@dhbw.de", "SH5E9Z7P5J6Z5G2BV0", "Password1234");

      assertArrayEquals(stringArr, result);
    }
  }

}

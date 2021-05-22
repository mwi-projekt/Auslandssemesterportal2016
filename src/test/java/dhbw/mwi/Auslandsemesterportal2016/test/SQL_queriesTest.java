package dhbw.mwi.Auslandsemesterportal2016.test;

import java.sql.ResultSet;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SQL_queriesTest {

    @Test
    public void testGetSalt() {
        try (MockedStatic<SQL_queries> mockedStatic = Mockito.mockStatic(SQL_queries.class)) {
 
            mockedStatic
              .when(() -> SQL_queries.getSalt("testusermwi@dhbw.de"))
              .thenReturn("SH5E9Z7P5J6Z5G2BV0");
       
            String result = SQL_queries.getSalt("testusermwi@dhbw.de");
       
            assertEquals("SH5E9Z7P5J6Z5G2BV0", result);
          }
        
    }
    
    @Test
    public void testUserLogin() {
        
    }
    
}

package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mock;
import org.testng.annotations.Test;
import org.mockito.MockedStatic;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

public class LoginTest {
    /*
     * //Test-User Studiengangsleiter private static String testSgl_correctMail =
     * "test@sgl.dhbw-karlsruhe.de"; private static String testSgl_correctPw =
     * "Hallo1234!";
     * 
     * //Test-User AAA-Mitarbeiter private static String testAAA_correctMail =
     * "auslandsamt@dh-karlsruhe.de"; private static String testAAA_correctPw =
     * "AuslandsMa1234!";
     * 
     * //Test-User Admin private static String testAdmin_correctMail =
     * "admin@admin.de"; private static String testAdmin_correctPw = "admin";
     */

    // Test-User Student
    private static String testUser_correctMail = "testusermwi@dhbw.de";
    private static String testUser_correctPw = "Password1234";
    private static String testUser_falseMail = "test.user.mwi@dhbw.de";
    private static String testUser_falsePw = "Passw0rd1234";

    @Mock
    SQL_queries sql_queries;

    @Test
    public void successfulLogin() {
        // HttpServletRequest request = mock(HttpServletRequest.class);
        // HttpServletResponse response = mock(HttpServletResponse.class);
        String salt = SQL_queries.getSalt(testUser_correctMail);

        String[] result = SQL_queries.userLogin(testUser_correctMail, salt, testUser_correctPw);

        /*
         * JsonObject json = new JsonObject(); json.addProperty("resultCode",
         * result[0]); json.addProperty("studiengang", result[1]);
         * json.addProperty("matrikelnummer", result[2]); json.addProperty("rolle",
         * result[3]); Util.writeJson(response, json);
         */

        assertEquals(1, result[0]);
    }

    // @Test
    // public void failedLoginBecauseOfFalseMail() {
    // String salt = SQL_queries.getSalt(testUser_falseMail);
    // String[] result = SQL_queries.userLogin(testUser_falseMail, salt,
    // testUser_correctPw);

    // assertEquals(1, result[0]);
    // }

    // @Test
    // public void failedLoginBecauseOfFalsePassword() {
    // String salt = SQL_queries.getSalt(testUser_correctMail);
    // String[] result = SQL_queries.userLogin(testUser_correctMail, salt,
    // testUser_falsePw);

    // assertEquals(1, result[0]);
    // }

    // @Test
    // public void failedLogin() {
    // String salt = SQL_queries.getSalt(testUser_falseMail);
    // String[] result = SQL_queries.userLogin(testUser_falseMail, salt,
    // testUser_falsePw);

    // assertEquals(1, result[0]);
    // }
}

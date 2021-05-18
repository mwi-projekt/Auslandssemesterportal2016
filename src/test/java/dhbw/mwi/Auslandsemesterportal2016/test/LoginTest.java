package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.User;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

public class LoginTest {

    private static String testUser_mail = "testusermwi@dhbw.de";
    private static String testUser_pw = "Password1234";
    private static int testUser_id = 368;
    private static int testUser_rolle = 3;

    @Test
    public void successfulLogin() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String salt = SQL_queries.getSalt(testUser_mail);

        String[] result = SQL_queries.userLogin(testUser_mail, salt, testUser_pw);

        JsonObject json = new JsonObject();
        json.addProperty("resultCode", result[0]);
        json.addProperty("studiengang", result[1]);
        json.addProperty("matrikelnummer", result[2]);
        json.addProperty("rolle", result[3]);
        Util.writeJson(response, json);

        assertEquals(1, result[0]);
    }
}

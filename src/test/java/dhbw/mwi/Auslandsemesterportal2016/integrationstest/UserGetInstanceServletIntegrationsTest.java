package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserGetInstanceServletIntegrationsTest {
    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM");
        String sessionID = loginResponse.getCookies().get("sessionID");

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45/getUserInstances?email=test@student.dhbw.karlsruhe.de&pw=7sdfyxc/fsdASDFM&matnr=1901901")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        assertNotNull(response);
        assertNotNull(new JsonParser().parse(response).getAsJsonArray());
    }

}

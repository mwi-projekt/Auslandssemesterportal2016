package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetSGLTasksServletIntegrationsTest {
    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@sgl.dhbw-karlsruhe.de&pw=8(sdf2aDJiGl,/")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@sgl.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45/getSGLTasks")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        assertNotNull(json);
        assertNotNull(json.get("data").getAsJsonArray());
        assertTrue(response.contains("complete"));
    }
}
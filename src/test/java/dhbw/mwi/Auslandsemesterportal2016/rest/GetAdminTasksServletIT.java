package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetAdminTasksServletIT {
    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=admin@admin.de&pw=khjfjKDdsf254s#!")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "admin@admin.de")
                .when()
                .get("http://10.3.15.45/getTasks")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        assertNotNull(json);
        assertNotNull(json.get("data").getAsJsonArray());
        assertTrue(response.contains("complete"));
    }
}
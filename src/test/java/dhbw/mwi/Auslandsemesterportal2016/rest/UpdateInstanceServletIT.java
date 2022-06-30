package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateInstanceServletIT {
    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String getInstanceResponse = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matnr", "190190190")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        JsonObject getInstanceResponseAsJson = JsonParser.parseString(getInstanceResponse).getAsJsonObject();
        String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

        Map<String, Object> requestBody = getRequestBody(instanceId);
        String returnedResponse = given()
                .log().all()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParams(requestBody)
                .when()
                .post("http://10.3.15.45/setVariable")
                .peek()
                .then().statusCode(200)
                .extract().response().asString();

        String expectedResponse = "Saved";
        assertEquals(expectedResponse ,returnedResponse.trim());

        // Test-Instanz wegräumen
        String returnedId = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matrikelnummer", "190190190")
                .queryParam("uni","California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/process/delete")
                .then().statusCode(200)
                .extract().response().asString();
        assertEquals(instanceId ,returnedId.trim());
    }

    private Map<String, Object> getRequestBody(String instanceId) {
        Map<String, Object> body = new HashMap<>();
        body.put("instance_id", instanceId);
        body.put("key", "gdprCompliance|uni|semesteradresseAnders");
        body.put("value", "true|Standard|false");
        body.put("type", "boolean|text|boolean");
        return body;
    }
}
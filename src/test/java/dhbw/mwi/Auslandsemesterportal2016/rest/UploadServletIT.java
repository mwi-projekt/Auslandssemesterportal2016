package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UploadServletIT {
    @Test
    void doPostSuccess() throws IOException {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String getInstanceResponse = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matnr", "1901901")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        JsonObject getInstanceResponseAsJson = JsonParser.parseString(getInstanceResponse).getAsJsonObject();
        String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

        Map<String, Object> map = getMap(instanceId);
        File testFile = getTestFile();

        given()
                .multiPart(testFile)
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .formParams(map)
                .when()
                .post("http://10.3.15.45/upload")
                .then().statusCode(200);
    }

    private File getTestFile() throws IOException {
        File testFile = new File("Test.pdf");
        List<String> input = Arrays.asList("Test", "Test");
        Files.write(testFile.toPath(),input);
        return testFile;
    }

    private Map<String, Object> getMap(String instanceId) {
        Map<String, Object> map = new HashMap<>();
        map.put("instance", instanceId);
        map.put("action", "daadHochladen");
        return map;
    }

    @Test
    void doOptionsSuccess(){
        String response = given()
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .options("http://10.3.15.45/upload")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals("{}", response);
    }
}
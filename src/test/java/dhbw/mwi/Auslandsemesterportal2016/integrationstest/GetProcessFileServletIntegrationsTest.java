package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
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
import static org.junit.jupiter.api.Assertions.*;

class GetProcessFileServletIntegrationsTest {
    @Test
    void doHeadNotFound() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200).extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String getInstanceResponse = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matnr", "1901901")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200).contentType(ContentType.JSON).extract().response().asString();

        JsonObject getInstanceResponseAsJson = JsonParser.parseString(getInstanceResponse).getAsJsonObject();
        String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

        given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("instance_id", instanceId)
                .queryParam("key","daadFormularEnglisch")
                .when()
                .head("http://10.3.15.45/getProcessFile")
                .then().statusCode(404);
    }
    @Test
    void doGetSuccess() throws IOException {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200).extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String getInstanceResponse = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matnr", "1901901")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200).contentType(ContentType.JSON).extract().response().asString();

        JsonObject getInstanceResponseAsJson = JsonParser.parseString(getInstanceResponse).getAsJsonObject();
        String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

        File testFile = new File("Test.pdf");
        List<String> input = Arrays.asList("Test", "Test");
        Files.write(testFile.toPath(),input);

        Map<String, Object> map = new HashMap<>();
        map.put("instance", instanceId);
        map.put("action", "daadFormularEnglisch");

        given()
                .multiPart(testFile)
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .formParams(map)
                .when()
                .post("http://10.3.15.45/upload")
                .then().statusCode(200);

        Response response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("instance_id", instanceId)
                .queryParam("key","daadFormularEnglisch")
                .when()
                .get("http://10.3.15.45/getProcessFile")
                .then().statusCode(200)
                .extract().response();

        assertEquals("",response.contentType());
    }
}
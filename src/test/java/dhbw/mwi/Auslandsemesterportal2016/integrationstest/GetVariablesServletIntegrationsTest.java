package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetVariablesServletIntegrationsTest {
    @Test
    void doGetSuccess() {
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

        String returnedVariables = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("instance_id", instanceId)
                .when()
                .get("http://10.3.15.45/getVariables")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        String expectedVariables = "{\"bewEmail\":\"test@student.dhbw-karlsruhe.de\",\"uni\":\"California State University San Marcos (USA)\",\"aktuelleUni\":\"DHBW Karlsruhe\",\"bewKurs\":\"WWI12B5\",\"bewVorname\":\"Test\",\"matrikelnummer\":\"1901901\",\"prioritaet\":\"2\",\"bewNachname\":\"Student\",\"bewStudiengang\":\"Wirtschaftsinformatik\"}";
        assertEquals(expectedVariables ,returnedVariables.trim());
    }
}
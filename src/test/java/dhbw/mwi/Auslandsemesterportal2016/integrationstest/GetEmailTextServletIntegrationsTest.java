package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

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

class GetEmailTextServletIntegrationsTest {

    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@sgl.dhbw-karlsruhe.de&pw=8(sdf2aDJiGl,/");
        String sessionID = loginResponse.getCookies().get("sessionID");
        // instance besorgen
        String getInstanceResponse = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@sgl.dhbw-karlsruhe.de")
                .queryParam("matnr", "190190190")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200).contentType(ContentType.JSON).extract().response().asString();

        JsonObject getInstanceResponseAsJson = new JsonParser().parse(getInstanceResponse).getAsJsonObject();
        String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

        // notwendige Params setzen
        Map<String, String> requestBody = getRequestBody(instanceId);

        given()
                .log().all()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@sgl.dhbw-karlsruhe.de")
                .formParams(requestBody)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .when()
                .post("http://10.3.15.45/setVariable")
                .peek()
                .then().statusCode(200);

        // Test-Methode aufrufen
        String content = given()
                .log().all()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@sgl.dhbw-karlsruhe.de")
                .queryParam("instance_id", instanceId)
                .queryParam("validate", "true")
                .when()
                .get("http://10.3.15.45/getMailText")
                .peek()
                .then().statusCode(200).extract().response().asString();

        String expectedContent = getExpectedContentSuccess();
        assertEquals(expectedContent, content);

        // Test-Instanz wegräumen
        String returnedId = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@sgl.dhbw-karlsruhe.de")
                .queryParam("matrikelnummer", "190190190")
                .queryParam("uni","California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/process/delete")
                .then().statusCode(200)
                .extract().response().asString();
        assertEquals(instanceId ,returnedId.trim());
    }

    private Map<String, String> getRequestBody(String instanceId) {
        Map<String, String> requestBody = new HashMap<>();
        // FIXME Wo ist der TestStudent hin?!
        String keys = "gdprCompliance|uni|semesteradresseAnders|uni1|uni2|uni3|validierungErfolgreich|muttersprache|englischNote|bewNachname";
        String values = "true|Standard|false|Abertay University of Dundee (Schottland)|California State University San Marcos (USA)|South-Eastern Finland University of Applied Sciences (Finnland)|true||13|Student";
        String types = "boolean|text|boolean|text|text|text|boolean|text|text|text";
        requestBody.put("instance_id", instanceId);
        requestBody.put("key", keys);
        requestBody.put("value", values);
        requestBody.put("type", types);
        return requestBody;
    }

    private String getExpectedContentSuccess() {
        String expectedContent = "Sehr geehrte/r Herr/Frau Student,\n" +
                "\n" +
                "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot für die Universitäten: \n" +
                "- Abertay University of Dundee (Schottland)\n" +
                "- California State University San Marcos (USA)\n" +
                "- South-Eastern Finland University of Applied Sciences (Finnland)\n" +
                "wurde erfolgreich durch ihre/n Studiengangsleiter/in validiert.\n" +
                "\n" +
                "-- Platzhalter für Anmerkungen des Studiengangsleiters --\n" +
                "\n" +
                "\n" +
                "Im nächsten Schritt wird ihre Bewerbung an ein/e Mitarbeiter/in des Akademischen Auslandsamtes für einen weiteren Validierungsprozess übergeben.\n" +
                "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren. Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de.\n" +
                "\n" +
                "Mit freundlichen Grüßen,\n" +
                "\n" +
                "Ihr Studiengangsleiter/in\n" +
                "\n" +
                "************\n" +
                "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
        return expectedContent;
    }
}
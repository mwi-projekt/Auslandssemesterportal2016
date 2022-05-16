package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGetInstanceServletIntegrationsTest {
    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=Hallo1234!");
        String sessionID = loginResponse.getCookies().get("sessionID");
        String expected = "{\"data\":[{\"instanceID\":\"24850710-d515-11ec-98d5-0242c0a82003\",\"uni\":\"Standard\",\"stepCounter\":\"Schritt 2 von 8\",\"prioritaet\":\"0\"},{\"instanceID\":\"cd747fc6-d51b-11ec-98d5-0242c0a82003\",\"uni\":\"California State University San Marcos (USA)\",\"stepCounter\":\"Schritt 1 von 8\",\"prioritaet\":\"1\"}]}";

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45/getUserInstances?email=test@student.dhbw.karlsruhe.de&pw=Hallo1234!&matnr=1901901")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        assertEquals(expected, response);
    }

}

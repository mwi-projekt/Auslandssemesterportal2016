package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetInstanceServletIntegrationsTest {

    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=Hallo1234!");
        String sessionID = loginResponse.getCookies().get("sessionID");
        String expected = "{\"instanceId\":\"cd747fc6-d51b-11ec-98d5-0242c0a82003\",\"uni\":\"California State University San Marcos (USA)\"}";

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45//getInstance?matnr=1901901&uni=California State University San Marcos (USA)&prio=1")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        assertEquals(expected, response);
    }

}

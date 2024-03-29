package dhbw.mwi.Auslandsemesterportal2016.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetInstanceServletIT {

    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        String response = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45/getInstance?matnr=1901901&uni=California State University San Marcos (USA)&prio=1")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        JsonObject responseAsJsonObject = JsonParser.parseString(response).getAsJsonObject();
        assertEquals("\"California State University San Marcos (USA)\"", responseAsJsonObject.get("uni").toString());
    }

}

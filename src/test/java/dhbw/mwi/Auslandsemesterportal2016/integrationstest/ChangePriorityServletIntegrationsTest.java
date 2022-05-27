package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

public class ChangePriorityServletIntegrationsTest {

        @Test
        void doGetSuccess() {
            Response loginResponse = post("http://localhost:80/login?email=test@student.dhbw-karlsruhe.de&pw=Hallo1234!");
            String sessionID = loginResponse.getCookies().get("sessionID");

            String getInstanceResponse = given()
                    .cookie("sessionID", sessionID)
                    .cookie("email", "test@student.dhbw-karlsruhe.de")
                    .queryParam("matnr", "190190190")
                    .queryParam("prio", "2")
                    .queryParam("uni", "California State University San Marcos (USA)")
                    .when()
                    .get("http://localhost:80/getInstance")
                    .then().statusCode(200).contentType(ContentType.JSON).extract().response().asString();

            JsonObject getInstanceResponseAsJson = new JsonParser().parse(getInstanceResponse).getAsJsonObject();
            String instanceId = getInstanceResponseAsJson.get("instanceId").toString().replace('\"', ' ').trim();

            given()
                    .cookie("sessionID", sessionID)
                    .cookie("email", "test@student.dhbw-karlsruhe.de")
                    .queryParam("instance", instanceId)
                    .queryParam("prio", "2")
                    .when()
                    .get("http://localhost:80/changePriority")
                    .then().statusCode(200);
        }
    }


package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangePriorityServletIntegrationsTest {
        @Test
        void doGetSuccess(){
            Response loginResponse = post("http://localhost:80/login?email=test@student.dhbw-karlsruhe.de&pw=Hallo1234!");
            String sessionID = loginResponse.getCookies().get("sessionID");



            given()
                    .cookie("sessionID", sessionID)
                    .cookie("email", "test@student.dhbw-karlsruhe.de")
                    .queryParam("instance","xyz")
                    .queryParam("prio","1")
                    .when()
                    .get("http://localhost:80/changePriority")
                    .then().statusCode(200);
        }
    }


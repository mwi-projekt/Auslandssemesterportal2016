package dhbw.mwi.Auslandsemesterportal2016.integrationstest;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;

class LogoutServletIntegrationsTest {

    @Test
    void doGetFailed() {
        post("http://10.3.15.45/logout").then().statusCode(400);
    }

    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .when()
                .get("http://10.3.15.45/logout")
                .then().statusCode(200)
                .cookie("email", equalTo(""))
                .cookie("sessionID", equalTo(""));
    }
}

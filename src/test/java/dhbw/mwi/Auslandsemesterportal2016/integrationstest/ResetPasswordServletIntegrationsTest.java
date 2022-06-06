package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import dhbw.mwi.Auslandsemesterportal2016.enums.SuccessEnum;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordServletIntegrationsTest {
    @Test
    void doPostSuccess() {
        String testEMail = "test@student.dhbw-karlsruhe.de";
        String response = given()
                .queryParam("email", testEMail)
                .when()
                .post("http://10.3.15.45/resetPassword")
                .then().statusCode(200)
                .extract().response().asString();
        assertEquals(SuccessEnum.RESETACC + testEMail, response);
    }

    @Test
    void doPostUnknownUser() {
        String testEMail = "xyz";
        String response = given()
                .queryParam("email", testEMail)
                .when()
                .post("http://10.3.15.45/resetPassword")
                .then().statusCode(500)
                .extract().response().asString();
        assertEquals("No account registered for this email adress", response);
    }
}
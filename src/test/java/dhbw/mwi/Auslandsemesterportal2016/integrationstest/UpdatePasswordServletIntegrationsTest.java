package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class UpdatePasswordServletIntegrationsTest {
    @Test
    void doGetSuccess() {
        String uuid = SQLQueries.forgetPassword("test@student.dhbw-karlsruhe.de");


        String response = given()
                .queryParam("uuid",uuid)
                .queryParam("password", "Hallo1234!")
                .when()
                .get("http://10.3.15.45/updatePassword")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals(1, response);
    }
}
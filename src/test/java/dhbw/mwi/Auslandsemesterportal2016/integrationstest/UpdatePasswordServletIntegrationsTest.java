package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdatePasswordServletIntegrationsTest {
    @Disabled("UUID kann nicht gefaked werden")
    @Test
    void doGetSuccess() {
        String uuid = SQLQueries.forgetPassword("test@student.dhbw-karlsruhe.de");

        String response = given()
                .queryParam("uuid",uuid)
                .queryParam("password", "7sdfyxc/fsdASDFM")
                .when()
                .get("http://10.3.15.45/updatePassword")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals("1", response);
    }
}
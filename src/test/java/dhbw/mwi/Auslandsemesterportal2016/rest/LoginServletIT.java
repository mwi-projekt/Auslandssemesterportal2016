package dhbw.mwi.Auslandsemesterportal2016.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginServletIT {

    @Test
    void doPostSuccess() {
        String expected = "{\"resultCode\":\"1\",\"studiengang\":\"Wirtschaftsinformatik\",\"matrikelnummer\":\"1901901\",\"rolle\":\"3\"}";
        String response = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }
    @Test
    void doPostFailed(){
        //Passwort falsch
        String expected = "{\"resultCode\":\"2\",\"studiengang\":\"\",\"matrikelnummer\":\"\",\"rolle\":\"\"}";
        String response = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=falschesPasswort")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }
}

package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LoginServletIntegrationsTest {

    @Test
    //Passwort richtig
    void doPostSuccess() {
        String expected = "{\"resultCode\":\"1\",\"studiengang\":\"Wirtschaftsinformatik\",\"matrikelnummer\":\"1901901\",\"rolle\":\"3\"}";
        String response = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM").then().contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }
    //Passwort falsch
    @Test
    void doPostFailed(){
        String expected = "{\"resultCode\":\"2\",\"studiengang\":\"\",\"matrikelnummer\":\"\",\"rolle\":\"\"}";
        String response = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=falschesPasswort").then().contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }

}



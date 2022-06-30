package dhbw.mwi.Auslandsemesterportal2016.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuslandsAngeboteServletIT {
    @Test
    void doGetSuccess(){
        String expected = "{\"data\":[{\"studiengang\":\"Wirtschaftsinformatik\"},{\"studiengang\":\"Wirtschaftsingenieurwesen\"}]}";

        String response = get("http://10.3.15.45/auslandsAngebote")
                .then().contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }

}

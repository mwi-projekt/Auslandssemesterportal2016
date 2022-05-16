package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAuslandsAngeboteIntegrationsTest {
    @Test
    void doGetSuccess(){
        String expected = "{\"data\":[{\"studiengang\":\"Wirtschaftsinformatik\"},{\"studiengang\":\"Wirtschaftsingenieurwesen\"}]}";

        String response = get("http://10.3.15.45/auslandsAngebote").then().contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }

}

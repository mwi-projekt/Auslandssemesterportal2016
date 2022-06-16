package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetUnisIntegrationsTest {
    @Test
    void doGetSuccess(){

        String expected = "{\"data\":[{\"uniTitel\":\"Abertay University of Dundee (Schottland)\"},{\"uniTitel\":\"American University in Bulgaria (Bulgarien)\"}," +
        "{\"uniTitel\":\"California State University San Marcos (USA)\"},{\"uniTitel\":\"Costa Rica Institute of Technology (Costa Rica)\"}," +
        "{\"uniTitel\":\"Durban University of Technology (Suedafrika)\"},{\"uniTitel\":\"South-Eastern Finland University of Applied Sciences (Finnland)\"}," +
        "{\"uniTitel\":\"Standard\"}]}";

        String response = get("http://10.3.15.45/unis?studiengang=Wirtschaftsinformatik")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        assertEquals(expected, response);
    }
}

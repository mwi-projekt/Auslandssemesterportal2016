package dhbw.mwi.Auslandsemesterportal2016.integrationstest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.*;

class ModelProcessGetServletIntegrationsTest {
    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=Hallo1234!");
        String sessionID = loginResponse.getCookies().get("sessionID");

        String returnedText = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("model", "standard")
                .queryParam("step", "downloadsAnbieten")
                .when()
                .get("http://10.3.15.45/processmodel/get")
                .then().statusCode(200)
                .extract().response().asString();

        String expectedText = "%5B%7B%22type%22:%22title%22,%22content%22:%22Herzlich%20Willkommen%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EMit%20der%20Wahl%20der%20Partneruniversit%C3%A4t%20befindest%20Du%20Dich%20schon%20im%20ersten%20Schritt%20Deiner%20Bewerbung!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EDiese%20Seite%20wird%20Dich%20nun%20bis%20zur%20Erreichung%20Deiner%20Bewerbung%20begleiten.%20Dabei%20wirst%20Du%20durch%20einzelne%20Aufgaben%20gef%C3%BChrt.%3C/p%3E%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EUm%20fortfahren%20zu%20k%C3%B6nnen,%20musst%20Du%20den%20%3Ca%20data-cke-saved-href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%20href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%3EDatenschutzerkl%C3%A4rungen%20%3C/a%3Eder%20DHBW%20Karlsruhe%20zustimmen.%3C/p%3E%22%7D,%7B%22type%22:%22form-checkbox%22,%22data%22:%7B%22label%22:%22Ich%20stimme%20zu%22,%22id%22:%22gdprCompliance%22%7D,%22content%22:%22%3Cp%3E%20Ich%20stimme%20zu%3C/p%3E%22%7D%5D";
        assertEquals(expectedText ,returnedText.trim());
    }
}
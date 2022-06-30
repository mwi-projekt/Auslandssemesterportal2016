package dhbw.mwi.Auslandsemesterportal2016.rest;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelProcessGetServletIT {
    @Test
    void doGetSuccess() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
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

        String expectedText = "%5B%7B%22type%22:%22title%22,%22content%22:%22Herzlich%20Willkommen%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Ch3%3EMit%20der%20Wahl%20der%20Partneruniversit%C3%A4t%20befindest%20Du%20Dich%20schon%20im%20ersten%20Schritt%20Deiner%20Bewerbung!%3C/h3%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EDiese%20Seite%20wird%20Dich%20nun%20bis%20zur%20Erreichung%20Deiner%20Bewerbung%20begleiten.%20Dabei%20wirst%20Du%20durch%20einzelne%20Aufgaben%20gef%C3%BChrt.&nbsp;%3C/p%3E%3Cp%3EF%C3%BCr%20den%20Bewerbungsprozess%20kannst%20Du%20einen%20%3Cstrong%3EDUALIS%20Auszug%3C/strong%3E%20vorbereiten.%20Das%20musst%20Du%20im%20weiteren%20Verlauf%20als%20%3Cem%3EDualis_Auszug.pdf%3C/em%3E%20hochladen.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%3Cp%3E%3Cstrong%3EHinweis:%20Es%20gibt%20derzeit%20noch%20keine%20M%C3%B6glichkeit%20innerhalb%20des%20Formulars%20zur%C3%BCck%20zu%20navigieren.%20Daher%20solltest%20Du%20nicht%20fr%C3%BChzeitig%20auf%20die%20n%C3%A4chste%20Seite%20springen.%20Eine%20Bearbeitung%20der%20vorherigen%20Seite%20ist%20dann%20nicht%20mehr%20m%C3%B6glich&nbsp;und%20Du%20m%C3%BCsstest%20den%20Bewerbungsprozess%20von%20vorne%20beginnen.%3C/strong%3E%3Cbr%20/%3E%5Cn%3C/p%3E%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EUm%20fortfahren%20zu%20k%C3%B6nnen,%20musst%20du%20den%20%3Ca%20data-cke-saved-href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%20href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%3EDatenschutzerkl%C3%A4rungen%20%3C/a%3Eder%20DHBW%20Karlsruhe%20zustimmen.%3C/p%3E%22%7D,%7B%22type%22:%22form-checkbox%22,%22data%22:%7B%22label%22:%22%20Ich%20stimme%20zu%22,%22id%22:%22gdprCompliance%22%7D,%22content%22:%22%3Cp%3EIch%20stimme%20zu%3C/p%3E%22%7D%5D";
        assertEquals(expectedText ,returnedText.trim());
    }
}
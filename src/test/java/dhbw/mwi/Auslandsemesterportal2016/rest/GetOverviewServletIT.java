package dhbw.mwi.Auslandsemesterportal2016.rest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetOverviewServletIT {
    @Test
    void doGetEmpty() {
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("definition", "downloadsAnbieten")
                .when()
                .get("http://10.3.15.45/getOverview")
                .then().statusCode(204);
    }
    @Test
    void doGetSuccess(){
        Response loginResponse = post("http://10.3.15.45/login?email=test@student.dhbw-karlsruhe.de&pw=7sdfyxc/fsdASDFM")
                .then().statusCode(200)
                .extract().response();
        String sessionID = loginResponse.getCookies().get("sessionID");

        given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("matnr", "190190190")
                .queryParam("prio", "2")
                .queryParam("uni", "California State University San Marcos (USA)")
                .when()
                .get("http://10.3.15.45/getInstance")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();

        String content = given()
                .cookie("sessionID", sessionID)
                .cookie("email", "test@student.dhbw-karlsruhe.de")
                .queryParam("definition", "standard")
                .when()
                .get("http://10.3.15.45/getOverview")
                .then().statusCode(200)
                .contentType(ContentType.JSON).extract().response().asString();
        String expectedContent = "{\"data\":[{\"activity\":\"downloadsAnbieten\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Herzlich%20Willkommen%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Ch3%3EMit%20der%20Wahl%20der%20Partneruniversit%C3%A4t%20befindest%20Du%20Dich%20schon%20im%20ersten%20Schritt%20Deiner%20Bewerbung!%3C/h3%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EDiese%20Seite%20wird%20Dich%20nun%20bis%20zur%20Erreichung%20Deiner%20Bewerbung%20begleiten.%20Dabei%20wirst%20Du%20durch%20einzelne%20Aufgaben%20gef%C3%BChrt.&nbsp;%3C/p%3E%3Cp%3EF%C3%BCr%20den%20Bewerbungsprozess%20kannst%20Du%20einen%20%3Cstrong%3EDUALIS%20Auszug%3C/strong%3E%20vorbereiten.%20Das%20musst%20Du%20im%20weiteren%20Verlauf%20als%20%3Cem%3EDualis_Auszug.pdf%3C/em%3E%20hochladen.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%3Cp%3E%3Cstrong%3EHinweis:%20Es%20gibt%20derzeit%20noch%20keine%20M%C3%B6glichkeit%20innerhalb%20des%20Formulars%20zur%C3%BCck%20zu%20navigieren.%20Daher%20solltest%20Du%20nicht%20fr%C3%BChzeitig%20auf%20die%20n%C3%A4chste%20Seite%20springen.%20Eine%20Bearbeitung%20der%20vorherigen%20Seite%20ist%20dann%20nicht%20mehr%20m%C3%B6glich&nbsp;und%20Du%20m%C3%BCsstest%20den%20Bewerbungsprozess%20von%20vorne%20beginnen.%3C/strong%3E%3Cbr%20/%3E%5Cn%3C/p%3E%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EUm%20fortfahren%20zu%20k%C3%B6nnen,%20musst%20du%20den%20%3Ca%20data-cke-saved-href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%20href=%5C%22https://www.karlsruhe.dhbw.de/datenschutz.html%5C%22%3EDatenschutzerkl%C3%A4rungen%20%3C/a%3Eder%20DHBW%20Karlsruhe%20zustimmen.%3C/p%3E%22%7D,%7B%22type%22:%22form-checkbox%22,%22data%22:%7B%22label%22:%22%20Ich%20stimme%20zu%22,%22id%22:%22gdprCompliance%22%7D,%22content%22:%22%3Cp%3EIch%20stimme%20zu%3C/p%3E%22%7D%5D\"},{\"activity\":\"universitaetAuswaehlen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Auswahl%20deiner%20priorisierten%20Universit%C3%A4ten%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3ETrage%20hier%20bitte%20deine&nbsp;drei&nbsp;priorisierten%20Universit%C3%A4ten%20und%20den%20Zeitraum,%20auf%20welchen%20sich%20deine%20Bewerbung%20bezieht,%20ein.%3C/p%3E%3Cp%3EDu%20kannst%20die%20Felder%20der%20zweiten%20und%20dritten%20Priorit%C3%A4t%20auch%20leer%20lassen.%3C/p%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Abertay%20University%20of%20Dundee%20(Schottland)%22,%22American%20University%20in%20Bulgaria%20(Bulgarien)%22,%22California%20State%20University%20San%20Marcos%20(USA)%22,%22California%20State%20University%20Channel%20Islands%20(USA)%22,%22Costa%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%22,%22Durban%20University%20of%20Technology%20(S%C3%BCdafrika)%22,%22Liverpool%20John%20Moores%20University%20(England)%22,%22South-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%22,%22Technische%20Universit%C3%A4t%20Lodz%20(Polen)%22,%22Universit%C3%A4t%20Zadar%20(Kroatien)%22%5D,%22label%22:%22Universit%C3%A4t%20Priorit%C3%A4t%201*%22,%22id%22:%22uni1%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EUniversit%C3%A4t%20Priorit%C3%A4t%201*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EAbertay%20University%20of%20Dundee%20(Schottland)%3C/option%3E%3Coption%3EAmerican%20University%20in%20Bulgaria%20(Bulgarien)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20San%20Marcos%20(USA)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20Channel%20Islands%20(USA)%3C/option%3E%3Coption%3ECosta%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%3C/option%3E%3Coption%3EDurban%20University%20of%20Technology%20(S%C3%BCdafrika)%3C/option%3E%3Coption%3ELiverpool%20John%20Moores%20University%20(England)%3C/option%3E%3Coption%3ESouth-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%3C/option%3E%3Coption%3ETechnische%20Universit%C3%A4t%20Lodz%20(Polen)%3C/option%3E%3Coption%3EUniversit%C3%A4t%20Zadar%20(Kroatien)%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22%20%22,%22Abertay%20University%20of%20Dundee%20(Schottland)%22,%22American%20University%20in%20Bulgaria%20(Bulgarien)%22,%22California%20State%20University%20San%20Marcos%20(USA)%22,%22California%20State%20University%20Channel%20Islands%20(USA)%22,%22Costa%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%22,%22Durban%20University%20of%20Technology%20(S%C3%BCdafrika)%22,%22Liverpool%20John%20Moores%20University%20(England)%22,%22South-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%22,%22Technische%20Universit%C3%A4t%20Lodz%20(Polen)%22,%22Universit%C3%A4t%20Zadar%20(Kroatien)%22%5D,%22label%22:%22Universit%C3%A4t%20Priorit%C3%A4t%202%22,%22id%22:%22uni2%22,%22required%22:false%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EUniversit%C3%A4t%20Priorit%C3%A4t%202%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E%20%3C/option%3E%3Coption%3EAbertay%20University%20of%20Dundee%20(Schottland)%3C/option%3E%3Coption%3EAmerican%20University%20in%20Bulgaria%20(Bulgarien)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20San%20Marcos%20(USA)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20Channel%20Islands%20(USA)%3C/option%3E%3Coption%3ECosta%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%3C/option%3E%3Coption%3EDurban%20University%20of%20Technology%20(S%C3%BCdafrika)%3C/option%3E%3Coption%3ELiverpool%20John%20Moores%20University%20(England)%3C/option%3E%3Coption%3ESouth-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%3C/option%3E%3Coption%3ETechnische%20Universit%C3%A4t%20Lodz%20(Polen)%3C/option%3E%3Coption%3EUniversit%C3%A4t%20Zadar%20(Kroatien)%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22%20%22,%22Abertay%20University%20of%20Dundee%20(Schottland)%22,%22American%20University%20in%20Bulgaria%20(Bulgarien)%22,%22California%20State%20University%20San%20Marcos%20(USA)%22,%22California%20State%20University%20Channel%20Islands%20(USA)%22,%22Costa%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%22,%22Durban%20University%20of%20Technology%20(S%C3%BCdafrika)%22,%22Liverpool%20John%20Moores%20University%20(England)%22,%22South-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%22,%22Technische%20Universit%C3%A4t%20Lodz%20(Polen)%22,%22Universit%C3%A4t%20Zadar%20(Kroatien)%22%5D,%22label%22:%22Universit%C3%A4t%20Priorit%C3%A4t%203%22,%22id%22:%22uni3%22,%22required%22:false%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EUniversit%C3%A4t%20Priorit%C3%A4t%203%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E%20%3C/option%3E%3Coption%3EAbertay%20University%20of%20Dundee%20(Schottland)%3C/option%3E%3Coption%3EAmerican%20University%20in%20Bulgaria%20(Bulgarien)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20San%20Marcos%20(USA)%3C/option%3E%3Coption%3ECalifornia%20State%20University%20Channel%20Islands%20(USA)%3C/option%3E%3Coption%3ECosta%20Rica%20Institute%20of%20Technology%20(Costa%20Rica)%3C/option%3E%3Coption%3EDurban%20University%20of%20Technology%20(S%C3%BCdafrika)%3C/option%3E%3Coption%3ELiverpool%20John%20Moores%20University%20(England)%3C/option%3E%3Coption%3ESouth-Eastern%20Finland%20University%20of%20Applied%20Sciences%20(Finnland)%3C/option%3E%3Coption%3ETechnische%20Universit%C3%A4t%20Lodz%20(Polen)%3C/option%3E%3Coption%3EUniversit%C3%A4t%20Zadar%20(Kroatien)%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Fr%C3%BChlings-/Sommersemester%202022%22,%22Herbst-/Wintersemester%202022%22,%22Fr%C3%BChlings-/Sommersemester%202023%22,%22Herbst-/Wintersemester%202023%22%5D,%22label%22:%22Auf%20welchen%20Zeitraum%20bezieht%20sich%20deine%20Bewerbung?%22,%22id%22:%22zeitraum%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EAuf%20welchen%20Zeitraum%20bezieht%20sich%20deine%20Bewerbung?%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EFr%C3%BChlings-/Sommersemester%202022%3C/option%3E%3Coption%3EHerbst-/Wintersemester%202022%3C/option%3E%3Coption%3EFr%C3%BChlings-/Sommersemester%202023%3C/option%3E%3Coption%3EHerbst-/Wintersemester%202023%3C/option%3E%3C/select%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"datenEingeben\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Pers%C3%B6nliche%20Daten%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Vorname*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewVorname%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EVorname*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Nachname*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewNachname%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3ENachname*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Telefonnummer*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewTelefon%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3ETelefonnummer*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Stra%C3%9Fe,%20Hausnummer*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewStrasse%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EStra%C3%9Fe,%20Hausnummer*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Postleitzahl*%22,%22type%22:%22number%22,%22numchars%22:%225%22,%22id%22:%22bewPLZ%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EPostleitzahl*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Ort*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewOrt%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EOrt*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Geburtsdatum%20(TT.MM.JJJJ)*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22bewGeburtsdatum%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EGeburtsdatum%20(TT.MM.JJJJ)*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Deutsch%22,%22Belgisch%22,%22Englisch%22,%22Franz%C3%B6sisch%22,%22Italienisch%22,%22Kroatisch%22,%22Luxemburgisch%22,%22Niederl%C3%A4ndisch%22,%22%C3%96sterreichisch%22,%22Polnisch%22,%22Portugiesisch%22,%22Schweizerisch%22,%22Spanisch%22,%22Syrisch%22,%22Tschechisch%22,%22T%C3%BCrkisch%22,%22Ungarisch%22%5D,%22label%22:%22Nationalit%C3%A4t*%22,%22id%22:%22bewLand%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3ENationalit%C3%A4t*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EDeutsch%3C/option%3E%3Coption%3EBelgisch%3C/option%3E%3Coption%3EEnglisch%3C/option%3E%3Coption%3EFranz%C3%B6sisch%3C/option%3E%3Coption%3EItalienisch%3C/option%3E%3Coption%3EKroatisch%3C/option%3E%3Coption%3ELuxemburgisch%3C/option%3E%3Coption%3ENiederl%C3%A4ndisch%3C/option%3E%3Coption%3E%C3%96sterreichisch%3C/option%3E%3Coption%3EPolnisch%3C/option%3E%3Coption%3EPortugiesisch%3C/option%3E%3Coption%3ESchweizerisch%3C/option%3E%3Coption%3ESpanisch%3C/option%3E%3Coption%3ESyrisch%3C/option%3E%3Coption%3ETschechisch%3C/option%3E%3Coption%3ET%C3%BCrkisch%3C/option%3E%3Coption%3EUngarisch%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22DHBW%20Heidenheim%22,%22DHBW%20Heilbronn%22,%22DHBW%20Karlsruhe%22,%22DHBW%20L%C3%B6rrach%22,%22DHBW%20Mannheim%22,%22DHBW%20Mosbach%22,%22DHBW%20Ravensburg%22,%22DHBW%20Stuttgart%22,%22DHBW%20Villingen-Schwenningen%22%5D,%22label%22:%22DHBW%20Campus*%22,%22id%22:%22aktuelleUni%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EDHBW%20Campus*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EDHBW%20Heidenheim%3C/option%3E%3Coption%3EDHBW%20Heilbronn%3C/option%3E%3Coption%3EDHBW%20Karlsruhe%3C/option%3E%3Coption%3EDHBW%20L%C3%B6rrach%3C/option%3E%3Coption%3EDHBW%20Mannheim%3C/option%3E%3Coption%3EDHBW%20Mosbach%3C/option%3E%3Coption%3EDHBW%20Ravensburg%3C/option%3E%3Coption%3EDHBW%20Stuttgart%3C/option%3E%3Coption%3EDHBW%20Villingen-Schwenningen%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Wirtschaftsinformatik%22,%22Wirtschaftsingenieurswesen%22%5D,%22label%22:%22Studiengang*%22,%22id%22:%22bewStudiengang%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EStudiengang*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EWirtschaftsinformatik%3C/option%3E%3Coption%3EWirtschaftsingenieurswesen%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Sales%20&%20Consulting%22,%22Data%20Science%22,%22Software%20Engineering%22%5D,%22label%22:%22Studienrichtung%22,%22id%22:%22bewStudienrichtung%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EStudienrichtung%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3ESales%20&amp;%20Consulting%3C/option%3E%3Coption%3EData%20Science%3C/option%3E%3Coption%3ESoftware%20Engineering%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%221.%20Semester%22,%222.%20Semester%22,%223.%20Semester%22,%224.%20Semester%22,%225.%20Semester%22,%226.%20Semester%22%5D,%22label%22:%22Aktuelles%20Fachsemester*%22,%22id%22:%22bewSemester%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EAktuelles%20Fachsemester*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E1.%20Semester%3C/option%3E%3Coption%3E2.%20Semester%3C/option%3E%3Coption%3E3.%20Semester%3C/option%3E%3Coption%3E4.%20Semester%3C/option%3E%3Coption%3E5.%20Semester%3C/option%3E%3Coption%3E6.%20Semester%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Matrikelnummer*%22,%22type%22:%22number%22,%22numchars%22:%22%22,%22id%22:%22matrikelnummer%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EMatrikelnummer*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22DHBW%20E-Mail*%22,%22type%22:%22email%22,%22numchars%22:%22%22,%22id%22:%22bewEmail%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EDHBW%20E-Mail*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22%20%22,%22Englisch%22,%22Spanisch%22%5D,%22label%22:%22Muttersprache%20(au%C3%9Fer%20Deutsch)%22,%22id%22:%22muttersprache%22,%22required%22:false%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EMuttersprache%20(au%C3%9Fer%20Deutsch)%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E%20%3C/option%3E%3Coption%3EEnglisch%3C/option%3E%3Coption%3ESpanisch%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Nein%22,%22Ja%22%5D,%22label%22:%22Wurdest%20du%20bereits%20von%20Erasmus%20gef%C3%B6rdert?%22,%22id%22:%22bewErasmus%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EWurdest%20du%20bereits%20von%20Erasmus%20gef%C3%B6rdert?%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3ENein%3C/option%3E%3Coption%3EJa%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Z%C3%A4hlen%20Sie%20sich%20in%20Bezug%20auf%20ihre%20Bildungschancen%20zu%20einer%20benachteiligten%20Gruppe%20(z.B.%20erste%20Person%20aus%20der%20Familie,%20die%20studiert)?%20Falls%20ja,%20stellen%20Sie%20dies%20bitte%20kurz%20dar.%22,%22type%22:%22text%22,%22numchars%22:%22500%22,%22id%22:%22bewBenachteiligung%22,%22required%22:false%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EZ%C3%A4hlen%20Sie%20sich%20in%20Bezug%20auf%20ihre%20Bildungschancen%20zu%20einer%20benachteiligten%20Gruppe%20(z.B.%20erste%20Person%20aus%20der%20Familie,%20die%20studiert)?%20Falls%20ja,%20stellen%20Sie%20dies%20bitte%20kurz%20dar.%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-checkbox%22,%22data%22:%7B%22label%22:%22Ich%20bin%20bereit%20einen%20Erfahrungsbericht%20%C3%BCber%20meinen%20Auslandsaufenthalt%20zu%20verfasssen%20und%20zu%20ver%C3%B6ffentlichen.%22,%22id%22:%22bewErfahrungsberichtZustimmung%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22checkbox%5C%22%3E%3Clabel%3E%3Cinput%20type=%5C%22checkbox%5C%22%3E%20Ich%20bin%20bereit%20einen%20Erfahrungsbericht%20%C3%BCber%20meinen%20Auslandsaufenthalt%20zu%20verfasssen%20und%20zu%20ver%C3%B6ffentlichen.%3C/label%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"datenEingebenUnt\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Partnerunternehmen%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EBitte%20trage%20hier%20die&nbsp;Daten%20deines%20Partnerunternehmens%20ein.%3C/p%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Unternehmen*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22untName%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EUnternehmen*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Stra%C3%9Fe,%20Hausnummer*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22untStrasse%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EStra%C3%9Fe,%20Hausnummer*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Postleitzahl*%22,%22type%22:%22number%22,%22numchars%22:%225%22,%22id%22:%22untPLZ%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EPostleitzahl*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Ort*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22untOrt%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EOrt*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%22Deutschland%22,%22Frankreich%22,%22Luxemburg%22,%22Niederlande%22,%22%C3%96sterreich%22,%22Polen%22,%22Schweiz%22,%22Belgien%22,%22Tschechien%22,%22Liechtenstein%22%5D,%22label%22:%22Land*%22,%22id%22:%22untLand%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3ELand*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3EDeutschland%3C/option%3E%3Coption%3EFrankreich%3C/option%3E%3Coption%3ELuxemburg%3C/option%3E%3Coption%3ENiederlande%3C/option%3E%3Coption%3E%C3%96sterreich%3C/option%3E%3Coption%3EPolen%3C/option%3E%3Coption%3ESchweiz%3C/option%3E%3Coption%3EBelgien%3C/option%3E%3Coption%3ETschechien%3C/option%3E%3Coption%3ELiechtenstein%3C/option%3E%3C/select%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Ansprechpartner*%22,%22type%22:%22text%22,%22numchars%22:%22%22,%22id%22:%22untAnsprechpartner%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EAnsprechpartner*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-text%22,%22data%22:%7B%22label%22:%22Email%20Adresse%20der%20Ansprechperson*%22,%22type%22:%22email%22,%22numchars%22:%22%22,%22id%22:%22untEMail%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EEmail%20Adresse%20der%20Ansprechperson*%3C/label%3E%3Cbr%20/%3E%5Cn%3Cinput%20class=%5C%22form-control%5C%22%3E%3C/div%3E%22%7D,%7B%22type%22:%22form-checkbox%22,%22data%22:%7B%22label%22:%22Einwilligung%20des%20Unternehmens%20liegt%20vor%20%22,%22id%22:%22untEinwilligung%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22checkbox%5C%22%3E%3Clabel%3E%3Cinput%20type=%5C%22checkbox%5C%22%3E%20Einwilligung%20des%20Unternehmens%20liegt%20vor%20%3C/label%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"zustimmungHochladen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Anmeldeformular%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EHier%20kannst%20du%20das%3Cstrong%3E&nbsp;%3Ca%20href=%5C%22#downloadAnmeldeformular%5C%22%20target=%5C%22_blank%5C%22%20data-cke-saved-href=%5C%22#downloadAnmeldeformular%5C%22%3EAnmeldeformular%3C/a%3E&nbsp;%3C/strong%3Eherunterladen.%3C/p%3E%3Cp%3EDieses%20Anmeldeformular%20muss%20ausgedruckt%20und%20sowohl%20von%20%3Cstrong%3Edir%3C/strong%3E,%20als%20auch%20von%20deiner%20%3Cstrong%3EAusbildungsbetreuung%3C/strong%3E&nbsp;%3Cstrong%3Eunterschrieben%3C/strong%3E%20werden.%3C/p%3E%3Cp%3EAnschlie%C3%9Fend%20kannst%20du%20die%20Datei%20hier%20hochladen.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20du%20sie%20nicht%20mehr%20nachtr%C3%A4glich%20korrigieren%20kannst!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22Anmeldeformular.pdf%22,%22id%22:%22zustimmungsFormular%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"englischNotePruefen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Englischnote%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EW%C3%A4hle&nbsp;die%20im%20Abitur%20erreichten%20Notenpunkte&nbsp;aus%20der%20Auswahlliste.%3C/p%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%221%22,%222%22,%223%22,%224%22,%225%22,%226%22,%227%22,%228%22,%229%22,%2210%22,%2211%22,%2212%22,%2213%22,%2214%22,%2215%22%5D,%22label%22:%22Englischnote%22,%22id%22:%22englischNote%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3EEnglischnote%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E1%3C/option%3E%3Coption%3E2%3C/option%3E%3Coption%3E3%3C/option%3E%3Coption%3E4%3C/option%3E%3Coption%3E5%3C/option%3E%3Coption%3E6%3C/option%3E%3Coption%3E7%3C/option%3E%3Coption%3E8%3C/option%3E%3Coption%3E9%3C/option%3E%3Coption%3E10%3C/option%3E%3Coption%3E11%3C/option%3E%3Coption%3E12%3C/option%3E%3Coption%3E13%3C/option%3E%3Coption%3E14%3C/option%3E%3Coption%3E15%3C/option%3E%3C/select%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"spanischNotePruefen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Spanischnote%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EW%C3%A4hle&nbsp;die%20im%20Abitur%20erreichten%20Notenpunkte&nbsp;aus%20der%20Auswahlliste.%3C/p%3E%22%7D,%7B%22type%22:%22form-select%22,%22data%22:%7B%22values%22:%5B%221%22,%222%22,%223%22,%224%22,%225%22,%226%22,%227%22,%228%22,%229%22,%2210%22,%2211%22,%2212%22,%2213%22,%2214%22,%2215%22%5D,%22label%22:%22Spanischnote%22,%22id%22:%22spanischNote%22,%22required%22:true%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Clabel%3ESpanischnote%3C/label%3E%3Cbr%20/%3E%5Cn%3Cselect%20class=%5C%22form-control%5C%22%3E%3Coption%3E1%3C/option%3E%3Coption%3E2%3C/option%3E%3Coption%3E3%3C/option%3E%3Coption%3E4%3C/option%3E%3Coption%3E5%3C/option%3E%3Coption%3E6%3C/option%3E%3Coption%3E7%3C/option%3E%3Coption%3E8%3C/option%3E%3Coption%3E9%3C/option%3E%3Coption%3E10%3C/option%3E%3Coption%3E11%3C/option%3E%3Coption%3E12%3C/option%3E%3Coption%3E13%3C/option%3E%3Coption%3E14%3C/option%3E%3Coption%3E15%3C/option%3E%3C/select%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"dualisHochladen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Dualis%20Auszug%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3ELade&nbsp;hier%20deinen%20Dualis%20Auszug%20hoch.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20sie%20nicht%20mehr%20nachtr%C3%A4glich%20korrigiert%20werden%20kann!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22Dualis_Auszug.pdf%22,%22id%22:%22dualisAuszug%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"daadHochladen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22DAAD-Formular%20Englisch%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EDa%20du%20nicht%20angegeben%20hast,%20dass%20Englisch%20deine%20Muttersprache%20ist,%20wird%20ein%20DAAD-Formular%20von%20dir%20ben%C3%B6tigt.%3C/p%3E%3Cp%3ELade%20es%20dir%20bitte%20%3Ca%20data-cke-saved-href=%5C%22%5C%5Cdocs%5C%5CDAAD-Sprachzeugnis.pdf%5C%22%20href=%5C%22%5C%5Cdocs%5C%5CDAAD-Sprachzeugnis.pdf%5C%22%20target=%5C%22_blank%5C%22%3E%3Cstrong%3Ehier%3C/strong%3E%3C/a%3E%3Cstrong%3E&nbsp;%3C/strong%3Eherunter%20und%20f%C3%BClle%20es%20entsprechend%20aus.%3C/p%3E%3Cp%3EAnschlie%C3%9Fend%20kannst%20du%20unten%20das%20ausgef%C3%BCllte%20DAAD-Formular%20wieder%20hochladen.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20du%20sie%20nachtr%C3%A4glich%20nicht%20mehr%20korrigieren%20kannst!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22DAAD-Formular_Englisch.pdf%22,%22id%22:%22daadFormularEnglisch%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"Activity_0kzd76q\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22DAAD-Formular%20Spanisch%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EDa%20du%20nicht%20angegeben%20hast,%20dass%20Spanisch%20deine%20Muttersprache%20ist,%20wird%20ein%20DAAD-Formular%20von%20dir%20ben%C3%B6tigt.%3C/p%3E%3Cp%3ELade%20es%20dir%20bitte%20%3Ca%20data-cke-saved-href=%5C%22%5C%5Cdocs%5C%5CDAAD-Sprachzeugnis.pdf%5C%22%20href=%5C%22%5C%5Cdocs%5C%5CDAAD-Sprachzeugnis.pdf%5C%22%20target=%5C%22_blank%5C%22%3E%3Cstrong%3Ehier%3C/strong%3E%3C/a%3E%3Cstrong%3E&nbsp;%3C/strong%3Eherunter%20und%20f%C3%BClle%20es%20entsprechend%20aus.%3C/p%3E%3Cp%3EAnschlie%C3%9Fend%20kannst%20du%20unten%20das%20ausgef%C3%BCllte%20DAAD-Formular%20wieder%20hochladen.%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20du%20sie%20nachtr%C3%A4glich%20nicht%20mehr%20korrigieren%20kannst!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22DAAD_Formular_Spanisch.pdf%22,%22id%22:%22daadFormularSpanisch%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"motivationHochladen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Motivationsschreiben%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3EDas%20Motivationsschreiben%20muss%20von%20dir%20auf%20Englisch%20erstellt%20werden.%20Solltest%20du%20Costa%20Rica%20als%20Zielland%20ausgew%C3%A4hlt%20haben,%20kannst%20du%20das%20Motivationsschreiben%20auch%20in%20Spanisch%20schreiben.%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EDas%20Motivationsschreiben%20muss%20einen%20Umfang%20von%20einer%20Seite%20haben.%3Cbr%20/%3E%5Cn%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EScanne%20das%20Schreiben%20ein%20und%20lade%20es%20hier%20hoch.%3Cbr%20/%3E%5Cn%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20sie%20nicht%20mehr%20nachtr%C3%A4glich%20korrigiert%20werden%20kann!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22Motivationsschreiben.pdf%22,%22id%22:%22motivationsSchreiben%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"abiturHochladen\",\"data\":\"%5B%7B%22type%22:%22title%22,%22content%22:%22Abiturzeugnis%22%7D,%7B%22type%22:%22paragraph%22,%22content%22:%22%3Cp%3ELade%20hier%20dein%20Abiturzeugnis%20hoch.%3Cstrong%3E%E2%80%8B%E2%80%8B%E2%80%8B%E2%80%8B%E2%80%8B%E2%80%8B%E2%80%8B%3C/strong%3E%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3EKontrolliere%20am%20besten%20die%20Datei%20nochmal,%20da%20du%20sie%20sp%C3%A4ter%20nicht%20mehr%20korrigieren%20kannst!%3C/p%3E%3Cp%3E%3Cbr%20/%3E%5Cn%3C/p%3E%3Cp%3E%3Cstrong%3EDu%20kannst%20den%20Bewerbungsprozess%20jederzeit%20pausieren%20und%20sp%C3%A4ter%20zu%20dieser%20Stelle%20zur%C3%BCckkehren.%3C/strong%3E%3Cbr%20/%3E%5Cn%3C/p%3E%22%7D,%7B%22type%22:%22form-upload%22,%22data%22:%7B%22filename%22:%22Abitur_Zeugnis.pdf%22,%22id%22:%22abiturZeugnis%22%7D,%22content%22:%22%3Cdiv%20class=%5C%22form-group%5C%22%3E%3Ci%20class=%5C%22fa%20fa-upload%5C%22%3E%3C/i%3E%3C/div%3E%22%7D%5D\"},{\"activity\":\"datenPruefen\",\"data\":\"%5B%5D\"}]}";
        assertEquals(expectedContent, content);
    }
}
package dhbw.mwi.Auslandsemesterportal2016.enums;

public enum TestEnum {
    TESTEMAIL("testusermwi@dhbw.de"), TESTMATRNR("1234567"), TESTROLLESTRING("Studierender"), TESTROLLEINT("1"),
    TESTPW("Test1234"), TESTSTUGANG("Wirtschaftsiformatik"), TESTKURS("WWI18B1"), TESTTELNR("07234512345"),
    TESTMOBILNR("014773698521"), TESTSTANDORT("Karlsruhe"), TESTVNAME("Test"), TESTNNAME("Admin"),
    TESTSESSIONID("sessionIDs1e5f2ge8gvs694g8vedsg"), TESTINSTANCEID("instanceIDs1e5f2ge8gvs694g8vedsg"),
    TESTKEYSTRING(
            "gdprCompliance|uni1|uni2|uni3|zeitraum|bewVorname|bewNachname|bewTelefon|bewStrasse|bewPLZ|bewOrt|bewGeburtsdatum|bewLand|aktuelleUni|bewStudiengang|bewSemester|matrikelnummer|bewEmail|muttersprache|bewErasmus|bewLA|bewSGL|untName|untStrasse|untPLZ|untOrt|untLand|untAnsprechpartner|untEMail|englischNote"),
    TESTVALSTRING(
            "false|Abertay University of Dundee (Schottland)|California State University San Marcos (USA)|South-Eastern Finland University of Applied Sciences (Finnland)|Frühlings-/Sommersemester 2022|Test|Student|546|fkfk|65465|wcdw|wecwce|Deutsch|DHBW Karlsruhe|Wirtschaftsinformatik|1. Semester|190190190|test@student.dhbw-karlsruhe.de||Nein|Nein|Nein|sx|qws|354|ufufu|Deutschland|wedw|wedw@wde.de|6"),
    TESTTYPESTRING(
            "boolean|text|text|text|text|text|text|text|text|number|text|text|text|text|text|text|number|email|text|text|text|text|text|text|number|text|text|text|email|text");

    private final String text;

    private TestEnum(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
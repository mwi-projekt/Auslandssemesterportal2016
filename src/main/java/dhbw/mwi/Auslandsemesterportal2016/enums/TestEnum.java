package dhbw.mwi.Auslandsemesterportal2016.enums;

public enum TestEnum {
    TESTEMAIL("testusermwi@dhbw.de"), TESTMATRNR("1234567"), TESTROLLESTRING("Studierender"), TESTROLLEINT("1"),
    TESTPW("Test1234"), TESTSTUGANG("Wirtschaftsiformatik"), TESTKURS("WWI18B1"), TESTTELNR("07234512345"),
    TESTMOBILNR("014773698521"), TESTSTANDORT("Karlsruhe"), TESTVNAME("Test"), TESTNNAME("Admin"),;

    private final String text;

    private TestEnum(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
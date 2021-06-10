package dhbw.mwi.Auslandsemesterportal2016.enums;

public enum MessageEnum {

    AAAREGISTR("Akademisches Auslandsamt Registrierung");

    private final String text;

    private MessageEnum(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}
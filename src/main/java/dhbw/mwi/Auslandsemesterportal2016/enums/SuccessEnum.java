package dhbw.mwi.Auslandsemesterportal2016.enums;

public enum SuccessEnum {
    USERDELETE("Deleted User successfully"), CREATEUSER("Created User successfully"),
    RESETACC("Done resetting account "), UPDATEUSER("Updated user successfully");

    private final String text;

    private SuccessEnum(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}

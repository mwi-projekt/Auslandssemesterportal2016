package dhbw.mwi.Auslandsemesterportal2016.enums;

public enum ErrorEnum {
    USERNOTDELETED("User not found or could not be deleted"), USERCREATE("Fehler beim Anlegen: "),
    PARAMMISSING("Error: parameters are missing"), DBERROR("Error: database error"), USERUPDATE("UpdateError"),
    USERREGISTER("RegisterError"), MAILERROR("MailError");

    private final String text;

    private ErrorEnum(String s) {
        text = s;
    }

    @Override
    public String toString() {
        return text;
    }
}

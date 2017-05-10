package dhbw.mwi.Auslandsemesterportal2016.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Util {

    public static String HashSha256(String input) {
        String result = null;

        if (null == input)
            return null;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(input.getBytes());

            byte[] hash = md.digest();

            result = javax.xml.bind.DatatypeConverter.printHexBinary(hash).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);

        String result = javax.xml.bind.DatatypeConverter.printHexBinary(bytes).toLowerCase();

        return result;
    }

}

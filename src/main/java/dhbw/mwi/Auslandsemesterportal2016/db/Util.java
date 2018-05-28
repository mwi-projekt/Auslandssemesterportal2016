package dhbw.mwi.Auslandsemesterportal2016.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

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

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);

        String result = javax.xml.bind.DatatypeConverter.printHexBinary(bytes).toLowerCase();

        return result;
    }

    public static Session getEmailSession() {
    	/*// Sender's email ID needs to be mentioned
        String host = "10.3.43.6";

		Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "25");
        
        // Get the Session object.
        Session session = Session.getInstance(props);
        
        */
        
     // Sender's email ID needs to be mentioned
        String from = "mwiausland@gmail.com";// change accordingly
        final String username = "mwiausland@gmail.com";// change
        // accordingly
        final String password = "MWIAusland1";// change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

		//Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		    }
		});
		
		return session;
    }
    
}

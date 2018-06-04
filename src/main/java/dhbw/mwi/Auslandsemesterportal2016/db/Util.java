package dhbw.mwi.Auslandsemesterportal2016.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

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

    private static Session getEmailSession() {
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
    
    public static Message getEmailMessage(String emailTo, String emailFrom, String subject) throws AddressException, MessagingException
    {
    	Message message = new MimeMessage(getEmailSession());

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(emailFrom));

        // Set To: header field of the header.
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));

        // Set Subject: header field
        message.setSubject(subject);
        return message;
    }
    
    public static Message getEmailMessage(String emailTo, String subject) throws AddressException, MessagingException
    {
    	return getEmailMessage(emailTo, "noreply@dhbw-karlsruhe.de",  subject);
    }
}

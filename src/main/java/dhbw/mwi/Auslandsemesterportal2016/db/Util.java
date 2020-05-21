package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dhbw.mwi.Auslandsemesterportal2016.Config;

public class Util {

    public static String HashSha256(String input) {
        String result = null;

        if (null == input)
            return null;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(input.getBytes());

            byte[] hash = md.digest();

            result = DatatypeConverter.printHexBinary(hash).toLowerCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);

        String result = DatatypeConverter.printHexBinary(bytes).toLowerCase();

        return result;
    }

    
    public static Message getEmailMessage(String emailTo, String emailFrom, String subject) throws AddressException, MessagingException
    {
    	Message message = new MimeMessage(Mail.getInstance());

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

    public static void setResponseHeaders(HttpServletRequest request, HttpServletResponse response){
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", Config.AllowedOrigins.contains(origin) ? origin : "");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Vary", "Origin");
    }

    public static void writeJson(HttpServletResponse response, JsonObject json) {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeJson(HttpServletResponse response, ResultSet rs) {
		JsonArray arr = new JsonArray();
		if (rs != null) {
			try {
				while (rs.next()) {
					JsonObject json = new JsonObject();
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
						json.addProperty(rs.getMetaData().getColumnName(i), rs.getString(i));
					}
					arr.add(json);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JsonObject data = new JsonObject();
		data.add("data", arr);
		writeJson(response, data);
	}
}

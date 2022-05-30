package dhbw.mwi.Auslandsemesterportal2016.db;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import dhbw.mwi.Auslandsemesterportal2016.Config;

public class Mail {

	private Mail() {
	}

	public static Session getInstance() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", Config.MAIL_HOST);
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");

		return Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Config.MAIL_USER, Config.MAIL_PASS);
			}
		});
	}

}

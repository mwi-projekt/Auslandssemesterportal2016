package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class validationFailure implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
				
		final String username = "mwiausland@gmail.com";
		final String password = "MWIAusland1";
		String host = "smtp.gmail.com";
	
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
	
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
														return new PasswordAuthentication(username, password);
			}
		});
	
		try {
			
			String email = (String) execution.getVariable("Student-Email");
			String nachname = (String) execution.getVariable("Student-Nachname");
			String uni = (String) execution.getVariable("Uni");
			
			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Eingereichte Bewerbung für Auslandssemester fehlerhaft/unvollständig", "utf-8", "B"));
			message.setContent("Sehr geehrte/r Herr/Frau " + nachname + (",")  + "\n" + "\n" +
					 "Ihre eingereichte Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: " + uni + " konnte nicht vollständig validiert werden." + "\n" + 
						"Wir bitten Sie, dass Sie Ihre Eingaben erneut im Auslandssemesterportal überprüfen." + "\n" +
						"Vielen Dank für Ihr Verständnis." + "\n" + "\n" +
						"Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Akademisches Auslandsamt", "text/plain; charset=UTF-8");
	
			Transport.send(message);
	
		} catch (MessagingException e) {
			System.out.print("Could not send email!");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

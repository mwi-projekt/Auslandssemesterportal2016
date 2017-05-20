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
		
		String email = (String) execution.getVariable("studentEmail");
		String nachname = (String) execution.getVariable("studentNachname");
		String uni = (String) execution.getVariable("uni");
		boolean erfolgreich = (Boolean) execution.getVariable("validierungErfolgreich");
		String fehlerUrsache = (String) execution.getVariable("fehlerUrsache");

		try {
			Message message = new MimeMessage(session);

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			//Bei erfolgreicher Validierung
			if(erfolgreich){
				message.setSubject(
						MimeUtility.encodeText("Eingereichte Bewerbung für Auslandssemester validiert", "utf-8", "B"));
				message.setContent("Sehr geehrte/r Herr/Frau " + nachname + (",") + 
				"\n"+ 
				"\n"+ "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot an der Universität: "+ uni +" wurde erfolgreich an das Akademisches Auslandsamt versendet."+
				"\n"+ "Im nächsten Schritt wird sich ein Mitarbeiter zeitnahe um die Bearbeitung Ihrer Bewerbung kümmern und entscheiden, ob Sie in die engere Auswahl potentieller Bewerber kommen"+ 
				"\n"+ "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren." +  
				"\n"+ 
				"\n"+ "Mit freundlichen Grüßen," + 
				"\n"+ 
				"\n"+ "Ihr Akademisches Auslandsamt", "text/plain; charset=UTF-8");
							
				}
			//wenn Validierung fehlgeschlagen
				else{
					message.setSubject(
							MimeUtility.encodeText("Bei der Validierung Ihrer Bewerbung ist ein Fehler aufgetreten", "utf-8", "B"));
				message.setContent("Sehr geehrte/r Herr/Frau " + nachname + (",") + 
				"\n"+ 
				"\n"+ "Vielen Dank für Ihre eingereichte Bewerbung an der Universität: "+ uni + 
				"\n"+ "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + 
				"\n"+		
				"\n"+ "Die Fehlerursache lautet wie folgt:" +
				"\n"+ fehlerUrsache +
				"\n"+
				"\n"+ "Ein Mitarbeiter wird sich daher bald mit Ihnen in Verbindung setzen." +
				"\n"+ "Wir bitten um Ihr Verständnis." +
				"\n"+ 
				"\n"+ "Mit freundlichen Grüßen," + 
				"\n"+ 
				"\n"+ "Ihr Akademisches Auslandsamt", "text/plain; charset=UTF-8");
			}

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

package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.Mail;
import org.junit.jupiter.api.Test;

import javax.mail.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailTest {
    @Test
    void getInstanceSessionIsCorrectelyConfigured() {
        Session session = Mail.getInstance();

        assertEquals("true", session.getProperty("mail.smtp.auth"));
        assertEquals(Config.MAIL_HOST, session.getProperty("mail.smtp.host"));
        assertEquals("465", session.getProperty("mail.smtp.port"));
        assertEquals("true", session.getProperty("mail.smtp.ssl.enable"));
    }
}
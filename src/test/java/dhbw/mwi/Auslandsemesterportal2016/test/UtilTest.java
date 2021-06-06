package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.db.Mail;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

public class UtilTest {
    HttpServletRequest request;
    HttpServletResponse response;
    MockedStatic<Util> util;

    @BeforeMethod
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        util = Mockito.mockStatic(Util.class);
    }

    @AfterMethod
    public void close() {
        util.close();
    }

    /*
     * method verifies that addResponseHeaders() is called
     * 
     * method includes calls in external class (Config), which are not part of
     * unit-testing that's why Util is mocked
     *
     * method is called in RegisterServlet-Class
     */
    @Test
    public void verifyAddResponseHeaders() {

        // do nothing when addResponseHeaders() from Util.class is called
        util.when(() -> Util.addResponseHeaders(any(), any())).thenAnswer((Answer<?>) invocation -> null);

        Util.addResponseHeaders(request, response);
        // verify static method addResponseHeaders() from Util.class was called
        util.verify(() -> Util.addResponseHeaders(request, response));

    }

    /*
     * Testing static getEmailMessage()
     * 
     * checks if subject, recipients and sender are equal
     *
     * method is called in RegisterServlet-Class
     */
    @Test
    public void testGetEmailMessage() throws AddressException, MessagingException {
        Message expectedMessage = new MimeMessage(Mail.getInstance());
        expectedMessage.setFrom(new InternetAddress("noreply@dhbw-karlsruhe.de"));
        expectedMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse("testusermwi@dhbw.de"));
        expectedMessage.setSubject("Akademisches Auslandsamt Registrierung");

        util.when(() -> Util.getEmailMessage(anyString(), anyString())).thenCallRealMethod();
        util.when(() -> Util.getEmailMessage(anyString(), anyString(), anyString())).thenCallRealMethod();

        Message actualMessage = Util.getEmailMessage("testusermwi@dhbw.de", "Akademisches Auslandsamt Registrierung");
        assertEquals(expectedMessage.getSubject(), actualMessage.getSubject());

        String expectedFromMail = Arrays.asList(expectedMessage.getFrom()).toString();
        String actualFromMail = Arrays.asList(actualMessage.getFrom()).toString();
        assertEquals(expectedFromMail, actualFromMail);

        String expectedRecipients = Arrays.asList(expectedMessage.getAllRecipients()).toString();
        String actualRecipients = Arrays.asList(actualMessage.getAllRecipients()).toString();
        assertEquals(expectedRecipients, actualRecipients);
    }

    /*
     * Testing static generateSalt()
     * 
     * checks if salt isn't empty and if length equals 64
     *
     * method is called in RegisterServlet-Class
     */
    @Test
    public void testGenerateSalt() {
        util.when(()->Util.generateSalt()).thenCallRealMethod();

        String salt = Util.generateSalt();
        assertNotNull(salt);
        assertEquals(salt.length(), 64);
    }

    @Test
    public void testHashSha256() {
        util.when(()->Util.HashSha256(anyString())).thenCallRealMethod();

        String hashPw = Util.HashSha256("Hallo1234");
        String expectedPw = Util.HashSha256(hashPw + "de3e21dcebe72427883ece");

        String actualPw = Util.HashSha256(Util.HashSha256("Hallo1234") + "de3e21dcebe72427883ece");
        assertEquals(expectedPw, actualPw);

        String pw = Util.HashSha256(Util.HashSha256("Hallo1234") + Util.generateSalt());
        assertNotNull(pw);
    }

}

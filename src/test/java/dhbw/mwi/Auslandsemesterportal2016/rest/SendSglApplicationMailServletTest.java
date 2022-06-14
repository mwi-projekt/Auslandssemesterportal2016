package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isTestUser;
import static javax.mail.Transport.send;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

class SendSglApplicationMailServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<Transport> transport;
    private MockedStatic<UserAuthentification> userAuthentification;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        transport = mockStatic(Transport.class);
        userAuthentification = mockStatic(UserAuthentification.class);
    }

    @AfterEach
    void tearDown() {
        transport.close();
        userAuthentification.close();
    }

    @Test
    void doPostDoesNotSendMailWhenTestuser() throws IOException {
        // given
        userAuthentification.when(() -> isTestUser(any())).thenReturn(true);
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // when
        new SendApplicationMailServlet() {
            public void callProtectedMethod(HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // then
        assertEquals("Keine E-Mail im Kontext von Tests versendet", writer.toString().trim());
        transport.verify(() -> Transport.send(any()), times(0));
    }

    @Test
    void doPostSendsMail() throws IOException {
        userAuthentification.when(() -> isTestUser(any())).thenReturn(false);
        when(request.getParameter("instance_id")).thenReturn("anyInstanceId");

        new SendSglApplicationMailServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        transport.verify(() -> send(any()), times(1));
    }

    @Test
    void messageIsCorrectlyConfigured() throws MessagingException, IOException {
        SendSglApplicationMailServlet sendSglApplicationMailServlet = new SendSglApplicationMailServlet();
        Message message = null;
        try {
            message = sendSglApplicationMailServlet.getMessage();
        } catch (MessagingException e) {
            fail();
        }

        Address[] from = message.getFrom();
        Address[] allRecipients = message.getAllRecipients();
        String subject = message.getSubject();
        String expectedContent = "<h2>Sehr geehrte Frau Witte"
                + ",</h2> es ist eine neue Bewerbung im Auslandssemesterportal."
                + "<br><br> "
                + "<a href= \"http://10.3.15.45/\" target=\"new\">Auslandssemesterportal</a>";
        String content = (String) message.getContent();

        assertEquals("noreply@dhbw-karlsruhe.de", from[0].toString());
        assertEquals("sarah.witte@dhbw-karlsruhe.de", allRecipients[0].toString());
        assertEquals("Neue Bewerbung im Auslandssemesterportal", subject);
        assertEquals(expectedContent, content);
    }
}
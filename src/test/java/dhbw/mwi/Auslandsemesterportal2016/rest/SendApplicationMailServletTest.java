package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.rest.SendApplicationMailServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SendApplicationMailServletTest {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    MockedStatic<Transport> transport;
    private MockedStatic<UserAuthentification> userAuthentification;

    @BeforeEach
    public void init() {
        transport = mockStatic(Transport.class);
        transport.when(() -> Transport.send(any())).thenAnswer((Answer<Boolean>) invocation -> null);
        userAuthentification = mockStatic(UserAuthentification.class);
    }

    @AfterEach
    public void close() {
        transport.close();
        userAuthentification.close();
    }

    @Test
    void getMessageHasExpectedData() throws MessagingException, IOException {
        String expectedRecipient = "thomas.freytag@dhbw-karlsruhe.de";
        String expectedSubject = "Neue Bewerbung im Auslandssemesterportal";
        String expectedContent = "<h2>Sehr geehrter Herr Freytag"
                + ",</h2> es ist eine neue Bewerbung im Auslandssemesterportal."
                + "<br><br> "
                + "<a href= \"http://10.3.15.45/\" target=\"new\">Auslandssemesterportal</a>";

        Message actual = new SendApplicationMailServlet().getMessage();

        assertEquals(expectedRecipient, actual.getAllRecipients()[0].toString());
        assertEquals(expectedSubject, actual.getSubject());
        assertEquals(expectedContent, actual.getContent());
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
    void testDoPost() throws IOException {
        // given
        userAuthentification.when(() -> isTestUser(any())).thenReturn(false);

        new SendApplicationMailServlet() {
            public void callProtectedMethod(HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        transport.verify(() -> Transport.send(any()), times(1));
    }
}

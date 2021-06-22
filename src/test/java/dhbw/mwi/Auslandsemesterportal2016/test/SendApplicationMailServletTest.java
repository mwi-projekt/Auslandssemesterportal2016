package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dhbw.mwi.Auslandsemesterportal2016.rest.SendApplicationMailServlet;

public class SendApplicationMailServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Transport> transport;

    // Initialization of necessary instances
    Boolean reachedSendMethod;

    @BeforeMethod
    public void init() {
        // Define necessary mock objects for mocking static methods
        transport = Mockito.mockStatic(Transport.class);

        // Define what happens when mocked method is called
        transport.when(() -> Transport.send(any())).thenAnswer((Answer<Boolean>) invocation -> null);
    }

    @AfterMethod
    public void close() {
        // Close mock objects for mocking static methods
        transport.close();
    }

    @Test
    public void testDoPost() throws IOException, MessagingException {
        // call protected doPost()-Method of RegisterServlet.class
        new SendApplicationMailServlet() {
            public SendApplicationMailServlet callProtectedMethod(HttpServletRequest request,
                    HttpServletResponse response) throws IOException {
                doPost(request, response);
                return this;
            }
        }.callProtectedMethod(request, response);

        // verify that method Transport.send() was called
        transport.verify(() -> Transport.send(any()));
    }
}

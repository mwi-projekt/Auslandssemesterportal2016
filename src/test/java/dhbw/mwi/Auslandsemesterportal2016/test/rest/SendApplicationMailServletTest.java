package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.rest.SendApplicationMailServlet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.mail.Transport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class SendApplicationMailServletTest {
    // Initialization of necessary mock objects for mocking instance methods
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    // Initialization of necessary mock objects for mocking static methods
    MockedStatic<Transport> transport;

    // Initialization of necessary instances
    Boolean reachedSendMethod;

    @BeforeEach
    public void init() {
        // Define necessary mock objects for mocking static methods
        transport = Mockito.mockStatic(Transport.class);

        // Define what happens when mocked method is called
        transport.when(() -> Transport.send(any())).thenAnswer((Answer<Boolean>) invocation -> null);
    }

    @AfterEach
    public void close() {
        // Close mock objects for mocking static methods
        transport.close();
    }

    @Disabled("Mails senden ist abgeklemmt")
    @Test
    void testDoPost() throws IOException {
        // call protected doPost()-Method of RegisterServlet.class
        new SendApplicationMailServlet() {
            public void callProtectedMethod(HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        // verify that method Transport.send() was called
        transport.verify(() -> Transport.send(any()));
    }
}

package dhbw.mwi.Auslandsemesterportal2016.db;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTEMAIL;
import static org.mockito.Mockito.*;

class login_dbTest {

    login_db underTest = new login_db();
    private DelegateExecution delegateExecution;
    private DelegateTask delegateTask;
    private Message message;
    private MockedStatic<Util> utilMockedStatic;
    private MockedStatic<Transport> transport;

    @BeforeEach
    void setUp() {
        delegateTask = mock(DelegateTask.class);
        delegateExecution = mock(DelegateExecution.class);

        message = mock(Message.class);
        utilMockedStatic = mockStatic(Util.class);
        transport = mockStatic(Transport.class);
    }

    @AfterEach
    void tearDown() {
        utilMockedStatic.close();
        transport.close();
    }

    @Test
    void notifies() {
        underTest.notify(delegateTask);
        verifyNoInteractions(delegateTask);
        System.out.println("Die Methode tut zur Zeit nichts");
    }

    @ParameterizedTest
    @ValueSource(strings = {"true;Eingereichte Bewerbung für Auslandssemester validiert", "edit;Bei der Validierung Ihrer Bewerbung ist ein Fehler aufgetreten"})
    void executeSuccessfullyWithTestUser(String validierungErfolgreich) throws MessagingException {
        when(delegateExecution.getVariable("bewEmail")).thenReturn(TESTEMAIL.toString());
        String isSuccessful = validierungErfolgreich.split(";")[0];
        when(delegateExecution.getVariable("validierungErfolgreich")).thenReturn(isSuccessful);
        String mailText = "dummy mail text";
        when(delegateExecution.getVariable("mailText")).thenReturn(mailText);

        utilMockedStatic.when(() -> Util.getEmailMessage(eq(TESTEMAIL.toString()), anyString())).thenReturn(message);

        underTest.execute(delegateExecution);

        String mailSubject = validierungErfolgreich.split(";")[1];
        utilMockedStatic.verify(() ->
                Util.getEmailMessage(TESTEMAIL.toString(), mailSubject),
                times(1));
        verify(message, times(1)).setContent(mailText, "text/plain; charset=UTF-8");
        transport.verifyNoInteractions();
    }

    @Test
    void executeSuccessfullyWithRealUser() throws MessagingException {
        when(delegateExecution.getVariable("bewEmail")).thenReturn("anyOtherRealMailAddress");
        when(delegateExecution.getVariable("validierungErfolgreich")).thenReturn("true");
        String mailText = "dummy mail text";
        when(delegateExecution.getVariable("mailText")).thenReturn(mailText);

        utilMockedStatic.when(() -> Util.getEmailMessage(eq("anyOtherRealMailAddress"), anyString())).thenReturn(message);

        underTest.execute(delegateExecution);

        utilMockedStatic.verify(() ->
                Util.getEmailMessage("anyOtherRealMailAddress", "Eingereichte Bewerbung für Auslandssemester validiert"),
                times(1));
        verify(message, times(1)).setContent(mailText, "text/plain; charset=UTF-8");
        transport.verify(() -> Transport.send(message), times(1));
    }
}
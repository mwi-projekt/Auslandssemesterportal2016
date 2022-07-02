package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTNACHNAME;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ProcessEngineExtension.class)
@Deployment(resources = {"standard.bpmn"})
class GetEmailTextServletTest {

    private ProcessEngine processEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private StringWriter writer;
    private RuntimeService runtimeService;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);

        runtimeService = processEngine.getRuntimeService();
    }

    @AfterEach
    void tearDown() throws IOException {
        userAuthentificationMockedStatic.close();
        writer.close();
        processEngine.close();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 3, 5})
    void doGetUnauthorizedRole(int role) throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        new GetEmailTextServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"instance_id", "validate"})
    void doGetParamMissing(String presentParam) throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);
        when(request.getParameter(presentParam)).thenReturn("any");

        new GetEmailTextServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false", "edit"})
    void doGetWhenRoleIs2(String validation) throws IOException {
        int role = 2;
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        String studentLastName = TESTNACHNAME.toString();
        List<String> unis = Arrays.asList("Schottland", "Finnland", "USA");
        String instanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceId, "bewNachname", studentLastName);
        runtimeService.setVariable(instanceId, "uni1", unis.get(0));
        runtimeService.setVariable(instanceId, "uni2", unis.get(1));
        runtimeService.setVariable(instanceId, "uni3", unis.get(2));

        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("validate")).thenReturn(validation);

        new GetEmailTextServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = selectEmailText(role, validation, studentLastName, unis);
        assertEquals(expected, writer.toString().trim());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false", "edit"})
    void doGetWhenRoleIs4(String validation) throws IOException {
        int role = 4;
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(role);

        String studentLastName = TESTNACHNAME.toString();
        List<String> unis = Arrays.asList("Schottland", "Finnland", "USA");
        String instanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceId, "bewNachname", studentLastName);
        runtimeService.setVariable(instanceId, "uni1", unis.get(0));
        runtimeService.setVariable(instanceId, "uni2", unis.get(1));
        runtimeService.setVariable(instanceId, "uni3", unis.get(2));

        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("validate")).thenReturn(validation);

        new GetEmailTextServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = selectEmailText(role, validation, studentLastName, unis);
        assertEquals(expected, writer.toString().trim());
    }

    @Test
    void doGetWhenRoleIs1() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        String instanceId = runtimeService.startProcessInstanceByKey("standard").getId();
        runtimeService.setVariable(instanceId, "bewNachname", TESTNACHNAME.toString());
        runtimeService.setVariable(instanceId, "uni1", "Schottland");
        runtimeService.setVariable(instanceId, "uni2", "Finnland");
        runtimeService.setVariable(instanceId, "uni3", "USA");

        when(request.getParameter("instance_id")).thenReturn(instanceId);
        when(request.getParameter("validate")).thenReturn("any");

        new GetEmailTextServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("", writer.toString().trim());
        verify(response, times(0)).sendError(anyInt());
        verify(response, times(0)).sendError(anyInt(), anyString());
    }

    private String selectEmailText(int role, String validation, String studentLastName, List<String> unis) {
        if (role == 2) {
            switch (validation) {
                case ("true"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot für die Universitäten: "
                        + "\n"+"- "+ unis.get(0) + "\n"+"- "+ unis.get(1) + "\n"+"- "+ unis.get(2) + "\n"+ "wurde erfolgreich an das Akademisches Auslandsamt versendet." + "\n" + "\n"
                        + "-- Platzhalter für Anmerkungen des Auslandsamts --" + "\n" + "\n"
                        + "Zeitnah nach der Anmeldefrist werden alle Bewerbungen gesichtet und entschieden, ob Sie in die engere Auswahl potentieller Bewerber kommen. Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per E-Mail über das Ergebnis informieren."
                        + "\n"
                        + " Bei Fragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de."
                        + "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren."
                        + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Akademisches Auslandsamt"
                        + "\n" + "\n" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                case ("false"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n"+"- "+ unis.get(0) + "\n"+"- "+ unis.get(1) + "\n"+"- "+ unis.get(2) + "\n"
                        + "\n"+ "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
                        + "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
                        + " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
                        + "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de." + "\n"
                        + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
                        + "Ihr Akademisches Auslandsamt" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                case ("edit"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n"+ "- "+ unis.get(0) + "\n"+"- "+ unis.get(1) + "\n"+"- "+ unis.get(2) + "\n"+ "\n"
                        + "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
                        + "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
                        + " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
                        + "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können. "
                        + "Bei Rückfragen melden Sie sich gerne unter internationaloffice@dhbw-karlsruhe.de." + "\n"
                        + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
                        + "Ihr Akademisches Auslandsamt" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                default: return null;
            }
        } else {
            switch (validation) {
                case ("true"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Herzlichen Glückwunsch! Ihre Bewerbung für das von Ihnen ausgewählte Auslandssemesterangebot für die Universitäten: " + "\n"+"- "+ unis.get(0)+ "\n"+"- "+ unis.get(1) + "\n"+"- "+unis.get(2) + "\n"+
                        "wurde erfolgreich durch ihre/n Studiengangsleiter/in validiert." + "\n" + "\n"
                        + "-- Platzhalter für Anmerkungen des Studiengangsleiters --" + "\n" + "\n" + "\n"
                        + "Im nächsten Schritt wird ihre Bewerbung an ein/e Mitarbeiter/in des Akademischen Auslandsamtes für einen weiteren Validierungsprozess übergeben."
                        + "\n"
                        + "Sobald dieser Prozess abgeschlossen ist, werden wir Sie schnellstmöglich per Email über das Ergebnis informieren. "
                        + "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
                        + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n" + "Ihr Studiengangsleiter/in" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                case ("false"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n"+"- "+ unis.get(0) + "\n"+"- "+ unis.get(1) + "\n"+"- "+unis.get(2) + "\n"+ "\n"
                        + "Leider konnte Ihre Bewerbung nicht berücksichtigt werden." + "\n" + "\n"
                        + "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
                        + " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
                        + "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
                        + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
                        + "Ihr Studiengangsleiter/in" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                case ("edit"): return "Sehr geehrte/r Herr/Frau " + studentLastName + (",") + "\n" + "\n"
                        + "Vielen Dank für Ihre eingereichte Bewerbung für die Universitäten: " + "\n"+ "- "+ unis.get(0) + "\n"+"- "+ unis.get(1) + "\n"+"- "+ unis.get(2) + "\n"+ "\n"
                        + "Leider wurden nicht alle Daten vollständig und/oder korrekt eingegeben." + "\n" + "\n"
                        + "Folgendes Problem hat sich ergeben: " + "\n " + "\n"
                        + " -- Platzhalter für Erläuterung des Problems -- " + "\n" + "\n"
                        + "Ihr Bewerbungsprozess wurde auf Anfang zurückgesetzt, damit Sie den Fehler beheben können. "
                        + "Bei Rückfragen melden Sie sich gerne unter thomas.freytag@dhbw-karlsruhe.de." + "\n"
                        + "Wir bitten um Ihr Verständnis." + "\n" + "\n" + "Mit freundlichen Grüßen," + "\n" + "\n"
                        + "Ihr Studiengangsleiter/in" + "\n" + "\n" + "************" + "\n"
                        + "Um das Auslandssemesterportal zu erreichen, müssen Sie sich im VPN der DHBW Karlsruhe befinden.";
                default: return null;
            }
        }
    }
}
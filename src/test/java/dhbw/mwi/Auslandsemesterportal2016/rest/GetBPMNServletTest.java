package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.FileCreator;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetBPMNServletTest {

    @TempDir
    File mockFile;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("model")).thenReturn("standard");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        userAuthentificationMockedStatic.close();
    }

    @Test
    void doGetUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(3);

        new GetBPMNServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doGetWithoutParam() throws IOException {
        when(request.getParameter("model")).thenReturn(null);

        new GetBPMNServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals(PARAMMISSING.toString(), writer.toString().trim());
    }

    @Test
    void doGetBMPNModel() throws IOException {
        new GetBPMNServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals(getStandardModel(), writer.toString().trim());
    }

    @Test
    void doGetFileNotFoundOrIsDirectory() throws IOException {
        MockedStatic<FileCreator> fileCreator = mockStatic(FileCreator.class);
        fileCreator.when(() -> FileCreator.getBPMNFile("standard")).thenReturn(mockFile);

        new GetBPMNServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        assertEquals("file not found", writer.toString().trim());
    }

    private String getStandardModel() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" id=\"Definitions_1\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"4.8.1\">\n" +
                "  <bpmn:collaboration id=\"Collaboration_1cl2lu7\">\n" +
                "    <bpmn:participant id=\"Participant_00ci3jw\" name=\"Standard\" processRef=\"standard\" />\n" +
                "  </bpmn:collaboration>\n" +
                "  <bpmn:process id=\"standard\" name=\"Bewerbungen Standard\" isExecutable=\"true\">\n" +
                "    <bpmn:laneSet>\n" +
                "      <bpmn:lane id=\"Lane_05ro1a0\" name=\"Studiengangsleiter\">\n" +
                "        <bpmn:flowNodeRef>studentBenachrichtigenSGL</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenValidierenSGL</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_1dolwiq</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "      <bpmn:lane id=\"Lane_190wn40\" name=\"Mitarbeiter AAA\">\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_Datenpruefung</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>studentBenachrichtigen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenValidieren</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>EndEvent_true</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>bestaetigungVersenden</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>EndEvent_false</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_0rz9mmk</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>abgeschlossen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>abgelehnt</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "      <bpmn:lane id=\"Lane_07n76w6\" name=\"Student\">\n" +
                "        <bpmn:flowNodeRef>StartEvent_1</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>downloadsAnbieten</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>universitaetAuswaehlen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenEingeben</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenEingebenUnt</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_CostaRicaUni</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_SpanischMutter</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>Gateway_1i3uw8o</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>spanischNotePruefen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>Gateway_0gik51m</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_EnglischStart</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>englischNotePruefen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_EnglischMutter</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_EnglischEnd</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>daadHochladen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>zustimmungHochladen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>dualisHochladen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>motivationHochladen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>abiturHochladen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenPruefen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>Activity_0kzd76q</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "    </bpmn:laneSet>\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Bewerbung starten\" camunda:formKey=\"app:start-form.html\">\n" +
                "      <bpmn:documentation>Auf Front-End ist ein \"Bewerbung\"-Button hinterlegt.</bpmn:documentation>\n" +
                "      <bpmn:outgoing>SequenceFlow_1jq11ro</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:userTask id=\"downloadsAnbieten\" name=\"Downloads anbieten\">\n" +
                "      <bpmn:incoming>SequenceFlow_1jq11ro</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_0env68s</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_1bz128o</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1qjobkf</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1bz128o\" sourceRef=\"studentBenachrichtigen\" targetRef=\"downloadsAnbieten\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0env68s\" sourceRef=\"studentBenachrichtigenSGL\" targetRef=\"downloadsAnbieten\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1jq11ro\" sourceRef=\"StartEvent_1\" targetRef=\"downloadsAnbieten\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1g3gfo6\" sourceRef=\"datenEingeben\" targetRef=\"datenEingebenUnt\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1qjobkf\" sourceRef=\"downloadsAnbieten\" targetRef=\"universitaetAuswaehlen\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_13o42g1\" sourceRef=\"universitaetAuswaehlen\" targetRef=\"datenEingeben\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1xa0u93\" sourceRef=\"datenEingebenUnt\" targetRef=\"ExclusiveGateway_CostaRicaUni\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_10tw9l0\" name=\"Nein\" sourceRef=\"ExclusiveGateway_CostaRicaUni\" targetRef=\"ExclusiveGateway_EnglischMutter\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${uni1 != \"Costa Rica Institute of Technology (Costa Rica)\" &amp;&amp; uni2 != \"Costa Rica Institute of Technology (Costa Rica)\" &amp;&amp; uni3 != \"Costa Rica Institute of Technology (Costa Rica)\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1scm3ub\" name=\"Ja\" sourceRef=\"ExclusiveGateway_CostaRicaUni\" targetRef=\"ExclusiveGateway_SpanischMutter\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${uni1 == \"Costa Rica Institute of Technology (Costa Rica)\" || uni2 == \"Costa Rica Institute of Technology (Costa Rica)\" || uni3 == \"Costa Rica Institute of Technology (Costa Rica)\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:userTask id=\"universitaetAuswaehlen\" name=\"Auswahl Universitäten\">\n" +
                "      <bpmn:incoming>Flow_1qjobkf</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_13o42g1</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"datenEingeben\" name=\"Daten eingeben Student\">\n" +
                "      <bpmn:incoming>Flow_13o42g1</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1g3gfo6</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"datenEingebenUnt\" name=\"Daten eingeben Unternehmen\">\n" +
                "      <bpmn:incoming>SequenceFlow_1g3gfo6</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1xa0u93</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_CostaRicaUni\" name=\"Costa Rica\">\n" +
                "      <bpmn:incoming>Flow_1xa0u93</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_10tw9l0</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_1scm3ub</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_SpanischMutter\" name=\"Spanisch = Muttersprache?\">\n" +
                "      <bpmn:incoming>Flow_1scm3ub</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1wdddko</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_1wyjrtr</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_Datenpruefung\" name=\"Datenprüfung erfolgreich?\" default=\"SequenceFlow_0kpssuu\">\n" +
                "      <bpmn:incoming>SequenceFlow_1coz885</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1wg9408</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_09m6nu0</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_0kpssuu</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sendTask id=\"studentBenachrichtigen\" name=\"Student benachrichtigen\" camunda:class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\">\n" +
                "      <bpmn:documentation>Student wird über Ablehnung informiert.</bpmn:documentation>\n" +
                "      <bpmn:incoming>SequenceFlow_1wg9408</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1bz128o</bpmn:outgoing>\n" +
                "    </bpmn:sendTask>\n" +
                "    <bpmn:userTask id=\"datenValidieren\" name=\"Daten validieren\" camunda:formKey=\"embedded:app:forms/task-form.html\" camunda:assignee=\"mitarbeiterAAA\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:taskListener class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\" event=\"assignment\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_0trilik</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1coz885</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:endEvent id=\"EndEvent_true\" name=\"Bewerbung erfolgreich abgeschlossen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0fihakq</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sendTask id=\"bestaetigungVersenden\" name=\"Bestätigungs-email versenden\" camunda:class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\">\n" +
                "      <bpmn:documentation>Student wird über Annahme bestätigt.</bpmn:documentation>\n" +
                "      <bpmn:incoming>SequenceFlow_09m6nu0</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1gd2a3p</bpmn:outgoing>\n" +
                "    </bpmn:sendTask>\n" +
                "    <bpmn:endEvent id=\"EndEvent_false\" name=\"Bewerbung erfolglos abgeschlossen\">\n" +
                "      <bpmn:incoming>SequenceFlow_1jjp43i</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_0rz9mmk\">\n" +
                "      <bpmn:incoming>SequenceFlow_0kpssuu</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_0xy7yr5</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1o4gvd7</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:userTask id=\"abgeschlossen\" name=\"Prozess abgeschlossen\">\n" +
                "      <bpmn:incoming>SequenceFlow_1gd2a3p</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0fihakq</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"abgelehnt\" name=\"Bewerbung abgelehnt\">\n" +
                "      <bpmn:incoming>SequenceFlow_1o4gvd7</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1jjp43i</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sendTask id=\"studentBenachrichtigenSGL\" name=\"Student benachrichtigen\" camunda:class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\">\n" +
                "      <bpmn:documentation>Student wird über Ablehnung informiert.</bpmn:documentation>\n" +
                "      <bpmn:incoming>SequenceFlow_0ji9cm2</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0env68s</bpmn:outgoing>\n" +
                "    </bpmn:sendTask>\n" +
                "    <bpmn:userTask id=\"datenValidierenSGL\" name=\"Daten validieren\" camunda:formKey=\"embedded:app:forms/task-form.html\" camunda:assignee=\"mitarbeiterAAA\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:taskListener class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\" event=\"assignment\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_0cmmyvv</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1dff142</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1dolwiq\" name=\"Bewerbungsprüfung Ergebnis?\" default=\"SequenceFlow_0xy7yr5\">\n" +
                "      <bpmn:incoming>SequenceFlow_1dff142</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0ji9cm2</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_0trilik</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_0xy7yr5</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0cmmyvv\" sourceRef=\"datenPruefen\" targetRef=\"datenValidierenSGL\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1coz885\" sourceRef=\"datenValidieren\" targetRef=\"ExclusiveGateway_Datenpruefung\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0kpssuu\" name=\"Ablehnen\" sourceRef=\"ExclusiveGateway_Datenpruefung\" targetRef=\"ExclusiveGateway_0rz9mmk\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_09m6nu0\" name=\"Annehmen\" sourceRef=\"ExclusiveGateway_Datenpruefung\" targetRef=\"bestaetigungVersenden\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1wg9408\" name=\"Zur Bearbeitung freigeben\" sourceRef=\"ExclusiveGateway_Datenpruefung\" targetRef=\"studentBenachrichtigen\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${!validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0trilik\" name=\"Annehmen\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"datenValidieren\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0fihakq\" sourceRef=\"abgeschlossen\" targetRef=\"EndEvent_true\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1gd2a3p\" sourceRef=\"bestaetigungVersenden\" targetRef=\"abgeschlossen\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1jjp43i\" sourceRef=\"abgelehnt\" targetRef=\"EndEvent_false\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0xy7yr5\" name=\"Ablehnen\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"ExclusiveGateway_0rz9mmk\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1o4gvd7\" sourceRef=\"ExclusiveGateway_0rz9mmk\" targetRef=\"abgelehnt\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0ji9cm2\" name=\"Zur Bearbeitung freigeben\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"studentBenachrichtigenSGL\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${!validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1dff142\" sourceRef=\"datenValidierenSGL\" targetRef=\"ExclusiveGateway_1dolwiq\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1wdddko\" name=\"Nein\" sourceRef=\"ExclusiveGateway_SpanischMutter\" targetRef=\"spanischNotePruefen\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${muttersprache != \"Spanisch\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_1i3uw8o\" name=\"Unis außer Costa-Rica\">\n" +
                "      <bpmn:incoming>Flow_1oc2qvr</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_1wyjrtr</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_0j8t8es</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0nhtml8</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0pixbq5</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0nhtml8\" name=\"Ja\" sourceRef=\"Gateway_1i3uw8o\" targetRef=\"ExclusiveGateway_EnglischMutter\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${uni1 != \"Costa Rica Institute of Technology (Costa Rica)\" || uni2 != \"Costa Rica Institute of Technology (Costa Rica)\" || uni3 != \"Costa Rica Institute of Technology (Costa Rica)\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0pixbq5\" name=\"Nein\" sourceRef=\"Gateway_1i3uw8o\" targetRef=\"ExclusiveGateway_EnglischEnd\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${uni2 == \"\" || uni2 == null}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1oc2qvr\" sourceRef=\"Activity_0kzd76q\" targetRef=\"Gateway_1i3uw8o\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1wyjrtr\" name=\"Ja\" sourceRef=\"ExclusiveGateway_SpanischMutter\" targetRef=\"Gateway_1i3uw8o\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${muttersprache == \"Spanisch\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:userTask id=\"spanischNotePruefen\" name=\"Spanischnote eintragen\">\n" +
                "      <bpmn:incoming>Flow_1wdddko</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0qf2hjf</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_0gik51m\" name=\"Spanischnote &#62;= 11 Punkte?\">\n" +
                "      <bpmn:incoming>Flow_0qf2hjf</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0p0p42q</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0j8t8es</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0qf2hjf\" sourceRef=\"spanischNotePruefen\" targetRef=\"Gateway_0gik51m\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0p0p42q\" name=\"Nein\" sourceRef=\"Gateway_0gik51m\" targetRef=\"Activity_0kzd76q\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${spanischNote&lt;11}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_EnglischStart\" name=\"Englischnote &#62;= 11 Punkte?\">\n" +
                "      <bpmn:incoming>SequenceFlow_1p3sxt6</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1d0ul03</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_0d39a82</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:userTask id=\"englischNotePruefen\" name=\"Englischnote eintragen\">\n" +
                "      <bpmn:incoming>SequenceFlow_12my7xt</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1p3sxt6</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_EnglischMutter\" name=\"Englisch = Muttersprache?\">\n" +
                "      <bpmn:incoming>Flow_10tw9l0</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_0nhtml8</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_06jsa12</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_12my7xt</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1p3sxt6\" sourceRef=\"englischNotePruefen\" targetRef=\"ExclusiveGateway_EnglischStart\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1d0ul03\" name=\"Nein\" sourceRef=\"ExclusiveGateway_EnglischStart\" targetRef=\"daadHochladen\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${englischNote&lt;11}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0d39a82\" name=\"Ja\" sourceRef=\"ExclusiveGateway_EnglischStart\" targetRef=\"ExclusiveGateway_EnglischEnd\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${englischNote&gt;=11}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_06jsa12\" name=\"Ja\" sourceRef=\"ExclusiveGateway_EnglischMutter\" targetRef=\"ExclusiveGateway_EnglischEnd\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${muttersprache == \"Englisch\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1tpgdb5\" sourceRef=\"daadHochladen\" targetRef=\"ExclusiveGateway_EnglischEnd\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0tdd3mj\" sourceRef=\"ExclusiveGateway_EnglischEnd\" targetRef=\"zustimmungHochladen\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_12my7xt\" name=\"Nein\" sourceRef=\"ExclusiveGateway_EnglischMutter\" targetRef=\"englischNotePruefen\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${muttersprache != \"Englisch\"}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_EnglischEnd\">\n" +
                "      <bpmn:incoming>SequenceFlow_0d39a82</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_1tpgdb5</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_06jsa12</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_0pixbq5</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0tdd3mj</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:userTask id=\"daadHochladen\" name=\"Englisch DAAD-Formular hochladen\">\n" +
                "      <bpmn:incoming>SequenceFlow_1d0ul03</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1tpgdb5</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"zustimmungHochladen\" name=\"Anmeldeformular hochladen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0tdd3mj</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0yksv3e</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"dualisHochladen\" name=\"Dualisauszug hochladen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0yksv3e</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_06b7u3b</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"motivationHochladen\" name=\"Motivations-schreiben hochladen\">\n" +
                "      <bpmn:incoming>SequenceFlow_06b7u3b</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0r1y3rt</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"abiturHochladen\" name=\"Abiturzeugnis hochladen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0r1y3rt</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0h9aw8k</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"datenPruefen\" name=\"Daten prüfen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0h9aw8k</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0cmmyvv</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0yksv3e\" sourceRef=\"zustimmungHochladen\" targetRef=\"dualisHochladen\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_06b7u3b\" sourceRef=\"dualisHochladen\" targetRef=\"motivationHochladen\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0r1y3rt\" sourceRef=\"motivationHochladen\" targetRef=\"abiturHochladen\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0h9aw8k\" sourceRef=\"abiturHochladen\" targetRef=\"datenPruefen\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0j8t8es\" sourceRef=\"Gateway_0gik51m\" targetRef=\"Gateway_1i3uw8o\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${spanischNote&gt;=11}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:userTask id=\"Activity_0kzd76q\" name=\"Spanisch DAAD-Formular hochladen\">\n" +
                "      <bpmn:incoming>Flow_0p0p42q</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1oc2qvr</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Collaboration_1cl2lu7\">\n" +
                "      <bpmndi:BPMNShape id=\"Participant_00ci3jw_di\" bpmnElement=\"Participant_00ci3jw\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"156\" y=\"80\" width=\"3336\" height=\"1000\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_07n76w6_di\" bpmnElement=\"Lane_07n76w6\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"80\" width=\"3306\" height=\"382\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_190wn40_di\" bpmnElement=\"Lane_190wn40\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"697\" width=\"3306\" height=\"383\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_05ro1a0_di\" bpmnElement=\"Lane_05ro1a0\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"462\" width=\"3306\" height=\"235\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0j8t8es_di\" bpmnElement=\"Flow_0j8t8es\">\n" +
                "        <di:waypoint x=\"1240\" y=\"405\" />\n" +
                "        <di:waypoint x=\"1240\" y=\"450\" />\n" +
                "        <di:waypoint x=\"1540\" y=\"450\" />\n" +
                "        <di:waypoint x=\"1540\" y=\"405\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0h9aw8k_di\" bpmnElement=\"SequenceFlow_0h9aw8k\">\n" +
                "        <di:waypoint x=\"2857\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2908\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3448\" y=\"103\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0r1y3rt_di\" bpmnElement=\"SequenceFlow_0r1y3rt\">\n" +
                "        <di:waypoint x=\"2702\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2757\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3270\" y=\"118\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_06b7u3b_di\" bpmnElement=\"SequenceFlow_06b7u3b\">\n" +
                "        <di:waypoint x=\"2551\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2602\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3180\" y=\"118\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0yksv3e_di\" bpmnElement=\"SequenceFlow_0yksv3e\">\n" +
                "        <di:waypoint x=\"2384\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2451\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2593\" y=\"118\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_12my7xt_di\" bpmnElement=\"SequenceFlow_12my7xt\">\n" +
                "        <di:waypoint x=\"1793\" y=\"268\" />\n" +
                "        <di:waypoint x=\"1856\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1814\" y=\"247\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0tdd3mj_di\" bpmnElement=\"SequenceFlow_0tdd3mj\">\n" +
                "        <di:waypoint x=\"2231\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2284\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2730\" y=\"139\" width=\"90\" height=\"12\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1tpgdb5_di\" bpmnElement=\"SequenceFlow_1tpgdb5\">\n" +
                "        <di:waypoint x=\"2206\" y=\"308\" />\n" +
                "        <di:waypoint x=\"2206\" y=\"335\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2578\" y=\"123\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_06jsa12_di\" bpmnElement=\"SequenceFlow_06jsa12\">\n" +
                "        <di:waypoint x=\"1768\" y=\"293\" />\n" +
                "        <di:waypoint x=\"1768\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2181\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1779\" y=\"337\" width=\"12\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0d39a82_di\" bpmnElement=\"SequenceFlow_0d39a82\">\n" +
                "        <di:waypoint x=\"2043\" y=\"293\" />\n" +
                "        <di:waypoint x=\"2043\" y=\"360\" />\n" +
                "        <di:waypoint x=\"2181\" y=\"360\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2053\" y=\"342\" width=\"12\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1d0ul03_di\" bpmnElement=\"SequenceFlow_1d0ul03\">\n" +
                "        <di:waypoint x=\"2068\" y=\"268\" />\n" +
                "        <di:waypoint x=\"2156\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2099\" y=\"245\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1p3sxt6_di\" bpmnElement=\"SequenceFlow_1p3sxt6\">\n" +
                "        <di:waypoint x=\"1956\" y=\"268\" />\n" +
                "        <di:waypoint x=\"2018\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2374\" y=\"107\" width=\"90\" height=\"12\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0p0p42q_di\" bpmnElement=\"Flow_0p0p42q\">\n" +
                "        <di:waypoint x=\"1265\" y=\"380\" />\n" +
                "        <di:waypoint x=\"1340\" y=\"380\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1292\" y=\"362\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0qf2hjf_di\" bpmnElement=\"Flow_0qf2hjf\">\n" +
                "        <di:waypoint x=\"1170\" y=\"380\" />\n" +
                "        <di:waypoint x=\"1215\" y=\"380\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1wyjrtr_di\" bpmnElement=\"Flow_1wyjrtr\">\n" +
                "        <di:waypoint x=\"970\" y=\"405\" />\n" +
                "        <di:waypoint x=\"970\" y=\"450\" />\n" +
                "        <di:waypoint x=\"1540\" y=\"450\" />\n" +
                "        <di:waypoint x=\"1540\" y=\"405\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1249\" y=\"432\" width=\"12\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1oc2qvr_di\" bpmnElement=\"Flow_1oc2qvr\">\n" +
                "        <di:waypoint x=\"1440\" y=\"380\" />\n" +
                "        <di:waypoint x=\"1515\" y=\"380\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0pixbq5_di\" bpmnElement=\"Flow_0pixbq5\">\n" +
                "        <di:waypoint x=\"1565\" y=\"380\" />\n" +
                "        <di:waypoint x=\"2160\" y=\"380\" />\n" +
                "        <di:waypoint x=\"2189\" y=\"368\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2158\" y=\"393\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0nhtml8_di\" bpmnElement=\"Flow_0nhtml8\">\n" +
                "        <di:waypoint x=\"1557\" y=\"372\" />\n" +
                "        <di:waypoint x=\"1751\" y=\"276\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1641\" y=\"337\" width=\"12\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1wdddko_di\" bpmnElement=\"Flow_1wdddko\">\n" +
                "        <di:waypoint x=\"995\" y=\"380\" />\n" +
                "        <di:waypoint x=\"1070\" y=\"380\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1035\" y=\"363\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1dff142_di\" bpmnElement=\"SequenceFlow_1dff142\">\n" +
                "        <di:waypoint x=\"2770\" y=\"560\" />\n" +
                "        <di:waypoint x=\"2820\" y=\"560\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0ji9cm2_di\" bpmnElement=\"SequenceFlow_0ji9cm2\">\n" +
                "        <di:waypoint x=\"2845\" y=\"585\" />\n" +
                "        <di:waypoint x=\"2845\" y=\"670\" />\n" +
                "        <di:waypoint x=\"2665\" y=\"670\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2741\" y=\"627\" width=\"78\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1o4gvd7_di\" bpmnElement=\"SequenceFlow_1o4gvd7\">\n" +
                "        <di:waypoint x=\"3109\" y=\"889\" />\n" +
                "        <di:waypoint x=\"3192\" y=\"889\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0xy7yr5_di\" bpmnElement=\"SequenceFlow_0xy7yr5\">\n" +
                "        <di:waypoint x=\"2845\" y=\"535\" />\n" +
                "        <di:waypoint x=\"2845\" y=\"520\" />\n" +
                "        <di:waypoint x=\"3084\" y=\"520\" />\n" +
                "        <di:waypoint x=\"3084\" y=\"864\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2848\" y=\"502\" width=\"46\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1jjp43i_di\" bpmnElement=\"SequenceFlow_1jjp43i\">\n" +
                "        <di:waypoint x=\"3292\" y=\"889\" />\n" +
                "        <di:waypoint x=\"3356\" y=\"889\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1gd2a3p_di\" bpmnElement=\"SequenceFlow_1gd2a3p\">\n" +
                "        <di:waypoint x=\"3073\" y=\"980\" />\n" +
                "        <di:waypoint x=\"3192\" y=\"980\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0fihakq_di\" bpmnElement=\"SequenceFlow_0fihakq\">\n" +
                "        <di:waypoint x=\"3292\" y=\"980\" />\n" +
                "        <di:waypoint x=\"3356\" y=\"980\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0trilik_di\" bpmnElement=\"SequenceFlow_0trilik\">\n" +
                "        <di:waypoint x=\"2870\" y=\"560\" />\n" +
                "        <di:waypoint x=\"2967\" y=\"560\" />\n" +
                "        <di:waypoint x=\"2967\" y=\"752\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2893\" y=\"542\" width=\"53\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1wg9408_di\" bpmnElement=\"SequenceFlow_1wg9408\">\n" +
                "        <di:waypoint x=\"2903\" y=\"889\" />\n" +
                "        <di:waypoint x=\"2737\" y=\"889\" />\n" +
                "        <di:waypoint x=\"2737\" y=\"923\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2768\" y=\"856\" width=\"78\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_09m6nu0_di\" bpmnElement=\"SequenceFlow_09m6nu0\">\n" +
                "        <di:waypoint x=\"2928\" y=\"914\" />\n" +
                "        <di:waypoint x=\"2928\" y=\"980\" />\n" +
                "        <di:waypoint x=\"2973\" y=\"980\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2862\" y=\"944\" width=\"53\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0kpssuu_di\" bpmnElement=\"SequenceFlow_0kpssuu\">\n" +
                "        <di:waypoint x=\"2953\" y=\"889\" />\n" +
                "        <di:waypoint x=\"3059\" y=\"889\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2983\" y=\"871\" width=\"46\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1coz885_di\" bpmnElement=\"SequenceFlow_1coz885\">\n" +
                "        <di:waypoint x=\"2967\" y=\"832\" />\n" +
                "        <di:waypoint x=\"2967\" y=\"848\" />\n" +
                "        <di:waypoint x=\"2928\" y=\"848\" />\n" +
                "        <di:waypoint x=\"2928\" y=\"864\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2845\" y=\"303\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0cmmyvv_di\" bpmnElement=\"SequenceFlow_0cmmyvv\">\n" +
                "        <di:waypoint x=\"2958\" y=\"400\" />\n" +
                "        <di:waypoint x=\"2958\" y=\"445\" />\n" +
                "        <di:waypoint x=\"2720\" y=\"445\" />\n" +
                "        <di:waypoint x=\"2720\" y=\"520\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1scm3ub_di\" bpmnElement=\"Flow_1scm3ub\">\n" +
                "        <di:waypoint x=\"880\" y=\"293\" />\n" +
                "        <di:waypoint x=\"880\" y=\"380\" />\n" +
                "        <di:waypoint x=\"945\" y=\"380\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"889\" y=\"334\" width=\"12\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_10tw9l0_di\" bpmnElement=\"Flow_10tw9l0\">\n" +
                "        <di:waypoint x=\"905\" y=\"268\" />\n" +
                "        <di:waypoint x=\"1743\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1315\" y=\"250\" width=\"23\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1xa0u93_di\" bpmnElement=\"Flow_1xa0u93\">\n" +
                "        <di:waypoint x=\"820\" y=\"268\" />\n" +
                "        <di:waypoint x=\"855\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_13o42g1_di\" bpmnElement=\"Flow_13o42g1\">\n" +
                "        <di:waypoint x=\"560\" y=\"268\" />\n" +
                "        <di:waypoint x=\"590\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1qjobkf_di\" bpmnElement=\"Flow_1qjobkf\">\n" +
                "        <di:waypoint x=\"432\" y=\"268\" />\n" +
                "        <di:waypoint x=\"460\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1g3gfo6_di\" bpmnElement=\"SequenceFlow_1g3gfo6\">\n" +
                "        <di:waypoint x=\"690\" y=\"268\" />\n" +
                "        <di:waypoint x=\"720\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1585.5\" y=\"107\" width=\"0\" height=\"12\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1jq11ro_di\" bpmnElement=\"SequenceFlow_1jq11ro\">\n" +
                "        <di:waypoint x=\"282\" y=\"268\" />\n" +
                "        <di:waypoint x=\"332\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1244.5\" y=\"118\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0env68s_di\" bpmnElement=\"SequenceFlow_0env68s\">\n" +
                "        <di:waypoint x=\"2565\" y=\"670\" />\n" +
                "        <di:waypoint x=\"412\" y=\"670\" />\n" +
                "        <di:waypoint x=\"412\" y=\"308\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1bz128o_di\" bpmnElement=\"SequenceFlow_1bz128o\">\n" +
                "        <di:waypoint x=\"2687\" y=\"963\" />\n" +
                "        <di:waypoint x=\"366\" y=\"963\" />\n" +
                "        <di:waypoint x=\"363\" y=\"308\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"246\" y=\"250\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"236\" y=\"286\" width=\"56\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0ds5skw_di\" bpmnElement=\"downloadsAnbieten\">\n" +
                "        <dc:Bounds x=\"332\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_14etfl9_di\" bpmnElement=\"universitaetAuswaehlen\">\n" +
                "        <dc:Bounds x=\"460\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1fwkyh9_di\" bpmnElement=\"datenEingeben\">\n" +
                "        <dc:Bounds x=\"590\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_138etgs_di\" bpmnElement=\"datenEingebenUnt\">\n" +
                "        <dc:Bounds x=\"720\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_1fcalg0_di\" bpmnElement=\"ExclusiveGateway_CostaRicaUni\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"855\" y=\"243\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"853\" y=\"219\" width=\"54\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_179nizr_di\" bpmnElement=\"ExclusiveGateway_SpanischMutter\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"945\" y=\"355\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"931\" y=\"317.5\" width=\"77\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_1mtx0ph_di\" bpmnElement=\"ExclusiveGateway_Datenpruefung\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"2903\" y=\"864\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2971\" y=\"842\" width=\"67\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"SendTask_1536vi5_di\" bpmnElement=\"studentBenachrichtigen\">\n" +
                "        <dc:Bounds x=\"2687\" y=\"923\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_03enxy9_di\" bpmnElement=\"datenValidieren\">\n" +
                "        <dc:Bounds x=\"2917\" y=\"752\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_0b01b6y_di\" bpmnElement=\"EndEvent_true\">\n" +
                "        <dc:Bounds x=\"3356\" y=\"962\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3337\" y=\"1008\" width=\"74\" height=\"40\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"SendTask_14eopq4_di\" bpmnElement=\"bestaetigungVersenden\">\n" +
                "        <dc:Bounds x=\"2973\" y=\"940\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_18fm4ss_di\" bpmnElement=\"EndEvent_false\">\n" +
                "        <dc:Bounds x=\"3356\" y=\"871\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3337\" y=\"914\" width=\"74\" height=\"40\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_0rz9mmk_di\" bpmnElement=\"ExclusiveGateway_0rz9mmk\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"3059\" y=\"864\" width=\"50\" height=\"50\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1yss45q_di\" bpmnElement=\"abgeschlossen\">\n" +
                "        <dc:Bounds x=\"3192\" y=\"940\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_16m5rie_di\" bpmnElement=\"abgelehnt\">\n" +
                "        <dc:Bounds x=\"3192\" y=\"849\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"SendTask_026l78y_di\" bpmnElement=\"studentBenachrichtigenSGL\">\n" +
                "        <dc:Bounds x=\"2565\" y=\"630\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_12abxr2_di\" bpmnElement=\"datenValidierenSGL\">\n" +
                "        <dc:Bounds x=\"2670\" y=\"520\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_1dolwiq_di\" bpmnElement=\"ExclusiveGateway_1dolwiq\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"2820\" y=\"535\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2861\" y=\"576\" width=\"87\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_1i3uw8o_di\" bpmnElement=\"Gateway_1i3uw8o\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"1515\" y=\"355\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1512\" y=\"327\" width=\"56\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0i32dnj_di\" bpmnElement=\"spanischNotePruefen\">\n" +
                "        <dc:Bounds x=\"1070\" y=\"340\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Gateway_0gik51m_di\" bpmnElement=\"Gateway_0gik51m\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"1215\" y=\"355\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1199\" y=\"327\" width=\"84\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_1prf2rl_di\" bpmnElement=\"ExclusiveGateway_EnglischStart\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"2018\" y=\"243\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2003\" y=\"205\" width=\"80\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0yw3zji_di\" bpmnElement=\"englischNotePruefen\">\n" +
                "        <dc:Bounds x=\"1856\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_175drvz_di\" bpmnElement=\"ExclusiveGateway_EnglischMutter\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"1743\" y=\"243\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1730\" y=\"206\" width=\"77\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_0uvn9zu_di\" bpmnElement=\"ExclusiveGateway_EnglischEnd\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"2181\" y=\"335\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2673\" y=\"176\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0b7eb4e_di\" bpmnElement=\"daadHochladen\">\n" +
                "        <dc:Bounds x=\"2156\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0tv0gv6_di\" bpmnElement=\"zustimmungHochladen\">\n" +
                "        <dc:Bounds x=\"2284\" y=\"319\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_08zndvo_di\" bpmnElement=\"dualisHochladen\">\n" +
                "        <dc:Bounds x=\"2451\" y=\"320\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1xluwsi_di\" bpmnElement=\"motivationHochladen\">\n" +
                "        <dc:Bounds x=\"2602\" y=\"320\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_03wdbk0_di\" bpmnElement=\"abiturHochladen\">\n" +
                "        <dc:Bounds x=\"2757\" y=\"320\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0dqmp1o_di\" bpmnElement=\"datenPruefen\">\n" +
                "        <dc:Bounds x=\"2908\" y=\"320\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0422r2f_di\" bpmnElement=\"Activity_0kzd76q\">\n" +
                "        <dc:Bounds x=\"1340\" y=\"340\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>";
    }
}
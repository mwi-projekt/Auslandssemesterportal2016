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

import static dhbw.mwi.Auslandsemesterportal2016.db.FileCreator.getBPMNFile;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetBPMNServletTest {

    @TempDir
    File mockedEmptyFile;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private MockedStatic<FileCreator> fileCreatorMockedStatic;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("model")).thenReturn("standard");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        fileCreatorMockedStatic = mockStatic(FileCreator.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        userAuthentificationMockedStatic.close();
        fileCreatorMockedStatic.close();
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
        fileCreatorMockedStatic.when(() -> getBPMNFile(anyString(), any())).thenCallRealMethod();
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
        fileCreatorMockedStatic.when(() -> getBPMNFile(anyString(), any())).thenReturn(mockedEmptyFile);

        new GetBPMNServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doGet(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("file not found", writer.toString().trim());
        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
    }

    private String getStandardModel() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" id=\"Definitions_1\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"5.0.0\">\n" +
                "  <bpmn:collaboration id=\"Collaboration_1cl2lu7\">\n" +
                "    <bpmn:participant id=\"Participant_00ci3jw\" name=\"Standard\" processRef=\"standard\" />\n" +
                "  </bpmn:collaboration>\n" +
                "  <bpmn:process id=\"standard\" name=\"Bewerbungen Standard\" isExecutable=\"true\">\n" +
                "    <bpmn:laneSet>\n" +
                "      <bpmn:lane id=\"Lane_05ro1a0\" name=\"Studiengangsleiter\">\n" +
                "        <bpmn:flowNodeRef>ExclusiveGateway_1dolwiq</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenValidierenSGL</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>studentBenachrichtigenSGL</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "      <bpmn:lane id=\"Lane_190wn40\" name=\"Mitarbeiter AAA\">\n" +
                "        <bpmn:flowNodeRef>goOutUebergeben</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>abgelehnt</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>abgeschlossen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>EndEvent_false</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>EndEvent_true</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "      <bpmn:lane id=\"Lane_07n76w6\" name=\"Student\">\n" +
                "        <bpmn:flowNodeRef>StartEvent_1</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>downloadsAnbieten</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenPruefen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>universitaetAuswaehlen</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenEingeben</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>datenEingebenUnt</bpmn:flowNodeRef>\n" +
                "        <bpmn:flowNodeRef>dualisHochladen</bpmn:flowNodeRef>\n" +
                "      </bpmn:lane>\n" +
                "    </bpmn:laneSet>\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Bewerbung starten\" camunda:formKey=\"app:start-form.html\">\n" +
                "      <bpmn:documentation>Auf Front-End ist ein \"Bewerbung\"-Button hinterlegt.</bpmn:documentation>\n" +
                "      <bpmn:outgoing>SequenceFlow_1jq11ro</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:userTask id=\"downloadsAnbieten\" name=\"Downloads anbieten\">\n" +
                "      <bpmn:incoming>SequenceFlow_1jq11ro</bpmn:incoming>\n" +
                "      <bpmn:incoming>SequenceFlow_0env68s</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1qjobkf</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0env68s\" sourceRef=\"studentBenachrichtigenSGL\" targetRef=\"downloadsAnbieten\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1jq11ro\" sourceRef=\"StartEvent_1\" targetRef=\"downloadsAnbieten\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1g3gfo6\" sourceRef=\"datenEingeben\" targetRef=\"datenEingebenUnt\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1qjobkf\" sourceRef=\"downloadsAnbieten\" targetRef=\"universitaetAuswaehlen\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_13o42g1\" sourceRef=\"universitaetAuswaehlen\" targetRef=\"datenEingeben\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_06b7u3b\" sourceRef=\"dualisHochladen\" targetRef=\"datenPruefen\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0fx55je\" sourceRef=\"datenEingebenUnt\" targetRef=\"dualisHochladen\" />\n" +
                "    <bpmn:serviceTask id=\"goOutUebergeben\" name=\"Daten an &#34;Go Out Formular&#34;  übergeben\" camunda:class=\"dhbw.mwi.Auslandsemesterportal2016.service.GoOutFormularService\">\n" +
                "      <bpmn:incoming>SequenceFlow_0trilik</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1x3as24</bpmn:outgoing>\n" +
                "    </bpmn:serviceTask>\n" +
                "    <bpmn:userTask id=\"abgelehnt\" name=\"Bewerbung abgelehnt\">\n" +
                "      <bpmn:incoming>Flow_0596pz2</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1jjp43i</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"abgeschlossen\" name=\"Prozess abgeschlossen\">\n" +
                "      <bpmn:incoming>Flow_1x3as24</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0fihakq</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:endEvent id=\"EndEvent_false\" name=\"Bewerbung erfolglos abgeschlossen\">\n" +
                "      <bpmn:incoming>SequenceFlow_1jjp43i</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:endEvent id=\"EndEvent_true\" name=\"Bewerbung erfolgreich abgeschlossen\">\n" +
                "      <bpmn:incoming>SequenceFlow_0fihakq</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1dolwiq\" name=\"Bewerbungs-prüfung Ergebnis?\">\n" +
                "      <bpmn:incoming>SequenceFlow_1dff142</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0ji9cm2</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>SequenceFlow_0trilik</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0596pz2</bpmn:outgoing>\n" +
                "    </bpmn:exclusiveGateway>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0cmmyvv\" sourceRef=\"datenPruefen\" targetRef=\"datenValidierenSGL\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1dff142\" sourceRef=\"datenValidierenSGL\" targetRef=\"ExclusiveGateway_1dolwiq\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0ji9cm2\" name=\"Zur Bearbeitung freigeben\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"studentBenachrichtigenSGL\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${!validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0trilik\" name=\"Annehmen\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"goOutUebergeben\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">${validierungErfolgreich}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1x3as24\" sourceRef=\"goOutUebergeben\" targetRef=\"abgeschlossen\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0596pz2\" name=\"Ablehnen\" sourceRef=\"ExclusiveGateway_1dolwiq\" targetRef=\"abgelehnt\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1jjp43i\" sourceRef=\"abgelehnt\" targetRef=\"EndEvent_false\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_0fihakq\" sourceRef=\"abgeschlossen\" targetRef=\"EndEvent_true\" />\n" +
                "    <bpmn:userTask id=\"datenPruefen\" name=\"Daten prüfen\">\n" +
                "      <bpmn:incoming>SequenceFlow_06b7u3b</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0cmmyvv</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
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
                "      <bpmn:outgoing>Flow_0fx55je</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"dualisHochladen\" name=\"Dualisauszug hochladen\">\n" +
                "      <bpmn:incoming>Flow_0fx55je</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_06b7u3b</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:userTask id=\"datenValidierenSGL\" name=\"Daten validieren\" camunda:formKey=\"embedded:app:forms/task-form.html\" camunda:assignee=\"mitarbeiterAAA\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <camunda:taskListener class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\" event=\"assignment\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "      <bpmn:incoming>SequenceFlow_0cmmyvv</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_1dff142</bpmn:outgoing>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:sendTask id=\"studentBenachrichtigenSGL\" name=\"Student benachrichtigen\" camunda:class=\"dhbw.mwi.Auslandsemesterportal2016.db.login_db\">\n" +
                "      <bpmn:documentation>Student wird über Ablehnung informiert.</bpmn:documentation>\n" +
                "      <bpmn:incoming>SequenceFlow_0ji9cm2</bpmn:incoming>\n" +
                "      <bpmn:outgoing>SequenceFlow_0env68s</bpmn:outgoing>\n" +
                "    </bpmn:sendTask>\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Collaboration_1cl2lu7\">\n" +
                "      <bpmndi:BPMNShape id=\"Participant_00ci3jw_di\" bpmnElement=\"Participant_00ci3jw\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"156\" y=\"80\" width=\"2084\" height=\"1000\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_07n76w6_di\" bpmnElement=\"Lane_07n76w6\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"80\" width=\"2054\" height=\"382\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_190wn40_di\" bpmnElement=\"Lane_190wn40\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"697\" width=\"2054\" height=\"383\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Lane_05ro1a0_di\" bpmnElement=\"Lane_05ro1a0\" isHorizontal=\"true\">\n" +
                "        <dc:Bounds x=\"186\" y=\"462\" width=\"2054\" height=\"235\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_06b7u3b_di\" bpmnElement=\"SequenceFlow_06b7u3b\">\n" +
                "        <di:waypoint x=\"1120\" y=\"268\" />\n" +
                "        <di:waypoint x=\"1200\" y=\"268\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"3180\" y=\"118\" width=\"90\" height=\"20\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_13o42g1_di\" bpmnElement=\"Flow_13o42g1\">\n" +
                "        <di:waypoint x=\"590\" y=\"268\" />\n" +
                "        <di:waypoint x=\"660\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1qjobkf_di\" bpmnElement=\"Flow_1qjobkf\">\n" +
                "        <di:waypoint x=\"432\" y=\"268\" />\n" +
                "        <di:waypoint x=\"490\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1g3gfo6_di\" bpmnElement=\"SequenceFlow_1g3gfo6\">\n" +
                "        <di:waypoint x=\"760\" y=\"268\" />\n" +
                "        <di:waypoint x=\"830\" y=\"268\" />\n" +
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
                "        <di:waypoint x=\"1200\" y=\"640\" />\n" +
                "        <di:waypoint x=\"382\" y=\"640\" />\n" +
                "        <di:waypoint x=\"382\" y=\"310\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0fx55je_di\" bpmnElement=\"Flow_0fx55je\">\n" +
                "        <di:waypoint x=\"930\" y=\"268\" />\n" +
                "        <di:waypoint x=\"1020\" y=\"268\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0cmmyvv_di\" bpmnElement=\"SequenceFlow_0cmmyvv\">\n" +
                "        <di:waypoint x=\"1300\" y=\"269\" />\n" +
                "        <di:waypoint x=\"1468\" y=\"271\" />\n" +
                "        <di:waypoint x=\"1468\" y=\"520\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1dff142_di\" bpmnElement=\"SequenceFlow_1dff142\">\n" +
                "        <di:waypoint x=\"1518\" y=\"560\" />\n" +
                "        <di:waypoint x=\"1568\" y=\"560\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0ji9cm2_di\" bpmnElement=\"SequenceFlow_0ji9cm2\">\n" +
                "        <di:waypoint x=\"1593\" y=\"585\" />\n" +
                "        <di:waypoint x=\"1593\" y=\"640\" />\n" +
                "        <di:waypoint x=\"1300\" y=\"640\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1481\" y=\"606\" width=\"78\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0trilik_di\" bpmnElement=\"SequenceFlow_0trilik\">\n" +
                "        <di:waypoint x=\"1618\" y=\"560\" />\n" +
                "        <di:waypoint x=\"1678\" y=\"560\" />\n" +
                "        <di:waypoint x=\"1678\" y=\"770\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1682\" y=\"643\" width=\"53\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1x3as24_di\" bpmnElement=\"Flow_1x3as24\">\n" +
                "        <di:waypoint x=\"1678\" y=\"850\" />\n" +
                "        <di:waypoint x=\"1678\" y=\"980\" />\n" +
                "        <di:waypoint x=\"1940\" y=\"980\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0596pz2_di\" bpmnElement=\"Flow_0596pz2\">\n" +
                "        <di:waypoint x=\"1593\" y=\"530\" />\n" +
                "        <di:waypoint x=\"1593\" y=\"520\" />\n" +
                "        <di:waypoint x=\"1748\" y=\"520\" />\n" +
                "        <di:waypoint x=\"1748\" y=\"889\" />\n" +
                "        <di:waypoint x=\"1940\" y=\"889\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1755\" y=\"602\" width=\"46\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_1jjp43i_di\" bpmnElement=\"SequenceFlow_1jjp43i\">\n" +
                "        <di:waypoint x=\"2040\" y=\"889\" />\n" +
                "        <di:waypoint x=\"2104\" y=\"889\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"SequenceFlow_0fihakq_di\" bpmnElement=\"SequenceFlow_0fihakq\">\n" +
                "        <di:waypoint x=\"2040\" y=\"980\" />\n" +
                "        <di:waypoint x=\"2104\" y=\"980\" />\n" +
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
                "      <bpmndi:BPMNShape id=\"Activity_05xny7a_di\" bpmnElement=\"goOutUebergeben\">\n" +
                "        <dc:Bounds x=\"1628\" y=\"770\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_16m5rie_di\" bpmnElement=\"abgelehnt\">\n" +
                "        <dc:Bounds x=\"1940\" y=\"849\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1yss45q_di\" bpmnElement=\"abgeschlossen\">\n" +
                "        <dc:Bounds x=\"1940\" y=\"940\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_18fm4ss_di\" bpmnElement=\"EndEvent_false\">\n" +
                "        <dc:Bounds x=\"2104\" y=\"871\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2085\" y=\"914\" width=\"74\" height=\"40\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"EndEvent_0b01b6y_di\" bpmnElement=\"EndEvent_true\">\n" +
                "        <dc:Bounds x=\"2104\" y=\"962\" width=\"36\" height=\"36\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"2085\" y=\"1008\" width=\"74\" height=\"40\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"ExclusiveGateway_1dolwiq_di\" bpmnElement=\"ExclusiveGateway_1dolwiq\" isMarkerVisible=\"true\">\n" +
                "        <dc:Bounds x=\"1568\" y=\"535\" width=\"50\" height=\"50\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"1593\" y=\"576\" width=\"90\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_0dqmp1o_di\" bpmnElement=\"datenPruefen\">\n" +
                "        <dc:Bounds x=\"1200\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_14etfl9_di\" bpmnElement=\"universitaetAuswaehlen\">\n" +
                "        <dc:Bounds x=\"490\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_1fwkyh9_di\" bpmnElement=\"datenEingeben\">\n" +
                "        <dc:Bounds x=\"660\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_138etgs_di\" bpmnElement=\"datenEingebenUnt\">\n" +
                "        <dc:Bounds x=\"830\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_08zndvo_di\" bpmnElement=\"dualisHochladen\">\n" +
                "        <dc:Bounds x=\"1020\" y=\"228\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"UserTask_12abxr2_di\" bpmnElement=\"datenValidierenSGL\">\n" +
                "        <dc:Bounds x=\"1418\" y=\"520\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"SendTask_026l78y_di\" bpmnElement=\"studentBenachrichtigenSGL\">\n" +
                "        <dc:Bounds x=\"1200\" y=\"600\" width=\"100\" height=\"80\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>";
    }
}
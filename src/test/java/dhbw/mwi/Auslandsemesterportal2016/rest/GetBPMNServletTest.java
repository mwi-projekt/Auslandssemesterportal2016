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
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        String actual = writer.toString().trim();
        assertTrue(actual.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(actual.contains("<bpmn:process id=\"standard\" name=\"Bewerbungen Standard\" isExecutable=\"true\">"));
        assertTrue(actual.contains("</bpmn:definitions>"));
        assertTrue(actual.contains("</bpmn:startEvent"));
        assertTrue(actual.contains("</bpmn:endEvent>"));
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
}
package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.FileCreator;
import dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;

import static dhbw.mwi.Auslandsemesterportal2016.db.FileCreator.getFileInUploadFolder;
import static dhbw.mwi.Auslandsemesterportal2016.db.UserAuthentification.isUserAuthentifiedByCookie;
import static java.util.Collections.singletonList;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ModelUploadServletTest {

    @TempDir
    private File folder;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;
    private MockedStatic<UserAuthentification> userAuthentificationMockedStatic;
    private Part part;
    private MockedStatic<FileCreator> fileCreatorMockedStatic;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        userAuthentificationMockedStatic = mockStatic(UserAuthentification.class);
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(1);

        part = mock(Part.class);
        fileCreatorMockedStatic = mockStatic(FileCreator.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        userAuthentificationMockedStatic.close();
        fileCreatorMockedStatic.close();
    }

    @Test
    void doPostUnauthorizedRole() throws IOException {
        userAuthentificationMockedStatic.when(() -> isUserAuthentifiedByCookie(request)).thenReturn(2);

        new ModelUploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_UNAUTHORIZED);
    }

    @Test
    void doPostUploadPartMissing() throws IOException, ServletException {
        when(request.getPart("upload")).thenReturn(null);
        when(request.getParameter("CKEditorFuncNum")).thenReturn("any");

        new ModelUploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).setStatus(SC_INTERNAL_SERVER_ERROR);
        String expected = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(any, '', 'Datei fehlt');</script>\n";
        assertEquals(expected, writer.toString());
    }

    @Test
    void doPostExceptionWhileUploading() throws IOException, ServletException {
        when(request.getParameter("CKEditorFuncNum")).thenReturn("updatedAction");
        when(request.getPart("upload")).thenReturn(part);
        when(part.getHeader("content-disposition")).thenThrow(new RuntimeException());

        new ModelUploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(updatedAction, '', 'Server Fehler');</script>\n";
        assertEquals(expected, writer.toString());
    }

    @Test
    void doPostUploadSuccessfully() throws IOException, ServletException {
        when(request.getParameter("CKEditorFuncNum")).thenReturn("updatedAction");
        when(request.getPart("upload")).thenReturn(part);
        when(part.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[] {1}));

        // in dem neuen File steht "dummy text"
        File fileWithInput = new File("abc.txt");
        Files.write(fileWithInput.toPath(), singletonList("dummy text"));
        when(part.getInputStream()).thenReturn(Files.newInputStream(fileWithInput.toPath()));

        String fileName = "xyz.txt";
        when(part.getHeader("content-disposition")).thenReturn("its;a;test;filename=\"" + fileName + "\"");
        fileCreatorMockedStatic.when(() -> getFileInUploadFolder(fileName)).thenReturn(new File(folder, fileName));

        new ModelUploadServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        String expected = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(updatedAction, '"
                + Config.UPLOAD_URL + fileName + "', '');</script>\n";
        assertEquals(expected, writer.toString());
        assertEquals("dummy text", Files.readAllLines(new File(folder, fileName).toPath()).get(0));
    }
}
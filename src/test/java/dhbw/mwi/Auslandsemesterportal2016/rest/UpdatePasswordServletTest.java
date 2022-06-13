package dhbw.mwi.Auslandsemesterportal2016.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries.setPassword;
import static dhbw.mwi.Auslandsemesterportal2016.enums.ErrorEnum.PARAMMISSING;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UpdatePasswordServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private MockedStatic<SQLQueries> sqlQueriesMockedStatic;
    private StringWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("uuid")).thenReturn("anyUuid");
        when(request.getParameter("password")).thenReturn("anyPassword");

        response = mock(HttpServletResponse.class);
        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        sqlQueriesMockedStatic = mockStatic(SQLQueries.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        writer.close();
        sqlQueriesMockedStatic.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"uuid", "password"})
    void doPostParamMissing(String missingParam) throws IOException {
        when(request.getParameter(missingParam)).thenReturn(null);

        new UpdatePasswordServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        verify(response, times(1)).sendError(SC_BAD_REQUEST, PARAMMISSING.toString());
    }

    @Test
    void doPostChangePasswordSuccessfully() throws IOException {
        sqlQueriesMockedStatic.when(() -> setPassword(anyString(), anyString())).thenReturn(1);

        new UpdatePasswordServlet() {
            public void callProtectedMethod(HttpServletRequest request, HttpServletResponse response)
                    throws IOException {
                doPost(request, response);
            }
        }.callProtectedMethod(request, response);

        assertEquals("1", writer.toString().trim());
    }
}
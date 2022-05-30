package dhbw.mwi.Auslandsemesterportal2016.test.db;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.db.Mail;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import org.junit.jupiter.api.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static dhbw.mwi.Auslandsemesterportal2016.enums.MessageEnum.AAAREGISTR;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTEMAIL;
import static dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum.TESTPASSWORT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilTest {

    @Test
    void getEmailMessage() throws MessagingException {
        // given
        String expectedRecipient = TESTEMAIL.toString();
        String expectedFrom = "noreply@dhbw-karlsruhe.de";
        String expectedSubject = AAAREGISTR.toString();

        Message result = new MimeMessage(Mail.getInstance());
        // when
        try {
            result = Util.getEmailMessage(expectedRecipient, expectedSubject);
        } catch (MessagingException e) {
            fail();
        }

        // then
        assertNotNull(result);
        assertEquals(expectedRecipient, result.getAllRecipients()[0].toString());
        assertEquals(expectedFrom, result.getFrom()[0].toString());
        assertEquals(expectedSubject, result.getSubject());
    }

    @Test
    void verifyAddResponseHeaders() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = spy(HttpServletResponse.class);
        when(request.getHeader("Origin")).thenReturn(Config.URL);

        Util.addResponseHeaders(request, response);

        verify(response, times(5)).addHeader(anyString(), any());
        assertEquals(Config.URL, response.getHeader("Access-Control-Allow-Origin"));
    }

    @Test
    void generateSalt() {
        String result = Util.generateSalt();
        assertNotNull(result);
        assertEquals(64, result.length());
    }

    @Test
    void hashSha256() {
        String result = Util.hashSha256(TESTPASSWORT.toString());
        assertNotNull(result);
    }

    @Test
    void hashSha256InputNull() {
        assertNull(Util.hashSha256(null));
    }

    @Test
    void writeJsonWithResultSet() throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        HttpServletResponse response = spy(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(writer);

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(2);
        when(metaData.getColumnName(anyInt())).thenReturn("testParam");
        when(resultSet.getString(anyString())).thenReturn("testParamValue");

        Util.writeJson(response, resultSet);

        String expectedJson = "{\"data\":[{\"testParam\":null}]}";
        assertEquals(expectedJson, stringWriter.toString().trim());
    }
}
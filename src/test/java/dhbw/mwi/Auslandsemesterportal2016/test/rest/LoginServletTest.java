package dhbw.mwi.Auslandsemesterportal2016.test.rest;

import dhbw.mwi.Auslandsemesterportal2016.db.SQLQueries;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;
import dhbw.mwi.Auslandsemesterportal2016.rest.LoginServlet;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServletTest {

    /*
     * method verifies that doPost() is called
     * 
     * method includes database calls in external class (SQL_queries), which are not
     * part of unit-testing that's why LoginServlet is mocked
     */
    @Test
    void verifyDoPostMethod() throws IOException {
        // Initialization of necessary mock objects for mocking instance methods
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Initialization of necessary mock objects for mocking static methods
        MockedStatic<SQLQueries> sqlMock = Mockito.mockStatic(SQLQueries.class);

        // Initialization of necessary instances
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        String salt = "SH5E9Z7P5J6Z5G2BV0";
        String accessToken = "abc123";
        LoginServlet loginServlet = new LoginServlet();

        // Define what happens when mocked method is called
        sqlMock.when(() -> SQLQueries.getSalt(TestEnum.TESTEMAIL.toString())).thenReturn(salt);
        sqlMock.when(() -> SQLQueries.userLogin(TestEnum.TESTEMAIL.toString(), salt, TestEnum.TESTPASSWORT.toString()))
                .thenReturn(new String[] { "1", TestEnum.TESTMATRIKELNUMMER.toString(), TestEnum.TESTMATRIKELNUMMER.toString(),
                        TestEnum.TESTROLLEINT.toString(), accessToken });

        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("pw")).thenReturn(TestEnum.TESTPASSWORT.toString());

        // run test
        loginServlet.doPost(request, response);

        // get the value of stringWriter
        String result = stringWriter.toString();
        assertEquals(result,
                "{\"resultCode\":\"1\",\"studiengang\":\"" + TestEnum.TESTMATRIKELNUMMER + "\",\"matrikelnummer\":\""
                        + TestEnum.TESTMATRIKELNUMMER + "\",\"rolle\":\"" + TestEnum.TESTROLLEINT
                        + "\"}");

        // Close mock objects for mocking static methods
        sqlMock.close();
    }

}

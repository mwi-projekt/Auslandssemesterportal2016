package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import dhbw.mwi.Auslandsemesterportal2016.rest.LoginServlet;
import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.enums.TestEnum;

public class LoginServletTest {

    /*
     * method verifies that doPost() is called
     * 
     * method includes database calls in external class (SQL_queries), which are not
     * part of unit-testing that's why LoginServlet is mocked
     */
    @Test
    public void verifyDoPostMethod() throws IOException {
        // prepare mocks
        MockedStatic<SQL_queries> sqlMock = Mockito.mockStatic(SQL_queries.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // prepare test
        String salt = "SH5E9Z7P5J6Z5G2BV0";
        String accessToken = "abc123";

        LoginServlet loginServlet = new LoginServlet();
        when(request.getParameter("email")).thenReturn(TestEnum.TESTEMAIL.toString());
        when(request.getParameter("pw")).thenReturn(TestEnum.TESTPW.toString());
        sqlMock.when(() -> SQL_queries.getSalt(TestEnum.TESTEMAIL.toString())).thenReturn(salt);
        sqlMock.when(() -> SQL_queries.userLogin(TestEnum.TESTEMAIL.toString(), salt, TestEnum.TESTPW.toString()))
                .thenReturn(new String[] { "1", TestEnum.TESTMATRNR.toString(), TestEnum.TESTMATRNR.toString(),
                        TestEnum.TESTROLLEINT.toString(), accessToken });

        // run test
        loginServlet.doPost(request, response);
        String result = stringWriter.toString();
        assertEquals(result,
                "{\"resultCode\":\"1\",\"studiengang\":\"" + TestEnum.TESTMATRNR.toString() + "\",\"matrikelnummer\":\""
                        + TestEnum.TESTMATRNR.toString() + "\",\"rolle\":\"" + TestEnum.TESTROLLEINT.toString()
                        + "\"}");
        sqlMock.close();
    }

}

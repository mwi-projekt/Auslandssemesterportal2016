package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.servlets.LoginServlet;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SQL_queries.class)
public class LoginServletTest extends LoginServlet
{
	/*HttpServletRequest req;
	HttpServletResponse res;
	String correctEmail, wrongEmail, salt, correctPassword, wrongPassword;
	LoginServlet servlet;
	
	@Before
	public void setup()
	{
		req = mock(HttpServletRequest.class);
		res = mock(HttpServletResponse.class);
		correctEmail = "testusermwi@student.dhbw-karlsruhe.de";
		wrongEmail = "externer@web.de";
		salt = "MWISalt";
		correctPassword = "Passwort1";
		wrongPassword = "12345678";
		servlet = new LoginServlet();
		PowerMockito.mockStatic(SQL_queries.class);
		

		
	}
	
	@Test
	public void emailCorrectPwCorrect()
	{
		when(SQL_queries.executeStatement(query, params, types)).thenReturn(new ResultSet());
		when(ergebnis.next()).thenReturn(true);
		when(ergebnis.getString("studiengang")).thenReturn("Wirtschaftsinformatik");
		when(ergebnis.getString("matrikelnummer")).thenReturn("12345678");
		when(ergebnis.getString("rolle")).thenReturn("2");
		when(ergebnis.getString("verifiziert")).thenReturn("1");
		
		String[] result = SQL_queries.userLogin(correctEmail, salt, correctEmail);
		assertEquals(result[0], "1");
	}
	
	@Test
	public void emailCorrectPwWrong() throws IOException
	{
		


	}
	
	@Test
	public void emailWrongPwCorrect()
	{
		
	}
	
	@Test
	public void emailWrongPwWrong()
	{
		
	}
	
	@Test
	public void emailNotFound()
	{
		
	}
	
	@After
	public void cleanup()
	{
		req = null;
		res = null;
	}*/
}

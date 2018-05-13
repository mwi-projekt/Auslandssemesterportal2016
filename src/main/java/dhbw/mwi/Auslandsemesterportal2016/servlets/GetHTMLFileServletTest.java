package dhbw.mwi.Auslandsemesterportal2016.servlets;

import static org.junit.Assert.*;

import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;


public class GetHTMLFileServletTest  extends Mockito
{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private GetHtmlFileServlet servlet;
	
	@Test
	public void requestForNonExistingPageShouldFail()  throws Exception
	{
		String requestedPage = "xyz";
		when(request.getParameter("page")).thenReturn(requestedPage);
		when(request.getParameter("accessLevel")).thenReturn("0");
		when(userAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(1);
		
		servlet.doGet(request, response);
		assertEquals(response.toString(), 403, response.getStatus());
	}

	@Test
	public void requestForTestPageShouldSucceed() throws Exception
	{
		when(request.getParameter("page")).thenReturn("student");
		when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("mail","a.a@a.com"), new Cookie("sessionID","XXX")});
		servlet.doGet(request, response);
		assertEquals(response.toString(), 200, response.getStatus());
	}
	
	@Before
	public void setup()
	{
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		servlet = new GetHtmlFileServlet();
	}

	@After
	public void cleanup()
	{
		request = null;
		response = null;
		servlet = null;
	}
}

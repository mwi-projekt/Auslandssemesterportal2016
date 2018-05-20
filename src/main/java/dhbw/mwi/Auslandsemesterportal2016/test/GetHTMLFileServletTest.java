package dhbw.mwi.Auslandsemesterportal2016.test;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;
import dhbw.mwi.Auslandsemesterportal2016.servlets.GetHtmlFileServlet;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserAuthentification.class})
public class GetHTMLFileServletTest  extends Mockito
{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private GetHtmlFileServlet servlet;
	private userAuthentification uA;
	
	@Before
	public void setup()
	{
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		servlet = new GetHtmlFileServlet();
		uA = mock(userAuthentification.class);
		
		
	}
	
	@Test
	public void requestForNonExistingPageShouldFail()  throws Exception
	{
		//Mock Ups
//		when(request.getParameter("page")).thenReturn("xyz");
//		when(request.getParameter("accessLevel")).thenReturn("0");
//		when(UserAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(1);
		doReturn(1).when(uA).isUserAuthentifiedByCookie(request);
		
		
		//Do Something
		servlet.doGet(request, response);
		
		//Check Success
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
	}

	
	@Test
	public void requestForTestPageShouldSucceed() throws Exception
	{
		//Mock Ups
		when(request.getParameter("page")).thenReturn("student");
//		when(userAuthentification.isUserAuthentifiedByCookie(request)).thenReturn(1);
		
		//Do Something
		servlet.doGet(request, response);
		
		//Check Success
		assertEquals(HttpServletResponse.SC_ACCEPTED, response.getStatus());
	}
	


	@After
	public void cleanup()
	{
		request = null;
		response = null;
		servlet = null;
		uA = null;
	}
}

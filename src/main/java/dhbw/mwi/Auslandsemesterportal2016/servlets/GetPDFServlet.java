package dhbw.mwi.Auslandsemesterportal2016.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetHtmlFileServlet", urlPatterns = 
		{"/WebContent/Anmeldeformular",
		"/WebContent/DAAD-Sprachzeugnis"})


public class GetPDFServlet extends HttpServlet
{
	private static final String PDF_FOLDER = "/WebContent/docs";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		
		String requestedPage = request.getServletPath();
		requestedPage = requestedPage.substring(11, requestedPage.length());
		
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		RequestDispatcher view = request.getServletContext().getRequestDispatcher(PDF_FOLDER+requestedPage+".pdf");
		view.include(request, response);
		return;
	}
}

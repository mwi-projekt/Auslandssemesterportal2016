package dhbw.mwi.Auslandsemesterportal2016.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetHtmlFileServlet", urlPatterns = {"/WebContent/getHtml"})

public class GetHtmlFileServlet extends HttpServlet 
{
	private static final String PUBLIC_HTML_FOLDER = "/HTML/public/";
	private static final String STUDENT_HTML_FOLDER = "/HTML/student/";
	private static final String EMPLOYEE_HTML_FOLDER = "/HTML/employee/";
	private static final String ADMIN_HTML_FOLDER = "/HTML/admin/";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String requestedPage = request.getContextPath();
		int userAccessLevel = userAuthentification.isUserAuthentifiedByCookie(request);
		switch(userAccessLevel)
		{
			case 3:
			{
				RequestDispatcher view = request.getRequestDispatcher(ADMIN_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			case 2:
			{
				RequestDispatcher view = request.getRequestDispatcher(EMPLOYEE_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			case 1:
			{
				RequestDispatcher view = request.getRequestDispatcher(STUDENT_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			default:
				RequestDispatcher view = request.getRequestDispatcher(PUBLIC_HTML_FOLDER+requestedPage+".html");
				view.forward(request, response);
		}
	}
}

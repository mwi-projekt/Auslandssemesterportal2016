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
	private static final String PUBLIC_HTML_FOLDER = "/WebContent/HTML/public/";
	private static final String STUDENT_HTML_FOLDER = "/WebContent/HTML/student/";
	private static final String EMPLOYEE_HTML_FOLDER = "/WebContent/HTML/employee/";
	private static final String ADMIN_HTML_FOLDER = "/WebContent/HTML/admin/";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String requestedPage = request.getParameter("page");
		int userAccessLevel = userAuthentification.isUserAuthentifiedByCookie(request);
		//Prüfe das Level des Nutzers, falls kein Dokument für das Level vorhanden, prüfe das nächstniedriger Level
		//gibt das höchste verfügbare, erlaubte Dokument zurück
		switch(userAccessLevel)
		{
			//Falls Dokument für Admin Ansicht vorhanden, zurückgeben
			case 3:
			{
				RequestDispatcher view = request.getRequestDispatcher(ADMIN_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			//Falls Dokument für Mitarbeiter Ansicht vorhanden, zurückgeben
			case 2:
			{
				RequestDispatcher view = request.getRequestDispatcher(EMPLOYEE_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			//Falls Dokument für Studenten Ansicht vorhanden, zurückgeben
			case 1:
			{
				RequestDispatcher view = request.getRequestDispatcher(STUDENT_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			//Falls Dokument für Öffentliche Ansicht vorhanden, zurückgeben
			case 0:
			{
				RequestDispatcher view = request.getRequestDispatcher(PUBLIC_HTML_FOLDER+requestedPage+".html");
				if(view!=null)
				{
					view.forward(request, response);
					break;
				}
			}
			//Falls kein öffentliches Dokument vorhanden ist, HTTP 401 nicht autorisiert
			default:
			{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
	                     "Sie haben keine Berechtigung auf diese Seite zuzugreifen!");
				return;
			}
		}
	}
}

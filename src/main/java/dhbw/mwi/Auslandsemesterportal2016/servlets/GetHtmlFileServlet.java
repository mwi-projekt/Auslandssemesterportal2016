package dhbw.mwi.Auslandsemesterportal2016.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetHtmlFileServlet", urlPatterns = {"/WebContent/getHtml",
		/*"/Web"
		 * */
		})
		 

public class GetHtmlFileServlet extends HttpServlet 
{
	private static final String[] HTML_FOLDER = {"/WebContent/HTML/public/",
												"/WebContent/HTML/student/",
												"/WebContent/HTML/employee/",
												"/WebContent/HTML/admin/"};
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String requestedPage = request.getParameter("page");
		int userAccessLevel = userAuthentification.isUserAuthentifiedByCookie(request);
		
		/*//TODO JUST FOR TESTING PURPOSE, REMOVE ON PRODUCTIVE USE
		if(request.getParameter("accessLevel")!=null)
		{
			userAccessLevel = Integer.parseInt(request.getParameter("accessLevel"));
		}
		*/
		//Prüfe das Level des Nutzers, falls kein Dokument für das Level vorhanden, prüfe das nächstniedriger Level
		//gibt das höchste verfügbare, erlaubte Dokument zurück
		while(userAccessLevel >=0)
		{
			try
			{
				RequestDispatcher view = request.getServletContext().getRequestDispatcher(HTML_FOLDER[userAccessLevel]+requestedPage+".html");
				if(view != null)
				{
					response.setStatus(HttpServletResponse.SC_ACCEPTED);
					view.include(request, response);
					return;
				}
			}
			catch(Exception e)
			{
				userAccessLevel--;
			}
		}
		//Falls kein berechtigtes Dokument vorliegt
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.sendError(HttpServletResponse.SC_NOT_FOUND,
                "Sie haben keine Berechtigung auf diese Seite zuzugreifen!");
		return;
		
		/*switch(userAccessLevel)
		{
			//Falls Dokument für Admin Ansicht vorhanden, zurückgeben
			case 3:
			{
				try{
					RequestDispatcher view = request.getServletContext().getRequestDispatcher(ADMIN_HTML_FOLDER+requestedPage+".html");
					if(view!=null)
					{
						view.forward(request, response);
						break;
					}
				}
				catch(Exception e)
				{
					//do nothing, just drop down
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
		}*/
	}
}

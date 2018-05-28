package dhbw.mwi.Auslandsemesterportal2016.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.userAuthentification;

@WebServlet(name = "GetHtmlFileServlet", urlPatterns = {//"/WebContent/getHtml",
		"/WebContent/faq",
		//"/WebContent/index",
		"/WebContent/changePw",
		"/WebContent/admin-process-modeler",
		"/WebContent/admin-process-overview",
		"/WebContent/admin-process",
		"/WebContent/choose-diagram",
		"/WebContent/index",
		"/WebContent/task_detail",
		"/WebContent/test",
		"/WebContent/accordion",
		"/WebContent/bewerbungsportal",
		"/WebContent/erfahrungsbericht1",
		"/WebContent/erfahrungsbericht2",
		"/WebContent/erfahrungsbericht3",
		"/WebContent/erfahrungsbericht4",
		"/WebContent/forgot_pw",
		"/WebContent/impressum",
		"/WebContent/bewerben",
		"/WebContent/bewerbungsformular",
		"/WebContent/dokumente",
		"/WebContent/einschreibungsprozess",
		"/WebContent/einscheibungsprozessSchottland",
		"/WebContent/fertig",
		"/WebContent/start"
		})
		 
//TODO richtige Fehlermeldung ausgeben. Aktuell immer 400 - BAD REQUEST

public class GetHtmlFileServlet extends HttpServlet 
{
	
	//Level: keins (0), Admin (1), Mitarbeiter (2), Student (3)
	private static final String[] HTML_FOLDER = {"/WebContent/HTML/public",
												"/WebContent/HTML/admin",
												"/WebContent/HTML/employee",
												"/WebContent/HTML/student"};
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		
		String requestedPage = request.getServletPath();
		int userAccessLevel;
		if(requestedPage  == "/WebContent/faq" || requestedPage == "/WebContent/impressum")
			userAccessLevel = 0;
		else
			userAccessLevel = userAuthentification.isUserAuthentifiedByCookie(request);
		
		requestedPage = requestedPage.substring(11, requestedPage.length());
		
		
		/*if(requestedPage == "/faq")
		{
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			RequestDispatcher view = request.getServletContext().getRequestDispatcher(HTML_FOLDER[0]+"/faq.html");
			view.include(request, response);
			return;
		}
		else if(requestedPage == "/impressum")
		{
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			RequestDispatcher view = request.getServletContext().getRequestDispatcher(HTML_FOLDER[0]+"/impressum.html");
			view.include(request, response);
			return;
		}*/
		
		
		
		//response.sendError(HttpServletResponse.SC_BAD_REQUEST, "." +requestedPage+". userLevel:" + userAccessLevel);
		
		
		
		//Prüfe das Level des Nutzers, falls kein Dokument für das Level vorhanden, prüfe das nächstniedriger Level
		//gibt das höchste verfügbare, erlaubte Dokument zurück
		
		int highestDocumentLevelFound = -1;
		boolean forbiddenDocumentFound = false;
		boolean publicDocumentFound = false;
		boolean rightDocumentFound = false;
		
		//Durchsuche alle Ordner
		for(int i = 0; i < HTML_FOLDER.length; i++)
		{
			RequestDispatcher tempView = null;
			try
			{
				tempView = request.getServletContext().getRequestDispatcher(HTML_FOLDER[i]+requestedPage+".html");
			}
			catch(Exception e)
			{
				continue;
			}	
			//falls Dokument in Ordner
			if(tempView != null)
			{
				if(i == 0)
				{
					publicDocumentFound = true;
				}
				//Zugriff nicht erlaubt
				else if(i == userAccessLevel)
				{
					rightDocumentFound = true;
				}
				else
				{
					forbiddenDocumentFound = true;
				}
			}
		}
		//wurde ein erlaubtes Dokument gefunden?
		try
		{
			
			if(rightDocumentFound)
			{
				
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				RequestDispatcher view = request.getServletContext().getRequestDispatcher(HTML_FOLDER[userAccessLevel]+requestedPage+".html");
				view.include(request, response);
				return;
			}
			else if(publicDocumentFound)
			{
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				RequestDispatcher view = request.getServletContext().getRequestDispatcher(HTML_FOLDER[0]+requestedPage+".html");
				view.include(request, response);
				return;
			}
			//es wurde kein erlaubtes Dokument gefunden
			else
			{
				//es wurde ein verbotenes Dokument gefunden
				if(forbiddenDocumentFound)
				{
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
			                "Sie haben keine Berechtigung auf diese Seite zuzugreifen!");
					return;
				}
				//es wurde nichts gefunden
				else
				{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
			                "Seite wurde nicht gefunden!");
					return;
				}
			}
		}
		catch(Exception e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Es gab einen Fehler in der Verarbeitung des Requests."
					+ "UserAccessLevel: " + userAccessLevel
					+ ", Requested Path: " + requestedPage
					+ ", ForbiddenDocumentFound: " + forbiddenDocumentFound
					+ ", PublicDocumentFound: " + publicDocumentFound
					+ ", RightDocuementFound: " + rightDocumentFound
					+ e.getMessage());
		}
		
		
	}
}

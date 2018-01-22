package dhbw.mwi.Auslandsemesterportal2016.db;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class userAuthentification 
{
	//Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Fehler
	static int isUserAuthentifiedByCookie(HttpServletRequest request)
	{
		String sessionId = null, mail = null;
		Cookie[] cookies = request.getCookies();
		int role = 0;
		//check if there are any cookies at all and then if session id has been set, meaning user is logged in
		if(cookies != null) 
		{
			for (int i = 0; i < cookies.length; i++)
			{
				//TODO define the name of the cookie value for the session id
				if(cookies[i].getName()=="sessionID")
					sessionId = cookies[i].getValue();
				if(cookies[i].getName() =="email")
					mail = cookies[i].getValue();
			}
		
		
		//check if session-id was found in cookies
			if(sessionId != null && mail != null)
			{
				//check if a sessionId matching to the mail was found in DB
				if(SQL_queries.checkUserSession(sessionId, mail))
				{
					role = SQL_queries.getRoleForUser(mail);
				}
			}
		}
		//Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Fehler
		return role;
	}
}

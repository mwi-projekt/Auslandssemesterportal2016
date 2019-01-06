package dhbw.mwi.Auslandsemesterportal2016.db;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class userAuthentification {
	// Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Invalid
	// Session; -1 = No Session at all
	public static int isUserAuthentifiedByCookie(HttpServletRequest request) {
		int rolle = 0;
		Cookie[] cookies = request.getCookies();
		String sessionId = null, mail = null;
		if (cookies != null) {

			for (Cookie c : cookies) {
				if (c.getName().equals("email")) {
					mail = c.getValue();
				} else if (c.getName().equals("sessionID")) {
					sessionId = c.getValue();
				}
			}
			// check if session-id was found in cookies
			if (sessionId != null && mail != null) {
				// check if a sessionId matching to the mail was found in DB
				if (SQL_queries.checkUserSession(sessionId, mail)) {
					rolle = SQL_queries.getRoleForUser(mail);
				}
			}
			return rolle;

		}
		// Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Fehler
		return -1;
	}
}

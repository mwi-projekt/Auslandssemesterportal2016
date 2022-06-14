package dhbw.mwi.Auslandsemesterportal2016.db;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserAuthentification {

	private UserAuthentification() {
	}

	// Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Invalid
	// Session; -1 = No Session at all
	public static int isUserAuthentifiedByCookie(HttpServletRequest request) {
		int rolle = 0;
		Cookie[] cookies = request.getCookies();
		String sessionId = null;
		String mail = null;
		if (cookies != null) {

			for (Cookie c : cookies) {
				if (c.getName().equals("email")) {
					mail = c.getValue();
				} else if (c.getName().equals("sessionID")) {
					sessionId = c.getValue();
				}
			}
			// check if session-id was found in cookies
			// check if a sessionId matching to the mail was found in DB
			if (sessionId != null && mail != null && SQLQueries.checkUserSession(sessionId, mail)) {
				rolle = SQLQueries.getRoleForUser(mail);
			}
			return rolle;

		}
		// Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Fehler
		return -1;
	}

	public static User getUserInfo(HttpServletRequest request) {
		User user;
		Cookie[] cookies = request.getCookies();
		String sessionId = null;
		String mail = null;
		if (cookies != null) {

			for (Cookie c : cookies) {
				if (c.getName().equals("email")) {
					mail = c.getValue();
				} else if (c.getName().equals("sessionID")) {
					sessionId = c.getValue();
				}
			}
			// check if session-id was found in cookies
			// check if a sessionId matching to the mail was found in DB
			if (sessionId != null && mail != null && SQLQueries.checkUserSession(sessionId, mail)) {
				user = SQLQueries.getUserInfo(mail);
				return user;
			}

		}
		return null;
	}

	public static boolean isTestUser(Cookie[] cookies) {
		List<Cookie> email = Arrays.stream(cookies)
				.filter(c -> c.getName().equals("email"))
				.collect(Collectors.toList());

		String emailOfUser = null;
		if (!email.isEmpty()) {
			emailOfUser = email.get(0).getValue();
		}

		return emailOfUser != null && isTestEmail(emailOfUser);
	}

	private static boolean isTestEmail(String emailOfUser) {
		return (emailOfUser.equals("test@student.dhbw-karlsruhe.de")
				|| emailOfUser.equals("test@sgl.dhbw-karlsruhe.de")
				|| emailOfUser.equals("auslandsamt@dh-karlsruhe.de")
				|| emailOfUser.equals("testadminmwi@dhbw.de"));
	}
}

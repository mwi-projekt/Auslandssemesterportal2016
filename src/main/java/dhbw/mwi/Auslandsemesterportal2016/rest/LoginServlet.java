package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String salt = "";
		String mail = "";

		mail = request.getParameter("email");
		salt = SQL_queries.getSalt(mail);
		String[] result = SQL_queries.userLogin(mail, salt, request.getParameter("pw"));
		Cookie cookie = new Cookie("sessionID", result[4]);
		Cookie mailcookie = new Cookie("email", mail);
		response.addCookie(cookie);
		response.addCookie(mailcookie);
		JsonObject json = new JsonObject();
		json.addProperty("resultCode", result[0]);
		json.addProperty("studiengang", result[1]);
		json.addProperty("matrikelnummer", result[2]);
		json.addProperty("rolle", result[3]);
		Util.setResponseHeaders(request,response);
		Util.writeJson(response, json);
	}
}

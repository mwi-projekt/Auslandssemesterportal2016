package dhbw.mwi.Auslandsemesterportal2016.servlets;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dhbw.mwi.Auslandsemesterportal2016.db.SQL_queries;

//TODO remove just for testing purpose
@WebServlet(name = "LoginServlet", urlPatterns = {"/WebContent/GenericServlet"})

public class HTMLDeliveryServlet extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String sessionId = null;
		Cookie[] cookies = request.getCookies();
		//check if there are any cookies at all and then if session id has been set, meaning user is logged in
		if(cookies != null) 
		{
			for (int i = 0; i < cookies.length; i++)
			{
				//TODO define the name of the cookie value for the session id
				if(cookies[i].getName()=="session_id")
				{
					sessionId = cookies[i].getValue();
					break;
				}
			}
		
		
		//check if session-id was found in cookies
			if(sessionId != null)
			{
				//TODO Target query: (Tables as of now may/ do not exist)
				/*ResultSet user = SQL_queries.executeStatement(
						"SELECT user.user_id, user.name "
						+ "FROM user"
						+ "INNER JOIN sessionid"
						+ "ON user.userid = sessionid.userid"
						+ "AND sessionid.sessionid = ?"
						, new String[]{sessionId}
						, new String[]{"String"});*/
				
				//In the Meanwhile working SQL:
				String userId = null, int role = 0;
				ResultSet rs = SQL_queries.executeStatement(
						"SELECT user_id"
						+ "FROM sessionid"
						+ "WHERE session_id = ?"
						, new String[]{sessionId}
						, new String[]{"String"});
				try {
					if(rs.next())
					{
						userId = rs.getString("user_id");
						role = rs.getInt("role");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//check if a userId matching to the sessionId was found in DB
				if(userId != null)
				{
					//return HTML page for retrieved role
					RequestDispatcher view = request.getRequestDispatcher("/WebContent/HTML/" + request.getParameter("page") + role +".html");
					view.forward(request, response);
				}
			}
		}
		//missing session-id concludes user is not logged in, therefore directed to login
		//default in case of error is also redirecting to login
		RequestDispatcher view = request.getRequestDispatcher("/WebContent/lgin/");
		view.forward(request, response);	
	}
}

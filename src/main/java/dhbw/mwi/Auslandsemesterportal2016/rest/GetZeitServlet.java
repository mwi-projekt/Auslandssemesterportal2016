package dhbw.mwi.Auslandsemesterportal2016.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Calendar;

import dhbw.mwi.Auslandsemesterportal2016.db.Util;

@WebServlet(urlPatterns = { "/zeit" })
public class GetZeitServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Util.addResponseHeaders(request,response);

		PrintWriter toClient = response.getWriter();
		
		//Aktuelles Jahr abfragen
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		//Liste enthält WS und SS für das aktuelle und die zwei darauffolgenden Jahre
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		for (int i=0; i < 3; i++) {
			int yearCur = year + i;
			stringBuilder.append("{\"zeit\":\"Sommersemester ")
					.append(yearCur)
					.append("\"},{\"zeit\":\"Wintersemester ")
					.append(yearCur)
					.append("\"}");
			if (i != 2)
				stringBuilder.append(",");
		}
		stringBuilder.append("]");
		toClient.print(stringBuilder);
	}

}

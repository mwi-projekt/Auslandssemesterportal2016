package dhbw.mwi.Auslandsemesterportal2016.db;

import dhbw.mwi.Auslandsemesterportal2016.db.DB;
import dhbw.mwi.Auslandsemesterportal2016.db.Util;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class SQL_queries {
	
private static ResultSet executeQuery(String query){ //Führt Query auf Datenbankanbindung aus DB.java aus
Connection connection = DB.getInstance();
ResultSet rs = null;
try{
	Statement statement = connection.createStatement();
	rs = statement.executeQuery(query);
	}
catch (Exception e){
	e.printStackTrace();
}
return rs;
}

public static boolean isMatnrUsed(int matNr){ //Prüft ob Matrikelnummer bereits verwendet wird
	String queryString = "SELECT 1 FROM user WHERE matrikelnummer = " + matNr + ";";
	boolean resultExists = true;
	ResultSet ergebnis = executeQuery(queryString);
	try{
		resultExists = ergebnis.next();
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return resultExists;
}

public static boolean isEmailUsed(String mail){ //Prüft ob Mailadresse bereits verwendet wird
	String queryString = "SELECT 1 FROM user WHERE email = '" + mail + "';";
	boolean resultExists = true;
	ResultSet ergebnis = executeQuery(queryString);
	try{
		resultExists = ergebnis.next();
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return resultExists;
}


public static String getSalt(String mail){//Ermittelt das zur Mailadresse hinterlegte Salt
	String queryString = "SELECT salt FROM user WHERE email = '" + mail + "';";
	String salt = "";
	ResultSet ergebnis = executeQuery(queryString);
	try{
		if (ergebnis.next()){
			salt = ergebnis.getString(1);
		}
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return salt;
}

public static ResultSet userLogin(String mail, String salt, String pw){//Prüft Logindaten. Gibt ResultSet zurück
	String hashedPw = Util.HashSha256(Util.HashSha256(pw) + salt);
	String query = "SELECT rolle, matrikelnummer, studiengang FROM user WHERE email = '" + mail + 
	"' AND passwort = '" + hashedPw + "' AND verifiziert = '1';";
	ResultSet ergebnis = executeQuery(query);
	return ergebnis;
}

}

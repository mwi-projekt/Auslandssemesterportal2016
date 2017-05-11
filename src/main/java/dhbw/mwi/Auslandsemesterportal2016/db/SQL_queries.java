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
	
private ResultSet executeQuery(String query){ //Führt Query auf Datenbankanbindung aus DB.java aus
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

public boolean isMatnrUsed(int matNr){ //Prüft ob Matrikelnummer bereits verwendet wird
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

public boolean isEmailUsed(String mail){ //Prüft ob Mailadresse bereits verwendet wird
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

public boolean isUserValidated(String mail){ //Prüft ob Nutzer die Mailadresse bestätigt hat
	String queryString = "SELECT 1 FROM user WHERE email = '" + mail + "' AND verifiziert = '1';";
	boolean resultExists = false;
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

public String getSalt(String mail){//Ermittelt das zur Mailadresse hinterlegte Salt
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

public int userLogin(String mail, String salt, String pw){//Prüft Login. Gibt Rolle des Users zurück, 0 bei Fehler
	String hashedPw = Util.HashSha256(Util.HashSha256(pw) + salt);
	String query = "SELECT rolle FROM user WHERE email = '" + mail + "' AND passwort = '" + hashedPw + "';";
	int loginCode = 0;
	ResultSet ergebnis = executeQuery(query);
	try{
		if (ergebnis.next()){
			loginCode = ergebnis.getInt(1);
		}
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return loginCode;
}

}

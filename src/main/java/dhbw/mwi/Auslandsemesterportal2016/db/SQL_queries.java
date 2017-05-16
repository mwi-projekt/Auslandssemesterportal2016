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

private static ResultSet executeStatement (String query, String[] data, String[] types){
	Connection connection = DB.getInstance();
	ResultSet rs = null;
	int parCount = data.length;
	
	try{
		PreparedStatement statement = connection.prepareStatement(query);
		for (int i = 0; i < parCount; i++){
			if (types[i] == "String"){
				statement.setString(i+1, data[i]);
			} else if (types[i] == "int"){
				statement.setInt(i+1, Integer.parseInt(data[i]));
			}
			}
		rs = statement.executeQuery();
		} 
	    catch (Exception e){
		e.printStackTrace();
	    }
	return rs;
}

public static boolean isMatnrUsed(int matNr){ //Prüft ob Matrikelnummer bereits verwendet wird
	String queryString = "SELECT 1 FROM user WHERE matrikelnummer = ?;";
	String[] params = new String[]{""+matNr};
	String[] types = new String[]{"int"};
	boolean resultExists = true;
	ResultSet ergebnis = executeStatement(queryString,params,types);
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

public static String userLogin(String mail, String salt, String pw){//Prüft Logindaten. Gibt Code zurück: 1 = Erfolgreich, 2 = Falsche Daten, 3 = nicht aktiviert, 4 = Datenbankfehler
	String hashedPw = Util.HashSha256(Util.HashSha256(pw) + salt);
	int resultCode = 4;
	String studiengang = "";
	String matrikelnummer = "";
	String rolle = "";
	String query = "SELECT verifiziert, matrikelnummer, studiengang, rolle FROM user WHERE email = '" + mail + 
	"' AND passwort = '" + hashedPw + "';";
	ResultSet ergebnis = executeQuery(query);
	try{
		if (ergebnis.next()){
			studiengang = ergebnis.getString("studiengang");
			matrikelnummer = ergebnis.getString("matrikelnummer");
			rolle = ergebnis.getString("rolle");
			if (ergebnis.getString("verifiziert").equals("1")){
				resultCode = 1;
			} else {
				resultCode = 3;
			}
		} else {
			resultCode = 2;
		}
	} catch (Exception e){
	 e.printStackTrace();
	}
	return "" + resultCode + ";" + studiengang + ";" + matrikelnummer + ";" + rolle;
	
}

}

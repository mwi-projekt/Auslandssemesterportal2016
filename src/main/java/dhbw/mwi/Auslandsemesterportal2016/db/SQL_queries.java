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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SQL_queries {

//Ersetzt durch executeStatement
/*
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
*/

public static ResultSet executeStatement (String query, String[] data, String[] types){//Führt Query mit Hilfe von PreparedStatements aus
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

public static int executeUpdate (String query, String[] data, String[] types){//Führt UPDATE mit Hilfe von PreparedStatements aus
	Connection connection = DB.getInstance();
	int parCount = data.length;
	int result = 0;
	try{
		PreparedStatement statement = connection.prepareStatement(query);
		for (int i = 0; i < parCount; i++){
			if (types[i] == "String"){
				statement.setString(i+1, data[i]);
			} else if (types[i] == "int"){
				statement.setInt(i+1, Integer.parseInt(data[i]));
			} else if (types[i] == "date"){
				java.sql.Date date = java.sql.Date.valueOf(data[i]);
				statement.setDate(i+1,  date);
			}
			}
		result = statement.executeUpdate();
		}
	    catch (Exception e){
		e.printStackTrace();
	    }
	return result;
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
	String queryString = "SELECT 1 FROM user WHERE email = ?;";
	boolean resultExists = true;
	String[] args = new String[]{mail};
	String[] types = new String[]{"String"};
	ResultSet ergebnis = executeStatement(queryString,args,types);
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
	String queryString = "SELECT salt FROM user WHERE email = ?;";
	String salt = "";
	String[] args = new String[]{mail};
	String[] types = new String[]{"String"};
	ResultSet ergebnis = executeStatement(queryString,args,types);
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
public static String createUserSession(int userID){
    String accessToken = Util.generateSalt();
    
    if(userSessionExists(userID)){
            String query_ = "UPDATE loginSessions SET sessionID = ? WHERE userID = ?";
            String[] params_ = new String[]{accessToken,""+userID};
            String[] types_ = new String[]{"String","int"};
            executeUpdate(query_,params_,types_);
    }
    else{
            String query_ = "INSERT INTO loginSessions (sessionID, userID) VALUES " +
                            "(?,?)";
            String[] params_ = new String[]{accessToken,""+userID};
            String[] types_ = new String[]{"String","int"};
            executeUpdate(query_,params_,types_);
    }
    return accessToken;
                                
    
}
public static String[] userLogin(String mail, String salt, String pw){
	//Prüft Logindaten. ResultCodes: 1 = Erfolgreich, 2 = Falsche Daten, 3 = nicht aktiviert, 4 = Datenbankfehler
	//Stringkette, die zurückgegeben wird: resultCode;Bezeichnung Studiengang;Matrikelnummer;Rolle (Nummer die in der DB steht)
	String hashedPw = Util.HashSha256(Util.HashSha256(pw) + salt);
	int resultCode = 4;
	String studiengang = "";
	String matrikelnummer = "";
	String rolle = "";
	String accessToken = "";
	int userID;
	String query = "SELECT verifiziert, matrikelnummer, studiengang, rolle, userID FROM user WHERE email = ? AND passwort = ?;";
	String[] params = new String[]{mail,hashedPw};
	String[] types = new String[]{"String","String"};
	ResultSet ergebnis = executeStatement(query,params,types);
	try{
		if (ergebnis.next()){
			studiengang = ergebnis.getString("studiengang");
			matrikelnummer = ergebnis.getString("matrikelnummer");
			rolle = ergebnis.getString("rolle");
			if (ergebnis.getString("verifiziert").equals("1")){
				accessToken = createUserSession(ergebnis.getInt("userID"));
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

	return new String[] {(""+resultCode), studiengang, matrikelnummer, rolle, accessToken};
}

public static boolean userSessionExists(int userID){
	String queryString = "SELECT 1 FROM loginSessions WHERE userID = ?;";
	boolean resultExists = true;
	String[] args = new String[]{""+userID};
	String[] types = new String[]{"int"};
	ResultSet ergebnis = executeStatement(queryString,args,types);
	try{
		resultExists = ergebnis.next();
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return resultExists;
}

public static boolean checkUserSession(String sessionID, String mail){
	String queryString = "SELECT loginSessions.sessionID, user.email FROM loginSessions,user WHERE user.userID = loginSessions.userID and user.email = ? and loginSessions.sessionID = ?;";
	String[] args = new String[]{mail,sessionID};
	String[] types = new String[]{"String","String"};
	boolean isCorrect = false;
	ResultSet ergebnis = executeStatement(queryString,args,types);
	try{
		isCorrect = ergebnis.next();
		ergebnis.close();
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return isCorrect;
}

//Rolle für User: 1 = Admin ; 2 = Mitarbeiter ; 3 = Student ; 0 = Fehler
public static int getRoleForUser(String mail){
		String queryString = "SELECT rolle FROM user WHERE email = ?;";
		String[] args = new String[]{mail};
		String[] types = new String[]{"String"};
		ResultSet ergebnis = executeStatement(queryString,args,types);

		try{
			if (ergebnis.next()){
				return ergebnis.getInt(1);
			} else {
				return 0;
			}
		} catch (Exception e){
			e.printStackTrace();
			return 0;
		}

}

//DO NOT USE IN ANY SERVLET. DO NOT GIVE USERIDs TO PUBLIC
public static int getUserID(String mail){
		String queryString = "SELECT userID FROM user WHERE email = ?;";
		String[] args = new String[]{mail};
		String[] types = new String[]{"String"};
		ResultSet ergebnis = executeStatement(queryString,args,types);

		try{
			if (ergebnis.next()){
				return ergebnis.getInt(1);
			} else {
				return -1;
			}
		} catch (Exception e){
			e.printStackTrace();
			return -1;
		}

}

public static int userLogout(String sessionID){
		String query_ = "DELETE FROM loginSessions WHERE sessionID = ?";
		String[] params_ = new String[]{sessionID};
		String[] types_ = new String[]{"String"};
		return executeUpdate(query_,params_,types_);
}

public static int userRegister(String vorname, String nachname, String passwort, String salt, int rolle, String email, String studiengang,
		String kurs, int matrikelnummer, String tel, String mobil, String standort, String verifiziert){
	String query = "INSERT INTO user (vorname, nachname, passwort, salt, rolle, email, studiengang, kurs, matrikelnummer, tel, mobil, standort, verifiziert) VALUES " +
			"(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	String[] args = new String[]{vorname,nachname,passwort,salt,""+rolle,email,studiengang,kurs,""+matrikelnummer,tel,mobil,standort,verifiziert};
	String[] types = new String[]{"String","String","String","String","int","String","String","String","int","String","String","String","String"};
	return executeUpdate(query,args,types);
}

public static ResultSet getJson(String step, String model){
	//Gibt JSON-Dokument für die Eingabemaske zurück
	String query = "SELECT json FROM processModel WHERE step = ? AND model = ?;";
	String[] params = new String[]{step,model};
	String[] types = new String[]{"String","String"};
	ResultSet ergebnis = executeStatement(query,params,types);
	try{
	 return ergebnis;
	} catch (Exception e){
	 e.printStackTrace();
	 return null;
	}
}

public static String getInstanceId(int matNr, String uni){
	String query = "SELECT processInstance FROM MapUserInstanz WHERE matrikelnummer = ? AND uni = ?";
	String[] params = new String[]{""+matNr,uni};
	String[] types = new String[]{"int","String"};
	ResultSet ergebnis = executeStatement(query,params,types);
	try{
		if (ergebnis.next()){
			return ergebnis.getString("processInstance");
		} else {
			return "";
		}
	} catch (Exception e){
		e.printStackTrace();
		return "";
	}
}

public static void createInstance(String instanceID, String uni, int matNr, int stepCount){
	//Nutzerinstanz eintragen}
	String query = "INSERT INTO MapUserInstanz (matrikelnummer, uni, processInstance, status) VALUES (?,?,?,?)";
	String[] params = new String[]{""+matNr,uni,instanceID,"1"};
	String[] types = new String[]{"int","String","String","String"};
	executeUpdate(query,params,types);
	//Bewerbungsprozess eintragen
	System.out.println("InsertIntoBewerbungsprozess");
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String dateString = dateFormat.format(date);
	query = "INSERT INTO bewerbungsprozess (matrikelnummer, uniName, startDatum, Schritte_aktuell, Schritte_gesamt) VALUES (?,?,?,?,?)";
	params = new String[]{""+matNr,uni,dateString,"0",""+stepCount};
	types = new String[]{"int","String","date","int","int"};
	executeUpdate(query,params,types);
}

public static String getAllActivities(String definitionKey){
	String query = "SELECT `step` FROM `processModel` WHERE `model` = ? ORDER BY `stepNumber`";
	String[] params = new String[]{definitionKey};
	String[] types = new String[]{"String"};
	String activities = "";
	ResultSet result =  executeStatement(query,params,types);
	try{
		while (result.next()){
			activities = activities + result.getString("step") + ";";
		}
		return activities;
	} catch (Exception e){
		e.printStackTrace();
		return "";
	}
}

public static String[] getUserData(int matNr){ //Gibt Name|Vorname|Mailadresse zurück
	String queryString = "SELECT nachname,vorname,email FROM user WHERE matrikelnummer = ?;";
	String[] params = new String[]{""+matNr};
	String[] types = new String[]{"int"};
	ResultSet ergebnis = executeStatement(queryString,params,types);
	try{
		 if(ergebnis.next()){
			 return new String[]{ergebnis.getString("nachname"),ergebnis.getString("vorname"),ergebnis.getString("email")};
		 } else {
			 return new String[0];
		 }

	}
	catch (Exception e){
		e.printStackTrace();
		return new String[0];
	}
}

public static String getmodel(String uni) {
	String queryy = "SELECT model FROM cms_auslandsAngeboteInhalt WHERE uniTitel=?";
	String[] args = new String[]{uni};
	String[] types = new String[]{"String"};
	ResultSet modell = executeStatement(queryy, args, types);
	try{
		if (modell.next()){
			return modell.getString("model");
		} else {
			return "";
		}
	} catch (Exception e){
		e.printStackTrace();
		return "";
	}
}

public static ArrayList<String[]> getUserInstances(int matNr){ //Uni|instanceID für alles Instanzen zurück (Getrennt nach List-Entries)
	String queryString = "SELECT uni,processInstance FROM MapUserInstanz WHERE matrikelnummer = ?;";
	String[] params = new String[]{""+matNr};
	String[] types = new String[]{"int"};
	ResultSet ergebnis = executeStatement(queryString,params,types);
	ArrayList<String[]> antwort = new ArrayList<String[]>();
	try{
		 while(ergebnis.next()){
			 antwort.add(new String[]{ergebnis.getString("uni"),ergebnis.getString("processInstance")});
		 }
		return antwort;
	}
	catch (Exception e){
		e.printStackTrace();
		return new ArrayList<String[]>();
	}
}

private static int getTotalSteps(String model){
	String query = "SELECT max(stepNumber) FROM processModel WHERE model = ?";
	String[] params = new String[]{model};
	String[] types = new String[]{"String"};
	ResultSet result =  executeStatement(query,params,types);
	try {
		result.next();
		return result.getInt("max(stepNumber)");
	}
	catch (Exception e){
		e.printStackTrace();
		return 0;
	}
}

public static String getStepCounter(String step, String model){
	String query = "SELECT stepNumber FROM processModel WHERE model = ? AND step = ?";
	String[] params = new String[]{model,step};
	String[] types = new String[]{"String","String"};
	ResultSet result =  executeStatement(query,params,types);
	try{
		if (result.next()){
			return "Schritt " + result.getInt("stepNumber") + " von " + getTotalSteps(model);
		} else {
			return "Fehler";
		}
	} catch (Exception e){
		e.printStackTrace();
		return "Fehler";
	}
}

public static String forgetPassword(String mail){
	UUID uuid = UUID.randomUUID();
	String query = "UPDATE user SET resetToken = ? WHERE email = ?";
	String[] params = new String[]{""+ uuid,mail};
	String[] types = new String[]{"String","String"};
	executeUpdate(query,params,types);
	return "" + uuid;
}

public static int setPassword(String uuid, String pwd){
	String salt = Util.generateSalt();
    String hashedpw = Util.HashSha256(Util.HashSha256(pwd) + salt);
	String query = "UPDATE user SET passwort = ?, salt = ?, resetToken = ''  WHERE resetToken = ?";
	String[] params = new String[]{hashedpw,salt,uuid};
	String[] types = new String[]{"String","String","String"};
	return executeUpdate(query,params,types);
}

}

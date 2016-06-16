package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import dhbw.mwi.Auslandsemesterportal2016.Auslandsemesterportal2016ProcessApplication;

/**
 * Servlet implementation class prozess_db
 */
@MultipartConfig(maxFileSize = 16177215) // file size up to 16MB
@WebServlet(name = "login_db", description = "connection to DB for the prozess.jsp", urlPatterns = {
		"/WebContent/login_db" })

public class login_db extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// JDBC driver name and database URL
	final String DB_URL = "jdbc:mysql://193.196.7.215:3306/mwi";
	// Database account
	final String USER = "mwi";
	final String PASS = "mwi2014";
	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	int rsupd;
	String uuidCode;
	
	ProcessInstance pI;
	ProcessEngine processEngine;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public login_db() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		String sql = "";
		

		// if (action.equals("get_files") ){
		// sql = "SELECT id, name, comment FROM prozess_files";
		// }

		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// Execute SQL query
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int spalten = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int k = 1; k <= spalten; k++) {
					out.println(rs.getString(k) + ";");
					System.out.println(rs.getString(k) + ";");
				}
			}
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			System.out.println("Fehler se");
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			System.out.println("Fehler e");
		} finally {
			System.out.println("Done doGet");
			try {
				// Clean-up environment
				rs.close();
				stmt.close();
				conn.close();
			} catch (Exception ex) {
				System.out.println("Exception : " + ex.getMessage());
			}
		}

	}
	
	protected static String HashSha256(String input) {
        
        String result = null;
         
        if(null == input) return null;
         
        try {
             
            MessageDigest md = MessageDigest.getInstance("SHA-256");
		
            md.update(input.getBytes());

            byte[] hash = md.digest();
            
            result = javax.xml.bind.DatatypeConverter.printHexBinary(hash).toLowerCase();
             
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
	
	protected static String generateSalt() {
		
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
		
		String result = javax.xml.bind.DatatypeConverter.printHexBinary(bytes).toLowerCase();
		
        return result;
		
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		String sql = "leer";
		String sqlsalt = "leer";
		String sqlupd = "leer";
		int uid = 0;
		int rolle = 0;
		System.out.println("Post empfangen");
		if (action.equals("sendmail")) {
			// String to = "patrick.julius@web.de";
			// String from = request.getParameter("name");
			// String host = "localhost";
			// Properties properties = System.getProperties();
			// properties.setProperty("mail.smtp.host", host);
			// Session session = Session.getDefaultInstance(properties);
			//
			// try{
			// // Create a default MimeMessage object.
			// MimeMessage message = new MimeMessage(session);
			//
			// // Set From: header field of the header.
			// message.setFrom(new InternetAddress(from));
			//
			//// // Set To: header field of the header.
			// message.addRecipient(Message.RecipientType.TO,
			// new InternetAddress(to));
			//
			// // Set Subject: header field
			// message.setSubject("Bewerbung f�r " +
			// request.getParameter("uni"));
			//
			// // Now set the actual message
			// message.setText("Hallo Frau Dreischer, der Student " +
			// request.getParameter("name") + " mit der Matrikelnummer " +
			// request.getParameter("matrikelnummer") + " hat das
			// Bewerbungsfomular f�r die Universit�t " +
			// request.getParameter("uni") + " abgeschlossen. Sie k�nnen seine
			// Daten im Auslandsportal unter dem Reiter Bewerber abfragen. Der
			// Student wartet jetzt auf Meldung von Ihnen. Bitte senden sie ihm
			// eine Email, welche Schritte er weiter durchf�hren muss. MfG Das
			// Auslandsportalteam.");
			//
			// // Send message
			// Transport.send(message);
			// System.out.println("Sent message successfully....");
			// }catch (MessagingException mex) {
			// mex.printStackTrace();
			// }
		} else {
			if (action.equals("post_login")) {
				// parsing from the date and time is within the SQL-Statment.
				// For the DATE the
				// form is YYYY-MM-DD. For TIME it is HH:MM:SS
				
				//SQL-Statement für Salt vorbereiten
				sqlsalt = "SELECT salt FROM user WHERE '" + request.getParameter("vorname")
				+ "'= vorname AND '" + request.getParameter("nachname") + "' = nachname";
				String salt = null;
				
				//Verbindung zur DB um Salt abzurufen
				try {
					// Register JDBC driver
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					// Open a connection
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
					// Execute SQL query
					stmt = conn.createStatement();
					System.out.println("Connect");
					if (sqlsalt != "leer") {
						rs = stmt.executeQuery(sqlsalt);
						int spalten = rs.getMetaData().getColumnCount();
						while (rs.next()) {
							for (int k = 1; k <= spalten; k++) {
								//Salt abrufen
								salt = rs.getString(k);
							}
						}
						sqlsalt = "leer";
					}

				} catch (SQLException se) {
					// Handle errors for JDBC
					se.printStackTrace();
					System.out.println("Fehler se");
				} catch (Exception e) {
					// Handle errors for Class.forName
					e.printStackTrace();
					System.out.println("Fehler e");
				} finally {
					System.out.println("Done salt");
					try {
						// Clean-up environment
						rs.close();
						stmt.close();
						conn.close();
					} catch (Exception ex) {
						System.out.println("Exception : " + ex.getMessage());
					}
				}
				
				//Berechnung des Passworthashes aus gehashtem Eingabewert und Salt
				String pw = HashSha256( HashSha256(request.getParameter("pw")) + salt );
				
				//SQL-Statement für die Anmeldung
				sql = "SELECT rolle, matrikelnummer, studiengang FROM user WHERE '" + request.getParameter("vorname")
						+ "'= vorname AND '" + request.getParameter("nachname") + "' = nachname AND '"
						+ pw + "' = passwort";
				System.out.println(sql);
			} else if (action.equals("post_register")) {
				if (request.getParameter("rolle").equals("Studierender")) {
					rolle = 3;
				} else if (request.getParameter("rolle").equals("Auslandsmitarbeiter")) {
					rolle = 2;
				}

				// registrierungsemail senden
				// Recipient's email ID needs to be mentioned.
				String to = request.getParameter("email");// change accordingly

				// Sender's email ID needs to be mentioned
				String from = "mwiausland@gmail.com";// change accordingly
				final String username = "mwiausland@gmail.com";// change
																// accordingly
				final String password = "MWIAusland1";// change accordingly

				// Assuming you are sending email through relay.jangosmtp.net
				String host = "smtp.gmail.com";

				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.port", "587");

				// Get the Session object.
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				try {
					// Create a default MimeMessage object.
					Message message = new MimeMessage(session);

					// Set From: header field of the header.
					message.setFrom(new InternetAddress(from));

					// Set To: header field of the header.
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

					// Set Subject: header field
					message.setSubject("Akademisches Auslandsamt Registrierung");

					// Now set the actual message
					/*
					 * message.setText(
					 * "Hello, this is sample for to check send " +
					 * "email using JavaMailAPI ");
					 */
					UUID id = UUID.randomUUID();
					System.out.println(id);
					
					//Zufälliges Salt generieren und Passwort hashen
					String salt = generateSalt();
					String pw = HashSha256( HashSha256(request.getParameter("passwort")) + salt );					
					
					sqlupd = "INSERT INTO user (vorname, nachname, passwort, salt, rolle, email, studiengang, kurs, matrikelnummer, tel, mobil, standort, verifiziert) VALUES ('"
							+ request.getParameter("vorname") + "', '" + request.getParameter("nachname") + "', '"
							+ pw + "', '" + salt + "', '" + rolle + "', '" + request.getParameter("email")
							+ "', '" + request.getParameter("studiengang") + "', '" + request.getParameter("kurs")
							+ "', '" + request.getParameter("matrikelnummer") + "', '" + request.getParameter("tel")
							+ "', '" + request.getParameter("mobil") + "', '" + request.getParameter("standort")
							+ "', '" + id + "')";
					System.out.println(sqlupd);
					String link = "193.196.7.215:8080/Auslandsemesterportal2016/WebContent/index.html?confirm=" + id;
					

					message.setContent("<h2>Hallo " + request.getParameter("vorname")
							+ ",</h2> du hast dich auf der Seite des Auslandsportals registriert. "
							+ "Um deine Registrierung abzuschlie&szlig;en klicke bitte auf folgenden Link. <br><br> "
							+ "<a href=" + link + ">Anmeldung best&auml;tigen</a>", "text/html");

					// Send message
					Transport.send(message);

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
				//wird am anfang immer aufgerufen k.A. weshalb deshalb wird hier überprüft, ob verifizierungsversuch
			} else if (action.equals("get_portalInfo")) {
				confirm(request.getParameter("confirm"));
				
				
				sql = "SELECT titel, listelement1, listelement2, listelement3, listelement4, listelement5, listelement6, listelement7 FROM cms_portalInfo";
				
			} else if (action.equals("post_portalInfo")) {
				sqlupd = "UPDATE cms_portalInfo SET titel = '" + request.getParameter("titel") + "', listelement1 = '"
						+ request.getParameter("listelement1") + "' , listelement2 = '"
						+ request.getParameter("listelement2") + "', listelement3 = '"
						+ request.getParameter("listelement3") + "' , listelement4 = '"
						+ request.getParameter("listelement4") + "' , listelement5 = '"
						+ request.getParameter("listelement5") + "' , listelement6 = '"
						+ request.getParameter("listelement6") + "' , listelement7 = '"
						+ request.getParameter("listelement7") + "'";
				System.out.println(sqlupd);
				
			} else if (action.equals("get_User")) {
				sql = "SELECT nachname, vorname, email, tel, mobil, studiengang, kurs, matrikelnummer, standort FROM user WHERE rolle ='"
						+ request.getParameter("rolle") + "' ";
				System.out.println("HIER");
				
			} else if (action.equals("get_Auslandsangebote")) {
				sql = "SELECT studiengang FROM cms_auslandsAngebote WHERE ID > 0";
				
			} else if (action.equals("get_AuslandsangeboteInhalt")) {
				sql = "SELECT uniTitel, allgemeineInfos, faq, erfahrungsbericht, bilder, bewerben FROM cms_auslandsAngeboteInhalt WHERE studiengang ='"
						+ request.getParameter("studiengang") + "' ";
				
			} else if (action.equals("get_userDaten")) {
				sql = "SELECT nachname, vorname, email, studiengang, kurs, standort, tel, mobil FROM user WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnr") + "' ";
				
			} else if (action.equals("get_Unis")) {
				
				/*Prozess studentBewerben wird gestartet*/
				processEngine = ProcessEngines.getDefaultProcessEngine();
				pI = new Auslandsemesterportal2016ProcessApplication().bewerbungStarten(processEngine);	
				
				sql = "SELECT uniTitel FROM cms_auslandsAngeboteInhalt WHERE studiengang ='"
						+ request.getParameter("studiengang") + "' ";
				
				/*SQL-Befehl für das Mapping von User und Prozessinstanz: !!!!! ANPASSUNG DER BEWERBUNGSPORTAL.JS-DATEI FEHLT!!!!!!
				 * "INSERT INTO MapUserInstanz (email, processInstance, status) VALUES ('" + request.getParameter("bewerberEmail") + "', '" + pI.getId() + "', '" + 1 + "')"; */
				
			} else if (action.equals("post_prozessStart")) {
				
				/*SQL-Befehl für das Auslesen der Prozessinstanz des Users: !!!!! ANPASSUNG DER BEWERBUNGSPORTAL.JS-DATEI FEHLT!!!!!!
				 * "SELECT processInstance FROM MapUserInstanz WHERE studiengang ='" + request.getParameter("bewerberEmail") + "' "; */
				
				/* Complete Task "Downloads anbieten" */
				completeTask();
				
				sqlupd = "INSERT INTO bewerbungsprozess (matrikelnummer, uniName, startDatum, schritt_1, schritt_2, schritt_3, schritt_4, schritt_5) VALUES ('"
						+ request.getParameter("matrikelnummer") + "', '" + request.getParameter("uni") + "', '"
						+ request.getParameter("datum") + "', '" + request.getParameter("schritt1") + "', '"
						+ request.getParameter("schritt2") + "', '" + request.getParameter("schritt3") + "', '"
						+ request.getParameter("schritt4") + "', '" + request.getParameter("schritt5") + "')";
				
			} else if (action.equals("get_prozessStatus")) {
				sql = "SELECT uniName, startDatum, schritt_1, schritt_2, schritt_3, schritt_4, schritt_5 FROM bewerbungsprozess WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' ";
				
			} else if (action.equals("get_Studiengaenge")) {
				sql = "SELECT studiengang FROM cms_auslandsAngebote";
				
			} else if (action.equals("get_angeboteDaten")) {
				sql = "SELECT studiengang, uniTitel, allgemeineInfos, faq, erfahrungsbericht, maps FROM cms_auslandsAngeboteInhalt";
				
			} else if (action.equals("post_newStudiengang")) {
				sqlupd = "INSERT INTO cms_auslandsAngebote (studiengang) VALUES ('"
						+ request.getParameter("studiengang") + "') ";
				
			} else if (action.equals("post_newAuslandsangebot")) {
				sqlupd = "INSERT INTO cms_auslandsAngeboteInhalt (studiengang, uniTitel, allgemeineInfos, faq, erfahrungsbericht, maps) VALUES ('"
						+ request.getParameter("studiengang") + "', '" + request.getParameter("uniTitel") + "', '"
						+ request.getParameter("allgemeineInfos") + "', '" + request.getParameter("faq") + "', '"
						+ request.getParameter("erfahrungsbericht") + "', '" + request.getParameter("maps") + "')";
				
			} else if (action.equals("post_editAuslandsangebot")) {
				sqlupd = "UPDATE cms_auslandsAngeboteInhalt SET allgemeineInfos = '"
						+ request.getParameter("allgemeineInfos") + "' , faq = '" + request.getParameter("faq")
						+ "', erfahrungsbericht = '" + request.getParameter("erfahrungsbericht") + "' , maps = '"
						+ request.getParameter("maps") + "' WHERE uniTitel ='" + request.getParameter("uniTitel")
						+ "' ";
				
			} else if (action.equals("post_infoMaterial")) {
				sqlupd = "UPDATE cms_infoMaterial SET titel = '" + request.getParameter("titel")
						+ "' , listelement1 = '" + request.getParameter("listelement1") + "' , link1 = '"
						+ request.getParameter("link1") + "' , listelement2 = '" + request.getParameter("listelement2")
						+ "' , link2 = '" + request.getParameter("link2") + "' , listelement3 = '"
						+ request.getParameter("listelement3") + "' , link3 = '" + request.getParameter("link3")
						+ "' , listelement4 = '" + request.getParameter("listelement4") + "' , link4 = '"
						+ request.getParameter("link4") + "' , listelement5 = '" + request.getParameter("listelement5")
						+ "' , link5 = '" + request.getParameter("link5") + "' , listelement6 = '"
						+ request.getParameter("listelement6") + "' , link6 = '" + request.getParameter("link6")
						+ "' , listelement7 = '" + request.getParameter("listelement7") + "' , link7 = '"
						+ request.getParameter("link7") + "' ";
				
			} else if (action.equals("get_infoMaterial")) {
				sql = "SELECT titel, listelement1, link1, listelement2, link2, listelement3, link3, listelement4, link4, listelement5, link5, listelement6, link6, listelement7, link7 FROM cms_infoMaterial";
				
			} else if (action.equals("get_bewerber")) {
				sql = "SELECT matrikelnummer, uniName, startDatum, schritt_1, schritt_2, schritt_3, schritt_4, schritt_5 FROM bewerbungsprozess";
				
			} else if (action.equals("update_User")) {
				
				/* Complete Task "Daten eingeben" */
				completeTask();
				
				sqlupd = "UPDATE user SET vorname = '" + request.getParameter("vorname") + "' , nachname = '"
						+ request.getParameter("nachname") + "' , email = '" + request.getParameter("email")
						+ "' , tel = '" + request.getParameter("telefon") + "' , mobil = '"
						+ request.getParameter("mobil") + "' , studiengang = '" + request.getParameter("studiengang")
						+ "' , kurs = '" + request.getParameter("kurs") + "' WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' ";
				
			} else if (action.equals("insert_EnglischAbi")) {
				sqlupd = "INSERT INTO englischnote (matrikelnummer, englischAbi) VALUES ('"
						+ request.getParameter("matrikelnummer") + "', '" + request.getParameter("abinote") + "') ";
				
			} else if (action.equals("get_Note")) {
				sql = "SELECT englischAbi FROM englischnote WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' ";
				
			} else if (action.equals("insert_Adresse")) {
				sqlupd = "INSERT INTO adresse (matrikelnummer, phase, strasse, hausnummer, plz, ort, bundesland, land) VALUES ('"
						+ request.getParameter("matrikelnummer") + "', '" + request.getParameter("phase") + "', '"
						+ request.getParameter("strasse") + "', '" + request.getParameter("hausnummer") + "', '"
						+ request.getParameter("plz") + "', '" + request.getParameter("stadt") + "', '"
						+ request.getParameter("bundesland") + "', '" + request.getParameter("land") + "') ";
				
			} else if (action.equals("get_Adresse")) {
				sql = "SELECT strasse, hausnummer, plz, ort, bundesland, land FROM adresse WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' AND phase = '" + request.getParameter("phase")
						+ "' ";
				
			} else if (action.equals("get_Partnerunternehmen")) {
				sql = "SELECT firma, ansprechpartner, email, strasse, hausnummer, plz, stadt FROM partnerunternehmen WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' ";
				
			} else if (action.equals("insert_Partnerunternehmen")) {
				sqlupd = " INSERT INTO partnerunternehmen (firma, ansprechpartner, email, strasse, hausnummer, plz, stadt, matrikelnummer) VALUES ('"
						+ request.getParameter("firma") + "', '" + request.getParameter("email") + "', '"
						+ request.getParameter("ansprechpartner") + "', '" + request.getParameter("strasse") + "', '"
						+ request.getParameter("hausnummer") + "', '" + request.getParameter("plz") + "', '"
						+ request.getParameter("stadt") + "', '" + request.getParameter("matrikelnummer") + "') ";
				
			} else if (action.equals("update_Adresse")) {
				sqlupd = "UPDATE adresse SET strasse = '" + request.getParameter("strasse") + "', hausnummer = '"
						+ request.getParameter("hausnummer") + "', plz = '" + request.getParameter("plz") + "', ort = '"
						+ request.getParameter("stadt") + "', bundesland = '" + request.getParameter("bundesland")
						+ "', land = '" + request.getParameter("land") + "' WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' AND phase = '" + request.getParameter("phase")
						+ "' ";
				
			} else if (action.equals("update_Partnerunternehmen")) {
				sqlupd = "UPDATE partnerunternehmen SET ansprechpartner = '" + request.getParameter("ansprechpartner")
						+ "', strasse = '" + request.getParameter("strasse") + "', hausnummer = '"
						+ request.getParameter("hausnummer") + "', plz = '" + request.getParameter("plz")
						+ "', stadt = '" + request.getParameter("stadt") + "', email = '"
						+ request.getParameter("email") + "' WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' ";
				
			} else if (action.equals("update_BewProzess1")) {
				sqlupd = "UPDATE bewerbungsprozess SET schritt_1 = 1 WHERE matrikelnummer = '"
						+ request.getParameter("matrikelnummer") + "' AND uniName = '" + request.getParameter("uni")
						+ "' ";
			}

			try {
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				// Open a connection
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				// Execute SQL query
				stmt = conn.createStatement();
				System.out.println("Connect");
				if (sql != "leer") {
					rs = stmt.executeQuery(sql);
					int spalten = rs.getMetaData().getColumnCount();
					while (rs.next()) {
						for (int k = 1; k <= spalten; k++) {
							out.println(rs.getString(k) + ";");
							System.out.println(rs.getString(k) + ";");
						}
					}
					sql = "leer";
				} else if (sqlupd != "leer") {
					rsupd = stmt.executeUpdate(sqlupd);
					out.println(rsupd);
					sqlupd = "leer";
				}

			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
				System.out.println("Fehler se");
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
				System.out.println("Fehler e");
			} finally {
				System.out.println("Done doPost");
				try {
					// Clean-up environment
					rs.close();
					stmt.close();
					conn.close();
				} catch (Exception ex) {
					System.out.println("Exception : " + ex.getMessage());
				}
			}
		}
	}
	
	/** Diese Methode komplettiert den jeweiligen Task*/
	public void completeTask(){
		/* Hier muss noch die Prozessinstanz-ID des jeweiligen Studenten als Parameter mitgegeben werden. pI.getId() muss
		 * entsprechend geändert werden. */
		
		processEngine.getTaskService().complete(
								processEngine.getTaskService().createTaskQuery().
													processInstanceId(pI.getId()).singleResult().getId());
	}

	public void confirm(String code) {
		
		String userId="_";
		if (code != null) {
			
			try {
				String sql = "SELECT userID FROM user WHERE verifiziert='" + code + "'";
				System.out.println(sql);
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				// Open a connection
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				// Execute SQL query
				stmt = conn.createStatement();
				System.out.println("Connect");
				if (sql != "leer") {
					rs = stmt.executeQuery(sql);
					int spalten = rs.getMetaData().getColumnCount();
					while (rs.next()) {
						for (int k = 1; k <= spalten; k++) {
							
							System.out.println(rs.getString(k) + ";");
							userId=rs.getString(k);
						}
					}
					sql = "leer";
					
				}
			 if(userId!="_"){
				 String query = "UPDATE user SET verifiziert = 1 WHERE userID=" + userId;
				 rsupd = stmt.executeUpdate(query);
				 
			 }
			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
				System.out.println("Fehler se");
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
				System.out.println("Fehler e");
			} finally {
				System.out.println("Done doPost");
				try {
					// Clean-up environment
					rs.close();
					stmt.close();
					conn.close();
				} catch (Exception ex) {
					System.out.println("Exception : " + ex.getMessage());
				}
			}

		}
	}
}

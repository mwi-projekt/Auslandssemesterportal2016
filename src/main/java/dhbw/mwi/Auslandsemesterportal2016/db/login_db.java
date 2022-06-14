package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;

import dhbw.mwi.Auslandsemesterportal2016.Config;
import dhbw.mwi.Auslandsemesterportal2016.enums.MessageEnum;

/**
 * Servlet implementation class prozess_db
 */
@MultipartConfig(maxFileSize = 16177215) // file size up to 16MB
@WebServlet(name = "login_db", description = "connection to DB for the prozess.jsp", urlPatterns = { "/login_db" })

public class login_db extends HttpServlet implements TaskListener {
	private static final long serialVersionUID = 1L;

	Connection conn;
	java.sql.Statement stmt;
	ResultSet rs;
	int rsupd;
	String uuidCode;

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

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
		String sql = "";

		// if (action.equals("get_files") ){
		// sql = "SELECT id, name, comment FROM prozess_files";
		// }

		try {
			// Open a connection
			conn = DB.getInstance();
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
		String sqlupd = "leer";

		if (action.equals("post_portalInfo")) {
			sqlupd = "UPDATE cms_portalInfo SET titel = '" + request.getParameter("titel") + "', listelement1 = '"
					+ request.getParameter("listelement1") + "' , listelement2 = '"
					+ request.getParameter("listelement2") + "', listelement3 = '"
					+ request.getParameter("listelement3") + "' , listelement4 = '"
					+ request.getParameter("listelement4") + "' , listelement5 = '"
					+ request.getParameter("listelement5") + "' , listelement6 = '"
					+ request.getParameter("listelement6") + "' , listelement7 = '"
					+ request.getParameter("listelement7") + "'";
			System.out.println(sqlupd);

		} else if (action.equals("post_newStudiengang")) {
			sqlupd = "INSERT INTO cms_auslandsAngebote (studiengang) VALUES ('" + request.getParameter("studiengang")
					+ "') ";

		} else if (action.equals("post_newAuslandsangebot")) {
			sqlupd = "INSERT INTO cms_auslandsAngeboteInhalt (studiengang, uniTitel, allgemeineInfos, faq, erfahrungsbericht, maps) VALUES ('"
					+ request.getParameter("studiengang") + "', '" + request.getParameter("uniTitel") + "', '"
					+ request.getParameter("allgemeineInfos") + "', '" + request.getParameter("faq") + "', '"
					+ request.getParameter("erfahrungsbericht") + "', '" + request.getParameter("maps") + "')";

		} else if (action.equals("post_editAuslandsangebot")) {
			sqlupd = "UPDATE cms_auslandsAngeboteInhalt SET allgemeineInfos = '"
					+ request.getParameter("allgemeineInfos") + "' , faq = '" + request.getParameter("faq")
					+ "', erfahrungsbericht = '" + request.getParameter("erfahrungsbericht") + "' , maps = '"
					+ request.getParameter("maps") + "' WHERE uniTitel ='" + request.getParameter("uniTitel") + "' ";

		} else if (action.equals("post_infoMaterial")) {
			sqlupd = "UPDATE cms_infoMaterial SET titel = '" + request.getParameter("titel") + "' , listelement1 = '"
					+ request.getParameter("listelement1") + "' , link1 = '" + request.getParameter("link1")
					+ "' , listelement2 = '" + request.getParameter("listelement2") + "' , link2 = '"
					+ request.getParameter("link2") + "' , listelement3 = '" + request.getParameter("listelement3")
					+ "' , link3 = '" + request.getParameter("link3") + "' , listelement4 = '"
					+ request.getParameter("listelement4") + "' , link4 = '" + request.getParameter("link4")
					+ "' , listelement5 = '" + request.getParameter("listelement5") + "' , link5 = '"
					+ request.getParameter("link5") + "' , listelement6 = '" + request.getParameter("listelement6")
					+ "' , link6 = '" + request.getParameter("link6") + "' , listelement7 = '"
					+ request.getParameter("listelement7") + "' , link7 = '" + request.getParameter("link7") + "' ";

		}
		try {
			// Open a connection
			conn = DB.getInstance();
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

	public void updateProcess(String sqlStatement) {
		Connection connection = null;
		java.sql.Statement statement = null;

		try {
			// Open a connection
			connection = DB.getInstance();

			// Execute SQL query
			statement = connection.createStatement();
			statement.executeUpdate(sqlStatement);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
			System.out.println("Fehler SQL updateProcess");
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			System.out.println("Fehler updateProcess");
		} finally {
			try {
				// Clean-up environment
				statement.close();
				connection.close();
			} catch (Exception ex) {
				System.out.println("Exception : " + ex.getMessage());
			}
		}

	}

	/** Diese Methode komplettiert den jeweiligen Task */
	public void completeTask(String instanceId) {
		processEngine.getTaskService().complete(
				processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult().getId());
	}

	/**
	 * Diese Methode komplettiert den jeweiligen Task und setzt entsprechende
	 * Variablen
	 */
	public void completeTask(String instanceId, Map<String, Object> variables) {
		processEngine.getTaskService().complete(
				processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult().getId(),
				variables);
	}

	public String getTaskName(String instanceId) {
		return processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult().getName();
	}

	public boolean getEnglischBoolean(String matrikelnummer) {
		Connection connection = null;
		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		boolean result = false;

		String sql = "SELECT englischAbi FROM englischnote WHERE matrikelnummer = '" + matrikelnummer + "' ";

		try {
			// Open a connection
			connection = DB.getInstance();

			// Execute SQL query
			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			int note = 0;
			// Note auslesen
			while (resultSet.next()) {
				note = Integer.parseInt(resultSet.getString(1));
			}

			if (note >= 11) {
				result = true;
			}
		} catch (SQLException e) {
			System.out.print("ERROR - ProcessService.getProcessId -SQLException");
			e.printStackTrace();
		} finally {
			try {
				// Clean-up environment
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception ex) {
				System.out.println("Exception : " + ex.getMessage());
			}
		}

		return result;
	}

	public String getEnglischNote(String matrikelnummer) {
		Connection connection = null;
		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		String note = "leer";

		String sql = "SELECT englischAbi FROM englischnote WHERE matrikelnummer = '" + matrikelnummer + "' ";

		try {
			// Open a connection
			connection = DB.getInstance();

			// Execute SQL query
			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			// Note auslesen
			while (resultSet.next()) {
				note = resultSet.getString(1);
			}
		} catch (SQLException e) {
			System.out.print("ERROR - ProcessService.getProcessId -SQLException");
			e.printStackTrace();
		} finally {
			try {
				// Clean-up environment
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception ex) {
				System.out.println("Exception : " + ex.getMessage());
			}
		}

		return note;
	}

	/**
	 * TASK LISTENER ASSIGNMENT: Methode dient zum Benachrichtigen des
	 * Auslandsmitarbeiter
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		// Automatic Mail Server Properties

		try {
			// TODO: Automatisch den zuständigen AAMitarbeiter ermitteln
			Message message = Util.getEmailMessage("mwiausland@gmail.com", MessageEnum.AAAREGISTR.toString());

			message.setContent("Sehr geehrte Frau Dreischer," + "\n" + "\n"
					+ "ein weiterer Student hat das Bewerbungsfomular für ein Auslandssemester abgeschlossen." + "\n"
					+ "Sie können seine Daten in der Camunda Tasklist unter folgendem Link nachvollziehen:" + "\n"
					+ Config.CAMUNDA_URL + "/app/tasklist/default/#/?task=" + delegateTask.getId(),
					"text/plain; charset=UTF-8");

			// Send message
//			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// SEND TASK
	// Methode dient zum Versenden von Email an Student nach
	// Validierung der getätigten Eingaben
	// FIXME klären, ob löschen in Ordnung (nicht mehr benötigt aufgrund GoOut-Schnittstelle)
//	@Override
//	public void execute(DelegateExecution execution) throws Exception {
//		String email = (String) execution.getVariable("bewEmail");
//		boolean erfolgreich = (Boolean) execution.getVariable("validierungErfolgreich");
//		String mailText = (String) execution.getVariable("mailText");
//
//		try {
//			Message message;
//
//			// Bei erfolgreicher Validierung
//			if (erfolgreich) {
//				message = Util.getEmailMessage(email, "Eingereichte Bewerbung für Auslandssemester validiert");
//				message.setContent(mailText, "text/plain; charset=UTF-8");
//			}
//			// wenn Validierung fehlgeschlagen
//			else {
//				message = Util.getEmailMessage(email, "Bei der Validierung Ihrer Bewerbung ist ein Fehler aufgetreten");
//				message.setContent(mailText, "text/plain; charset=UTF-8");
//			}
//
////			Transport.send(message);
//
//		} catch (MessagingException e) {
//			System.out.print("Could not send email!");
//			e.printStackTrace();
//		}
//	}
}

package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class prozessInhalt_db
 */

@WebServlet("/prozessInhalt_db")
@MultipartConfig
public class prozessInhalt_db extends HttpServlet {

	private static final long serialVersionUID = 1L;
    
	// JDBC driver name and database URL
    final String DB_URL="jdbc:mysql://193.196.7.215:3306/mwi";
	// Database account
    final String USER = "mwi";
    final String PASS = "mwi2014";
    java.sql.Connection conn;
    java.sql.Statement stmt;
    ResultSet rs;

    
    // DB FIELDS
    private static final String dbf_timestampDokumentePostalischVersendet = " timestampDokumentePostalischVersendet";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public prozessInhalt_db() {
        super();
        System.err.println("FOUND IT");
        // TODO Auto-generated constructor stub
    }

  //**************************************************************************************************************************************
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			String action = request.getParameter("action");
			String sql = "";
			
			if ( action.equals("get_document") ){
				sql = "SELECT (name, dokument, "
							+ " checkboxAutoHochgeladen,"
							+ " timestampHochgeladen, "
							+ " checkboxAuslandsamtDokumenteKorrekt, "
							+ " timestampKorrektHochgeladen,"
							+ " checkboxDokumentePostalischVersendet, "
							+ " timestampDokumentePostalischVersendet,"
							+ " checkboxDokumentePostalischKorrekt, "
							+ " timestampDokumentePostalischKorrekt ";
			}

		    try{
		    	// Register JDBC driver
		          Class.forName("com.mysql.jdbc.Driver").newInstance();
		          // Open a connection
		          conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
		          
		          // Execute SQL query
		          stmt = (Statement) conn.createStatement();
		          rs = stmt.executeQuery(sql);
		          int spalten = rs.getMetaData().getColumnCount(); 
		          while(rs.next()){	        	  
		        	  for (int k = 1; k <= spalten; k++) {
		                   out.println( rs.getString(k)+ ";" );
		                   System.out.println(rs.getString(k)+ ";" );
		              }
		          }
		     }
		     catch(SQLException se){
		          //Handle errors for JDBC
		          se.printStackTrace();
		          System.out.println("Fehler se " + se);
		     }
		     catch(Exception e){
		          //Handle errors for Class.forName
		          e.printStackTrace();
		          System.out.println("Fehler e " + e);
		     }
		     finally{
		    	 System.out.println("Done doGet");
				 try{
					// Clean-up environment
					rs.close();
					stmt.close();
					conn.close();
				 }
				 catch(Exception ex){
					System.out.println( "Exception : " + ex.getMessage() );
				 }
		     }
		}

	//**************************************************************************************************************************************
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			//String action = request.getParameter("action");
			String sql = "";
			
				sql = "UPDATE dokument (name, dokument, "
							+ " checkboxAutoHochgeladen,"
							+ " timestampHochgeladen, "
							+ " checkboxAuslandsamtDokumenteKorrekt, "
							+ " timestampKorrektHochgeladen,"
							+ " checkboxDokumentePostalischVersendet, "
							+ " timestampDokumentePostalischVersendet,"
							+ " checkboxDokumentePostalischKorrekt, "
							+ " timestampDokumentePostalischKorrekt ";

		    try{
		    	// Register JDBC driver
		          Class.forName("com.mysql.jdbc.Driver").newInstance();
		          // Open a connection
		          conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
		          
		          // Execute SQL query
		          stmt = (Statement) conn.createStatement();
		          rs = stmt.executeQuery(sql);
		          int spalten = rs.getMetaData().getColumnCount(); 
		          while(rs.next()){	        	  
		        	  for (int k = 1; k <= spalten; k++) {
		                   out.println( rs.getString(k)+ ";" );
		                   System.out.println(rs.getString(k)+ ";" );
		              }
		          }
		     }
		     catch(SQLException se){
		          //Handle errors for JDBC
		          se.printStackTrace();
		          System.out.println("Fehler se " + se);
		     }
		     catch(Exception e){
		          //Handle errors for Class.forName
		          e.printStackTrace();
		          System.out.println("Fehler e " + e);
		     }
		     finally{
		    	 System.out.println("Done doUpdate");
				 try{
					// Clean-up environment
					rs.close();
					stmt.close();
					conn.close();
				 }
				 catch(Exception ex){
					System.out.println( "Exception : " + ex.getMessage() );
				 }
		     }
		}

//**************************************************************************************************************************************
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Funktioniert und sollte fast fertig sein!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			String action = request.getParameter("action");
			String sql = "";
			System.out.println("doPost called: " + request);
			
			//Timestamp variable f�r eine nicht markierte Checkbox
			Timestamp theTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
			Timestamp noTimeAtAll = new Timestamp(1L);
			
			//Hier werden die �bergebenen Daten der Checkbox (on/off) in einen Int-Wert (1/0) gewandelt
			int checkboxAutoHochgeladen = Integer.parseInt(request.getParameter("checkboxAutoHochgeladen"));
			Timestamp timeStampAutoHochgeladen = (checkboxAutoHochgeladen==1)?theTime:noTimeAtAll;
			int checkboxAuslandsamtDokumenteKorrekt = Integer.parseInt(request.getParameter("checkboxAuslandsamtDokumenteKorrekt"));
			Timestamp timeStampAuslandsamtDokumenteKorrekt = (checkboxAuslandsamtDokumenteKorrekt==1)?theTime:noTimeAtAll;
			int checkboxDokumentePostalischVersendet = Integer.parseInt(request.getParameter("checkboxDokumentePostalischVersendet"));
			Timestamp timeStampDokumentePostalischVersendet = (checkboxDokumentePostalischVersendet==1)?theTime:noTimeAtAll;
			int checkboxDokumentePostalischKorrekt = Integer.parseInt(request.getParameter("checkboxDokumentePostalischKorrekt"));
			Timestamp timeStampPostalischKorrekt = (checkboxDokumentePostalischKorrekt==1)?theTime:noTimeAtAll;

			if ( action.equals("post_dokument") ){
				/**
				 * 1. DB Lesen: Suche Datensatz mit matrikenr & name_dok
				 * wenn gefunden: mache update
				 * ansonsten das hier:
				 * 
				 * if(doGet() == "") {
				 * 
				 */
				System.out.println("post_dokument == action.");
					//SQL-Statement -> Trage diese Daten in die Tabelle dokumente ein
					sql = "INSERT INTO dokumente (matrikelnummer, name_dok, dokument, "
							+ "	checkboxAutoHochgeladen, "
							+ " timestampHochgeladen, "
							+ " checkboxAuslandsamtDokumenteKorrekt, "
							+ "	timestampKorrektHochgeladen, "
							+ " checkboxDokumentePostalischVersendet, "
							+ prozessInhalt_db.dbf_timestampDokumentePostalischVersendet + ", "
							+ " checkboxDokumentePostalischKorrekt, "
							+ " timestampDokumentePostalischKorrekt) "
							+ "VALUES ('"	+ request.getParameter("matrikelnummer") +"','"
											+ request.getParameter("name_dok") +"','" 
											+ request.getParameter("dokument") + "', '" 
											+ checkboxAutoHochgeladen +"','"
											+ timeStampAutoHochgeladen +"','"
											+ checkboxAuslandsamtDokumenteKorrekt + "','"
											+ timeStampAuslandsamtDokumenteKorrekt+ "','"
											+ checkboxDokumentePostalischVersendet + "','"
											+ timeStampDokumentePostalischVersendet + "','"
											+ checkboxDokumentePostalischKorrekt + "','"
											+ timeStampPostalischKorrekt
											+ "')";
			}
//			else {
//				doUpdate();
//			}
			
			System.out.println(sql);
		    try{
		    	// Register JDBC driver
		        Class.forName("com.mysql.jdbc.Driver").newInstance();
		        // Open a connection
		        conn = DriverManager.getConnection(DB_URL,USER,PASS);
		        // Execute SQL query
		        stmt = conn.createStatement();
		          
		        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);      
		          
		        ResultSet generatedKeys = stmt.getGeneratedKeys();
		        if(generatedKeys.next()){
		        	System.out.println("ID_DB = " + generatedKeys.getInt(1));
		        	out.println(generatedKeys.getInt(1));
		        }
		     }
		     catch(SQLException se){
		          //Handle errors for JDBC
		          se.printStackTrace();
		          System.out.println("Fehler se " + se);
		     }
		     catch(Exception e){
		          //Handle errors for Class.forName
		          e.printStackTrace();
		          System.out.println("Fehler e "+ e);
		     }
		     finally{
		    	 System.out.println("Done doPost");
				 try{
					// Clean-up environment
					rs.close();
					stmt.close();
					conn.close();
				 }
				 catch(Exception ex){
					System.out.println( "Exception : " + ex.getMessage() );
				 }
		     }			
		}
	}

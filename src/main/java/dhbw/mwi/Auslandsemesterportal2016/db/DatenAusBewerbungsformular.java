package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DatenAusBewerbungsformular
 */

@WebServlet("/DatenAusBewerbungsformular")
@MultipartConfig
public class DatenAusBewerbungsformular extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	// JDBC driver name and database URL
    final String DB_URL="jdbc:mysql://193.196.7.215:3306/mwi";
	// Database account
    final String USER = "mwi";
    final String PASS = "mwi2014";
    java.sql.Connection conn;
    java.sql.Statement stmt;
    ResultSet rs;
    
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatenAusBewerbungsformular() {
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
			
			//Hohle Daten aus den Datenbanken...
			if ( action.equals("get_bewerbung") ){
				sql = "SELECT (Attribut1, "
							+ " Attribut2, "
							+ " Attribut3 ";
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
			//�berschreibe Daten aus den Datenbanken...
				sql = "UPDATE TABELLE (Attribut1, "
							+ " Attribut2, "
							+ " Attribut3 ";

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
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			String action = request.getParameter("action");
			String sql = "";
			System.out.println("doPost called: " + request);

			if ( action.equals("post_bewerbung") ){
				/**
				 * 1. DB Lesen: Suche Datensatz mit matrikenr & BewerbungsID
				 * wenn gefunden: mache update
				 * ansonsten das hier:
				 * 
				 * if(doGet() == "") {
				 * 
				 */
				System.out.println("post_dokument == action.");
					//SQL-Statement -> Trage diese Daten in die Tabelle dokumente ein
					sql = "INSERT INTO studentErweitert (studentTel, "
							+ " studentMobil) "
							+ "VALUES ('"	+ request.getParameter("studentTel") +"','"
											+ request.getParameter("studnetMobil") +"')"; 
					sql = "INSERT INTO table (ATTRIBUT) VALUES ('" + request.getParameter("ATTRIBUT") +"')";
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

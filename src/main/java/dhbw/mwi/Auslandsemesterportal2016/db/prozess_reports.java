package src.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class prozess_reports extends TimerTask {
		//
		//Database url and port
		final String DB_URL="jdbc:mysql://193.196.7.215:3306/mwi";
    	//
		//Database account
    	final String USER = "mwi";
    	final String PASS = "mwi2014";
    	Connection conn;
    	Statement stmt;
    	ResultSet rs;
    	//
    	//Date format setting for compare and log
    	Date dNow = null;
		SimpleDateFormat ft = 
				new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
		SimpleDateFormat ftD = 
				new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat ftT = 
				new SimpleDateFormat ("kk:mm:ss");
		//
		//SQL statment for the operating with the DB
		String sql = null;
	//
	public void run() {
		ArrayList <String> data = new ArrayList<String>();
		dNow = new Date();
		//
		//the statements for the part of the server control
		sql = "SELECT do FROM prozess_srv WHERE id ='1'";
		data = DB(sql);
		System.out.println(data.get(0));
		if ( data.get(0).equals("up") ) {
			sql = "UPDATE prozess_srv SET status='up' WHERE id='1'";
			DB_control(sql);
			System.out.println( "-->JOB is running !!<-- " + ft.format(dNow) );
		}
		if ( data.get(0).equals("down") ) {
			sql = "UPDATE prozess_srv SET status='down' WHERE id='1'";
			DB_control(sql);
			System.out.println( "-->JOB is now NOT running !!<-- " + ft.format(dNow) );
			this.cancel();
		}
				//
				//
				//the statements for the cron-job part 
				sql = "SELECT * FROM prozess_terms WHERE DEnd ='" + "2015-01-01'";// + 
				//"' AND TEnd ='" + ftT.format(dNow) + "'";
				data = DB(sql);
				System.out.println(data);
	}
	//
	//is used to generate results from the DB
	private ArrayList<String> DB(String sql){
		ArrayList <String> data = new ArrayList<String>();
		try{
			//
			// Register JDBC driver
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        //
	        // Open a connection
	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
	        //
	        // Execute SQL query
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(sql);
	        int spalten = rs.getMetaData().getColumnCount(); 
	        while(rs.next()){	        	  
	        	for (int k = 1; k <= spalten; k++) {
	                data.add( rs.getString(k) );
	             }
	         }
	     }
	     catch(SQLException se){
	    	 //
	    	 //Handle errors for JDBC
	         se.printStackTrace();
	         System.out.println("Fehler se");
	     }
	     catch(Exception e){
	    	 //
	    	 //Handle errors for Class.forName
	         e.printStackTrace();
	         System.out.println("Fehler e");
	     }
	     finally{
	    	 System.out.println("Done doGet");
			 try{
				 //
				 // Clean-up environment
				 rs.close();
				 stmt.close();
				 conn.close();
			 }
			 catch(Exception ex){
				 System.out.println( "Exception : " + ex.getMessage() );
			 }
	     }
	return data;
	}
		//
		//
		//Control of the server 
		private void DB_control(String sql){
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
				}
			}
			catch(SQLException se){
				//
				//Handle errors for JDBC
				se.printStackTrace();
				System.out.println("Fehler se");
			}
			catch(Exception e){
				//
				//Handle errors for Class.forName
				e.printStackTrace();
				System.out.println("Fehler e");
			}
			finally{
				System.out.println("Done JOB Control status");
				try{
					//
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
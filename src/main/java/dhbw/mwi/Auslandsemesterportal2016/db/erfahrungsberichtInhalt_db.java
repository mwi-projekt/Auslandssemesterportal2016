package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class erfahrungsberichtInhalt_db
 */

@WebServlet("/erfahrungsberichtInhalt_db")
@MultipartConfig
public class erfahrungsberichtInhalt_db extends HttpServlet {

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
    public erfahrungsberichtInhalt_db() {
        super();
        System.err.println("FOUND IT");
        // TODO Auto-generated constructor stub
    }

  //**************************************************************************************************************************************
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		 
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			String action = request.getParameter("action");
			String updateSql = "";
			String insertSql = "";
			System.out.println("doPost called: " + request);
            //int stern = Integer.parseInt(request.getParameter("stern"));
			//int matrikelnummer = Integer.parseInt(request.getParameter("matrikelnummer"));
			int matrikelnummer= 999;
			
			int frageID = Integer.parseInt(request.getParameter("frageID"));
			String frage = request.getParameter("frage");
			String antworttext = request.getParameter("antworttext");
			//String bild = request.getParameter("bild");
			//Boolean flag = Boolean.valueOf(request.getParameter("flag"));
			//int flag = Integer.parseInt(request.getParameter("flag"));
			
			
			//frageID, matrikelnummer,frage, antworttext, stern in DB speichern oder updaten
			
			if (action.equals("erfahrungsbericht_post") ){
				
				System.out.println("post_erfahrungsbericht == action.");
				
					insertSql = "INSERT INTO erfahrungsbericht (frageID, matrikelnummer,frage, antworttext, stern) "
							+ "VALUES ('"	+ frageID +"','" 
							                + matrikelnummer +"','"
											+ request.getParameter("frage") + "', '" 
											+ request.getParameter("antworttext") + "', '"
											+ Integer.parseInt(request.getParameter("stern")) + "')";
					
				      
				    updateSql = "UPDATE erfahrungsbericht "
					     +"SET frageID = ?, matrikelnummer = ?, frage = ?, antworttext = ?, stern = ?  WHERE frageID = ? AND matrikelnummer = ? ";
					         
							               
			}
		     //Bilder in DB speichern
			 else if( action.equals("bild_post") ) {
		    	 
					System.out.println("post_bild == action.");
		    		
					insertSql = "INSERT INTO erfahrungsberichtBild (frageID, matrikelnummer, bild) "
								+ "VALUES ('"	+ frageID +"','"
												+ matrikelnummer +"','" 
												+ request.getParameter("bild") + "')";
						
			   }
			
			  // Wenn  Button mit ID== X1(Erfarungsbericht schlie�en) gedr�ckt wird, wird flag auf true gesetzt und
			 //Erfahrunsbericht wird nicht mehr aktiv
			
			 /* else if( action.equals("flag_post") ) {
		    	 
					System.out.println("post_flag == action.");
		    		
					insertSql = "INSERT INTO erfahrungsberichtAbschlu� (matrikelnummer, flag) "
								+ "VALUES ('"	+ matrikelnummer +"','" 
												+ flag + "')";
			   }*/
			
		    try{
		    	// Register JDBC driver
		        Class.forName("com.mysql.jdbc.Driver").newInstance();
		        // Open a connection
		        conn = DriverManager.getConnection(DB_URL,USER,PASS);
		        if( (!action.equals("bild_post")) && (!action.equals("flag_post")) ) {
		        	java.sql.PreparedStatement updateStatement = conn.prepareStatement(updateSql);
		        	updateStatement.setInt(1, frageID);
		        	updateStatement.setInt(2, matrikelnummer);
		        	updateStatement.setString(3, frage);
		        	updateStatement.setString(4, antworttext);
		        	updateStatement.setInt(5, Integer.parseInt(request.getParameter("stern")));
		        	updateStatement.setInt(6, frageID);
		        	updateStatement.setInt(7, matrikelnummer);
		       
		        	int rc = 0;
		        if(updateStatement != null){
		        	 rc = updateStatement.executeUpdate();
		        }
		        	if(rc == 0){
		        		
		        		java.sql.PreparedStatement insertStatement = conn.prepareStatement(insertSql);
		        		insertStatement.execute();
		        		insertStatement.close();
		        	}else{
		        		updateStatement.close();
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
		          System.out.println("Fehler e "+ e);
		     }
		     finally{
		    	 System.out.println("Done doPost");
				 try{
					
					conn.close();
				 }
				 catch(Exception ex){
					System.out.println( "Exception : " + ex.getMessage() );
				 }
		     }			
		}
	


}


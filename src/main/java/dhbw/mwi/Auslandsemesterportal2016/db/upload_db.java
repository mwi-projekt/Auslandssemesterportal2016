package dhbw.mwi.Auslandsemesterportal2016.db;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class upload_db
 */
@MultipartConfig(maxFileSize = 16177215) //file size up to 16MB
@WebServlet("/upload_db")
public class upload_db extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// JDBC driver name and database URL
    final String DB_URL="jdbc:mysql://193.196.7.215:3306/mwi";
	// Database account
    final String USER = "mwi";
    final String PASS = "mwi2014";
    Connection conn;
    Statement stmt;
    ResultSet rs;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload_db() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		//get values from the AJAX data field
		String name = request.getParameter("name");
		String comment = request.getParameter("comment");
		InputStream inputStream = null;
		Part filePart = request.getPart("file");
		String type = "";
		//obtains the upload file part in this multipart request
		if (filePart != null){
			//debug messages
			System.out.println(filePart.getName());
			System.out.println(filePart.getSize());
			System.out.println(filePart.getContentType());
			type = filePart.getContentType();
			//input stream of the file for upload
			inputStream = filePart.getInputStream();
		}
		else{
			System.out.println("no File");
		}
		
		try {
			// Register JDBC driver
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        // Open a connection with statement
	        conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			//prepare SQL statement
			String sql = "INSERT INTO prozess_files (name, comment, type, file) values (?,?,?,?)";
			java.sql.PreparedStatement psql = conn.prepareStatement(sql);
			psql.setString(1, name);
			psql.setString(2, comment);
			psql.setString(3, type);
			psql.setBlob(4, inputStream);	
			
			//sends the SQL statement to the database server
			int row = psql.executeUpdate();
			if (row > 0){
				response.setContentType("text/html; charset=UTF-8");
				out.println("Upload war erfolgreich !");
			}

		}
		catch (SQLException sqlex){
			System.out.println( "SQL Exception : " + sqlex.getMessage() );
		}
		catch(Exception ex){
			System.out.println( "Exception : " + ex.getMessage() );
		}
		finally {
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

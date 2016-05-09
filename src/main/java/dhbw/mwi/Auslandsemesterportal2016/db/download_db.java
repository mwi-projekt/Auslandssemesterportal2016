package dhbw.mwi.Auslandsemesterportal2016.db;

import java.io.IOException;
import java.io.OutputStream;
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


/**
 * Servlet implementation class download_db
 */
@MultipartConfig(maxFileSize = 16177215) //file size up to 16MB
@WebServlet("/download_db")
public class download_db extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// JDBC driver name and database URL
    final String DB_URL="jdbc:mysql://193.196.7.215:3306/mwi";
	// Database account
    final String USER = "mwi";
    final String PASS = "mwi2014";
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
    private static final int BUFFER_SIZE = 4096;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public download_db() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get upload id from parameters
        String id = request.getParameter("sid");
        try{
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Open a connection with statement
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //queries the database
            String sql = "SELECT id, name, comment, type, file FROM prozess_files WHERE id = ?";
            java.sql.PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                //gets file name, type and file blob data
                String fileName = result.getString("name");
                String fileType = result.getString("type");
                java.sql.Blob blob = result.getBlob("file");
                InputStream inputStream = blob.getBinaryStream();
                int fileLength = inputStream.available();
                
                System.out.println("fileLength = " + fileLength);
                
                //sets MIME type for the file
                if (fileType == null) {        
                	response.setContentType("application/octet-stream");
                }
                else {
                	response.setContentType(fileType);
                }
               
                // set content properties and header attributes for the response
                response.setContentLength(fileLength);
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", fileName);
                response.setHeader(headerKey, headerValue);
                
                //writes the file to the client
                OutputStream outStream = response.getOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outStream.close();             
            }
            else {
            	//fuckstatment (no file)
            	response.getWriter().print("File not found for the id: " + id);  
            }
        }
        catch (SQLException ex) {
        	ex.printStackTrace();
        	System.out.println("SQL Error: " + ex.getMessage());
        } 
        catch (IOException ex) {
        	ex.printStackTrace();
        	System.out.println("IO Error: " + ex.getMessage());
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        	System.out.println("Error: " + ex.getMessage());
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get upload id from parameters
        String id = request.getParameter("sid");
        try{
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // Open a connection with statement
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            //queries the database
            String sql = "SELECT id, name, comment, type, file FROM prozess_files WHERE id = ?";
            java.sql.PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            
            System.out.println(sql);
            System.out.println("? = "+id);
            
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                //gets file name, type and file blob data
                String fileName = result.getString("name");
                String fileType = result.getString("type");
                java.sql.Blob blob = result.getBlob("file");
                InputStream inputStream = blob.getBinaryStream();
                int fileLength = inputStream.available();
                
                System.out.println("fileLength = " + fileLength);
                System.out.println("fileType = " + fileType);
                
                
                //sets MIME type for the file
                if (fileType == null) {        
                	response.setContentType("application/octet-stream");
                }
                else {
                	response.setContentType(fileType);
                }
               
                // set content properties and header attributes for the response
                response.setContentLength(fileLength);
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", fileName);
                response.setHeader(headerKey, headerValue);
                
                //writes the file to the client
                OutputStream outStream = response.getOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outStream.close();             
            }
            else {
            	//fuckstatment (no file)
            	response.getWriter().print("File not found for the id: " + id);  
            }
        }
        catch (SQLException ex) {
        	ex.printStackTrace();
        	System.out.println("SQL Error: " + ex.getMessage());
        } 
        catch (IOException ex) {
        	ex.printStackTrace();
        	System.out.println("IO Error: " + ex.getMessage());
        } 
        catch (Exception ex) {
        	ex.printStackTrace();
        	System.out.println("Error: " + ex.getMessage());
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
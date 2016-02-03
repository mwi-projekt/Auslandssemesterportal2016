package src.DB;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;




/**
 * Servlet implementation class prozess_running
 */
@WebServlet("/prozess_running")
public class prozess_running extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public prozess_running() {
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
		String action = request.getParameter("action");
	    Timer timer = null;
	    RequestDispatcher requestDispatcher = request.getRequestDispatcher("prozess_db");
	    requestDispatcher.include(request, response);
	    	//
			//starting of the Scheduling thread
			if ( action.equals("start_srv") ){
				//
	        	// The Schedule (every timer starts a thread!!) 
				timer = new Timer();
		   	    timer.schedule(
		   	    		new  prozess_reports(),
		   	    		1,
		   	    		60000
		   	    );//starts after 1 msec after every min it is repeating 
			}
			//
			//stops the Scheduling thread 
			if ( action.equals("stop_srv") ){
				System.out.println("P-Server has recived STOP !");
			}	
		
	}


		
	
}

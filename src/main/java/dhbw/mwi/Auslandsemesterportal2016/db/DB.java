package dhbw.mwi.Auslandsemesterportal2016.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    static final String DB_URL="jdbc:mysql://127.0.0.1:3306/mwi2?autoReconnect=true";
    static final String USER = "root";
    static final String PASS = "qwe12345";
    static Connection conn;

    public static Connection getInstance() {
        if (conn == null) {
            try {
                // Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();

                // Open a connection with statement
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            catch(Exception ex){
                System.out.println( "Exception : " + ex.getMessage() );
            }
        }

        return conn;
    }

}

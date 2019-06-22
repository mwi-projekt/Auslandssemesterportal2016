package dhbw.mwi.Auslandsemesterportal2016.db;

import java.sql.Connection;
import java.sql.DriverManager;

import dhbw.mwi.Auslandsemesterportal2016.Config;

public class DB {

    static Connection conn;

    public static Connection getInstance() {
        if (conn == null) {
            try {
                // Register JDBC driver
                Class.forName(Config.DB_DRIVER).getConstructor().newInstance();

                // Open a connection with statement
                conn = DriverManager.getConnection(Config.DB_URL);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        return conn;
    }

}

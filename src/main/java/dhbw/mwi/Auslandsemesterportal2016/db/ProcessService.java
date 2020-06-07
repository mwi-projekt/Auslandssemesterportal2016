package dhbw.mwi.Auslandsemesterportal2016.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessService {

    public static String getProcessId(String matrnumber, String uni) {
        Connection connection = DB.getInstance();
        java.sql.Statement statement = null;
        ResultSet resultSet = null;
        String id = "leer";

        String sql = "SELECT processInstance FROM MapUserInstanz WHERE matrNumber ='" + matrnumber
                + "' AND uni ='" + uni + "'";

        // Test
        System.out.println("Matrikelnummer: " + matrnumber);
        System.out.println("Uni: " + uni);

        try {
            // Execute SQL query
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            // ID auslesen
            while (resultSet.next()) {
                id = resultSet.getString(1);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.print("ERROR - getProcessId -SQLException");
            e.printStackTrace();
        }

        return id;
    }

}

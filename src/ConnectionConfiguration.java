package src;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfiguration {
    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql:http://spectrolive.cran.univ-lorraine.fr", "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
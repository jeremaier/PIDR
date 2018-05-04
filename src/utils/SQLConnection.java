package src.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private String user;
    private String password;
    private static Connection connection = null;

    public SQLConnection(String user, String password) {
        this.user = user;
        this.password = password;
        this.createConnnection();
    }

    public static Connection getConnection() {
        return SQLConnection.connection;
    }

    private void createConnnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://spectrolive-sql.cran.univ-lorraine.fr:3306/spectrolive?user=" + this.user + "&password=" + this.password + "&useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
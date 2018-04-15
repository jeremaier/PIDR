package src.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfiguration {
    private String user;
    private String password;
    private static Connection connection = null;

    public ConnectionConfiguration(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public Connection createConnnection() {
        try {
            Thread.sleep(100);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection("jdbc:mysql://spectrolive-sql.cran.univ-lorraine.fr:3306/spectrolive?user=" + user + "&password=" + password + "&useSSL=false");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        try {
            if (this.connection != null && !this.connection.isClosed())
                this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
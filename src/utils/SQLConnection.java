package src.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private String user;
    private String password;
    private Connection connection = null;

    public SQLConnection(String user, String password) {
        this.user = user;
        this.password = password;
        this.createConnnection();
    }

    private void createConnnection() {
        try {
            //Thread.sleep(100);
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection("jdbc:mysql://spectrolive-sql.cran.univ-lorraine.fr:3306/spectrolive?user=" + this.user + "&password=" + this.password + "&useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
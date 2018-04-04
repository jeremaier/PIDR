package src.util;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfiguration extends Task<Connection> {
    private String user;
    private String password;
    private Connection connection = null;

    public ConnectionConfiguration(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    protected Connection call() {
        try {
            Thread.sleep(500);
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://spectrolive.cran.univ-lorraine.fr:3306/spectrolive", user, password);
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
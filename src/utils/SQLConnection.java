package src.utils;


import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private String user;
    private String password;
    private static Connection connection = null;
    private Button connectButton;

    public SQLConnection(String user, String password, Button button) {
        this.user = user;
        this.password = password;
        this.connectButton = button;
        this.createConnnection();
    }

    public static Connection getConnection() {
        return SQLConnection.connection;
    }

    private void createConnnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            SQLConnection.connection = DriverManager.getConnection("jdbc:mysql://spectrolive-sql.cran.univ-lorraine.fr:3306/spectrolive?user=" + this.user + "&password=" + this.password + "&useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de se connecter au serveur");
            this.connectButton.setDisable(false);
        }
    }

    public void closeConnection() throws SQLException {
        try {
            if (SQLConnection.connection != null && !SQLConnection.connection.isClosed())
                SQLConnection.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
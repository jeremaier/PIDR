package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.net.ftp.FTPClient;
import src.controller.LesionsController;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LesionsView {
    public LesionsView(Stage stage, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Lesions");
        viewLoader.setLocation(getClass().getResource("/ressource/Lesions.fxml"));
        viewLoader.setControllerFactory(iC -> new LesionsController());

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();

            try {
                FTPClient ftpConnection = fileManager.getConnection();
                ftpConnection.logout();
                ftpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        });

        assert rootLog != null;
        stage.setScene(new Scene(rootLog));
        stage.show();
    }
}
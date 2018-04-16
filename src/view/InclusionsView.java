package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.net.ftp.FTPClient;
import src.controller.InclusionsController;
import src.utils.FileManager;
import sun.net.ftp.FtpClient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class InclusionsView {
    public InclusionsView(Connection connection, FileManager fileManager) {
        Stage inclusionsWindow = new Stage();
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        inclusionsWindow.setTitle("Inclusions");
        viewLoader.setLocation(getClass().getResource("/ressource/Inclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new InclusionsController(connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            inclusionsWindow.close();
        }

        inclusionsWindow.setOnCloseRequest((WindowEvent event) -> {
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
        inclusionsWindow.setResizable(false);
        inclusionsWindow.setScene(new Scene(rootLog));
        inclusionsWindow.show();
    }
}
package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LoginView {
    public LoginView(Stage primaryStage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        primaryStage.setTitle("SpectroLive");
        viewLoader.setLocation(getClass().getResource("/ressource/Login.fxml"));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(rootLog, 600, 400));
        primaryStage.show();
    }
}
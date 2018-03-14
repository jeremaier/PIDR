package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LogView {
    public LogView(Stage primaryStage) {
        Parent rootLog = null;
        FXMLLoader logViewLoader = new FXMLLoader();

        primaryStage.setTitle("SpectroLive");
        logViewLoader.setLocation(getClass().getResource("../ressource/Login.fxml"));
        logViewLoader.setControllerFactory(iC -> new LogViewController());

        try {
            rootLog = logViewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        primaryStage.setScene(new Scene(rootLog, 600, 400));
        primaryStage.show();
    }
}


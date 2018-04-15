package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.TranscriptomieController;

import java.io.IOException;

public class TranscriptomieView {
    public TranscriptomieView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Analyse transcriptomique");
        viewLoader.setLocation(getClass().getResource("/ressource/Transcriptomique.fxml"));
        viewLoader.setControllerFactory(iC -> new TranscriptomieController());

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        stage.setScene(new Scene(rootLog));
        stage.show();
    }
}
package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.TranscriptomieController;
import src.table.TranscriptomicAnalysis;

import java.io.IOException;
import java.sql.Connection;

public class TranscriptomieView {
    public TranscriptomieView(Stage stage, Connection connection, TranscriptomicAnalysis transcriptomicAnalysis) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Analyse transcriptomique");
        viewLoader.setLocation(getClass().getResource("/ressource/Transcriptomique.fxml"));
        viewLoader.setControllerFactory(iC -> new TranscriptomieController(connection, transcriptomicAnalysis));

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
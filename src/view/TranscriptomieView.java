package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.TranscriptomieController;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class TranscriptomieView {
    public TranscriptomieView( Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage stage = new Stage();

        stage.setTitle("Analyse transcriptomique");
        viewLoader.setLocation(getClass().getResource("/ressource/Transcriptomique.fxml"));
        viewLoader.setControllerFactory(iC -> new TranscriptomieController(connection, fileManager, transcriptomicAnalysis, siteId));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        stage.setResizable(false);
        stage.setScene(new Scene(rootLog));
        stage.show();
    }
}
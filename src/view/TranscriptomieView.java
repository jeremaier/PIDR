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
    public TranscriptomieView(Stage stage, Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage transcriptomieStage = new Stage();

        transcriptomieStage.setTitle("Analyse transcriptomique");
        transcriptomieStage.setX(stage.getX());
        transcriptomieStage.setY(stage.getY());
        viewLoader.setLocation(getClass().getResource("/ressource/Trancriptomique.fxml"));
        viewLoader.setControllerFactory(iC -> new TranscriptomieController(connection, fileManager, transcriptomicAnalysis, siteId));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            transcriptomieStage.close();
        }

        transcriptomieStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        transcriptomieStage.setResizable(false);
        transcriptomieStage.setScene(new Scene(rootLog));
        transcriptomieStage.show();
    }
}
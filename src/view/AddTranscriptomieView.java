package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddTransciptomieController;
import src.table.CutaneousSite;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddTranscriptomieView {
    public AddTranscriptomieView(Stage stage, Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int id) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Gestion des analyses transcriptomiques");
        viewLoader.setLocation(getClass().getResource("/ressource/AddTranscriptomie.fxml"));
        viewLoader.setControllerFactory(iC -> new AddTransciptomieController(stage,  connection, fileManager, transcriptomicAnalysis  , id));

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

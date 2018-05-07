package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddTransciptomieController;
import src.controller.TranscriptomieController;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddTranscriptomieView {
    public AddTranscriptomieView(Stage parentStage, TranscriptomieController transcriptomieController, Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int id) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addTranscriptomieStage = new Stage();

        addTranscriptomieStage.setX(parentStage.getX());
        addTranscriptomieStage.setY(parentStage.getY());
        addTranscriptomieStage.setTitle("Gestion des analyses transcriptomiques");
        viewLoader.setLocation(getClass().getResource("/ressource/AddTranscriptomie.fxml"));
        viewLoader.setControllerFactory(iC -> new AddTransciptomieController(transcriptomieController,  connection, fileManager, transcriptomicAnalysis  , id));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addTranscriptomieStage.setResizable(false);
        addTranscriptomieStage.setScene(new Scene(rootLog));
        addTranscriptomieStage.show();
    }
}

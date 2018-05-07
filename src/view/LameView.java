package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.LameController;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class LameView {
    public LameView(Stage stage, Connection connection, FileManager fileManager, Lesion lesion, String numanapat) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage lameStage = new Stage();

        lameStage.setTitle("Lames histologiques");
        lameStage.setX(stage.getX());
        lameStage.setY(stage.getY());
        viewLoader.setLocation(getClass().getResource("/ressource/Lames.fxml"));
        viewLoader.setControllerFactory(iC -> new LameController(connection,lesion,fileManager, numanapat));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lameStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        lameStage.setResizable(false);
        lameStage.setScene(new Scene(rootLog));
        lameStage.show();
    }
}
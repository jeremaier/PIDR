package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.LesionsController;
import src.table.Inclusion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class LesionsView {
    public LesionsView(Connection connection, FileManager fileManager, Inclusion inclusion) {
        Parent rootLog = null;
        Stage LesionsStage = new Stage();
        FXMLLoader viewLoader = new FXMLLoader();

        LesionsStage.setTitle("Lesions");
        viewLoader.setLocation(getClass().getResource("/ressource/Lesions.fxml"));
        viewLoader.setControllerFactory(iC -> new LesionsController(connection, fileManager, inclusion));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LesionsStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        LesionsStage.setScene(new Scene(rootLog));
        LesionsStage.show();
    }
}
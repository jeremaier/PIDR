package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.InclusionsController;

import java.io.IOException;
import java.sql.Connection;

public class InclusionsView {
    public InclusionsView(Stage stage, Connection connection) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Inclusions");
        viewLoader.setLocation(getClass().getResource("../../ressource/Inclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new InclusionsController(connection));

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
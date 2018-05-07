package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.InclusionsController;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class InclusionsView {
    public InclusionsView(Stage stage, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        Stage inclusionStage = new Stage();
        FXMLLoader viewLoader = new FXMLLoader();

        if (stage == null) {
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            inclusionStage.setX((primScreenBounds.getWidth() - 800) / 2 - 120);
            inclusionStage.setY((primScreenBounds.getHeight() - 600) / 2 - 80);
        } else {
            inclusionStage.setX(stage.getX());
            inclusionStage.setY(stage.getY());
        }

        inclusionStage.setTitle("Inclusions");
        viewLoader.setLocation(getClass().getResource("/ressource/Inclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new InclusionsController(connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            inclusionStage.close();
        }

        inclusionStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        inclusionStage.setResizable(false);
        inclusionStage.setScene(new Scene(rootLog));
        inclusionStage.show();
    }
}
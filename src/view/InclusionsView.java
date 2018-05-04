package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.InclusionsController;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class InclusionsView {
    public InclusionsView(Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        Stage inclusionsStage = new Stage();
        FXMLLoader viewLoader = new FXMLLoader();

        inclusionsStage.setTitle("Inclusions");
        inclusionsStage.setX(inclusionsStage.getX() - 25);
        viewLoader.setLocation(getClass().getResource("/ressource/Inclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new InclusionsController(connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            inclusionsStage.close();
        }

        inclusionsStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        inclusionsStage.setResizable(false);
        inclusionsStage.setScene(new Scene(rootLog));
        inclusionsStage.show();
    }
}
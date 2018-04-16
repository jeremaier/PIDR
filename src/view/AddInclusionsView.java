package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddInclusionController;
import src.table.Inclusion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddInclusionsView {
    public AddInclusionsView(Stage stage, Inclusion inclusion, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Ajout d'une inclusion");
        viewLoader.setLocation(getClass().getResource("/ressource/AddInclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new AddInclusionController(inclusion, connection, fileManager));

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
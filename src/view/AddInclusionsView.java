package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddInclusionController;

import java.io.IOException;

public class AddInclusionsView {
    public AddInclusionsView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Ajout d'une inclusion");
        viewLoader.setLocation(getClass().getResource("../../ressource/AddInclusions.fxml"));
        viewLoader.setControllerFactory(iC -> new AddInclusionController());

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
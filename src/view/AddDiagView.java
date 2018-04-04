package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddLameController;

import java.io.IOException;

public class AddDiagView {
    public AddDiagView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Ajout d'un diagnostic");
        viewLoader.setLocation(getClass().getResource("../ressource/AddDiag.fxml"));
        viewLoader.setControllerFactory(iC -> new AddDiagController());

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
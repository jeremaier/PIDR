package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddLameController;

import java.io.IOException;

public class AddLameView {
    public AddLameView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Ajout d'une lame");
        viewLoader.setLocation(getClass().getResource("/ressource/AddLame.fxml"));
        viewLoader.setControllerFactory(iC -> new AddLameController());

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        stage.setResizable(false);
        stage.setScene(new Scene(rootLog));
        stage.show();
    }
}
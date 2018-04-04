package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddLameController;

import java.io.IOException;

public class SiteView {
    public SiteView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Vision d'un site");
        viewLoader.setLocation(getClass().getResource("../ressource/Site.fxml"));
        viewLoader.setControllerFactory(iC -> new SiteController());

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
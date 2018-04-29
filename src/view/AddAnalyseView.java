package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddAnalyseController;

import java.io.IOException;

public class AddAnalyseView {
    public AddAnalyseView(Stage stage) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Ajout d'une analyse transcriptomique");
        viewLoader.setLocation(getClass().getResource("/ressource/AddAnalyse.fxml"));
        viewLoader.setControllerFactory(iC -> new AddAnalyseController());

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
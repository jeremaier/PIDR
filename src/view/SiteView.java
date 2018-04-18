package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.SiteController;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class SiteView {
    public SiteView(Stage stage, CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        stage.setTitle("Vision d'un site");
        viewLoader.setLocation(getClass().getResource("/ressource/Site.fxml"));
        viewLoader.setControllerFactory(iC -> new SiteController( connection, lesion));

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
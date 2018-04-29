package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.SiteController;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class SiteView {
    public SiteView(Lesion lesion, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage siteStage = new Stage();

        siteStage.setTitle("Vision d'un site");
        viewLoader.setLocation(getClass().getResource("/ressource/Site.fxml"));
        viewLoader.setControllerFactory(iC -> new SiteController(connection, lesion));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        siteStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        siteStage.setResizable(false);
        siteStage.setScene(new Scene(rootLog));
        siteStage.show();
    }
}
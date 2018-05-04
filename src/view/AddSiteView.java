package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddSiteController;
import src.controller.SiteController;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddSiteView {
    public AddSiteView(SiteController siteController, CutaneousSite site, Connection connection, FileManager  fileManager, Lesion lesion) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage stage=new Stage();

        stage.setTitle("Ajout d'un site cutané");
        viewLoader.setLocation(getClass().getResource("/ressource/AddSite.fxml"));
        viewLoader.setControllerFactory(iC -> new AddSiteController(siteController, site, connection, fileManager, lesion));

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
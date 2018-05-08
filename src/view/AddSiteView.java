package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddSiteController;
import src.controller.SiteController;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddSiteView {
    public AddSiteView(Stage parentStage, SiteController siteController, CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addSiteStage = new Stage();

        addSiteStage.setX(parentStage.getX() + parentStage.getWidth() - 265);
        addSiteStage.setY(parentStage.getY());
        addSiteStage.setTitle(site == null ? "Ajout d'un site cutané" : "Modification d'un site cutané");
        viewLoader.setLocation(getClass().getResource("/ressource/AddSite.fxml"));
        viewLoader.setControllerFactory(iC -> new AddSiteController(siteController, site, connection, fileManager, lesion));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addSiteStage.setResizable(false);
        addSiteStage.setScene(new Scene(rootLog));
        addSiteStage.show();
    }
}
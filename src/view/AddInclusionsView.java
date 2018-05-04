package src.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddInclusionController;
import src.controller.InclusionsController;
import src.daoImpl.InclusionDaoImpl;
import src.table.Inclusion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddInclusionsView {
    public AddInclusionsView(Stage parentStage, InclusionsController inclusionsController, ObservableList<Inclusion> inclusionsList, Inclusion inclusion, InclusionDaoImpl inclusionDaoImpl, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addInclusionStage = new Stage();

        addInclusionStage.setX(parentStage.getX() + parentStage.getWidth());
        addInclusionStage.setY(parentStage.getY());
        addInclusionStage.setTitle(inclusion == null ? "Ajout d'une inclusion" : "Modification d'une inclusion");
        viewLoader.setLocation(getClass().getResource("/ressource/AddInclusion.fxml"));
        viewLoader.setControllerFactory(iC -> new AddInclusionController(inclusionsController, inclusionsList, inclusion, inclusionDaoImpl, connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addInclusionStage.setResizable(false);
        addInclusionStage.setScene(new Scene(rootLog));
        addInclusionStage.show();
    }
}
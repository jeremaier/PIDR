package src.view;

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
    public AddInclusionsView(/*Stage stage,*/ InclusionsController inclusionsController, Inclusion inclusion, InclusionDaoImpl inclusionDaoImpl, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addInclusionStage = new Stage();

        addInclusionStage.setTitle(inclusion == null ? "Ajout d'une inclusion" : "Modification d'une inclusion");
        //addInclusionStage.setX(stage.getX() + stage.getWidth() / 2);
        //addInclusionStage.setY(stage.getY() + stage.getHeight() / 2);

        viewLoader.setLocation(getClass().getResource("/ressource/AddInclusion.fxml"));
        viewLoader.setControllerFactory(iC -> new AddInclusionController(inclusionsController, inclusion, inclusionDaoImpl, connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addInclusionStage.setScene(new Scene(rootLog));
        addInclusionStage.show();
    }
}
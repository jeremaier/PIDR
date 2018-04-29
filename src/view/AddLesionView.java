package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddLesionController;
import src.controller.LesionsController;
import src.daoImpl.LesionDaoImpl;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;

public class AddLesionView {
    public AddLesionView(LesionsController lesionsController, Lesion lesion, int inclusionId, LesionDaoImpl lesionDaoImpl, FileManager fileManager) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addLesionStage = new Stage();

        addLesionStage.setTitle(lesion == null ? "Ajout d'une lésion cutanée" : "Modification d'une lésion cutanée");
        viewLoader.setLocation(getClass().getResource("/ressource/AddLesion.fxml"));
        viewLoader.setControllerFactory(iC -> new AddLesionController(lesionsController, lesion, inclusionId, lesionDaoImpl, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addLesionStage.setResizable(false);
        addLesionStage.setScene(new Scene(rootLog));
        addLesionStage.show();
    }
}
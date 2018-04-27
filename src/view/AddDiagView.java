package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddDiagController;
import src.controller.AddLesionController;
import src.table.Lesion;

import java.io.IOException;

public class AddDiagView {
    public AddDiagView(AddLesionController addLesionController, Lesion lesion) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage diagStage = new Stage();

        diagStage.setTitle("Ajout d'un diagnostic");
        viewLoader.setLocation(getClass().getResource("/ressource/AddDiag.fxml"));
        viewLoader.setControllerFactory(iC -> new AddDiagController(addLesionController, lesion));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        diagStage.setScene(new Scene(rootLog));
        diagStage.show();
    }
}
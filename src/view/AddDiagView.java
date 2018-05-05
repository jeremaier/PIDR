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
    public AddDiagView(Stage parentStage, AddLesionController addLesionController, Lesion lesion) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage diagStage = new Stage();

        diagStage.setX(parentStage.getX() + parentStage.getWidth());
        diagStage.setY(parentStage.getY());
        diagStage.setTitle(lesion.getAutreDiag().equals("") ? "Ajout d'un diagnostic" : "Modification d'un diagnostic");
        viewLoader.setLocation(getClass().getResource("/ressource/AddDiag.fxml"));
        viewLoader.setControllerFactory(iC -> new AddDiagController(addLesionController, lesion));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        diagStage.setResizable(false);
        diagStage.setScene(new Scene(rootLog));
        diagStage.show();
    }
}
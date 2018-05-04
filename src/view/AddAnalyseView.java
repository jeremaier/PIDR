package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddAnalyseController;

import java.io.IOException;

public class AddAnalyseView {
    public AddAnalyseView(Stage parentStage) {
        Parent rootLog = null;
        Stage addAnalyseStage = new Stage();
        FXMLLoader viewLoader = new FXMLLoader();

        addAnalyseStage.setX(parentStage.getX() + parentStage.getWidth());
        addAnalyseStage.setY(parentStage.getY());
        addAnalyseStage.setTitle("Ajout d'une analyse transcriptomique");
        viewLoader.setLocation(getClass().getResource("/ressource/AddAnalyse.fxml"));
        viewLoader.setControllerFactory(iC -> new AddAnalyseController());

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addAnalyseStage.setResizable(false);
        addAnalyseStage.setScene(new Scene(rootLog));
        addAnalyseStage.show();
    }
}
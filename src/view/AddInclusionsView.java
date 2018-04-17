package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.AddInclusionController;
import src.controller.InclusionsController;
import src.table.Inclusion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddInclusionsView {
    public AddInclusionsView(Stage stage, InclusionsController inclusionsController, Inclusion inclusion, Connection connection, FileManager fileManager) {
        Parent rootLog = null;
        Stage addInclusionStage = new Stage();
        FXMLLoader viewLoader = new FXMLLoader();

        addInclusionStage.setTitle("Ajout d'une inclusion");
        addInclusionStage.setX(stage.getX() + stage.getWidth() / 2);
        addInclusionStage.setY(stage.getY() + stage.getHeight() / 2);
        viewLoader.setLocation(getClass().getResource("/ressource/AddInclusion.fxml"));
        viewLoader.setControllerFactory(iC -> new AddInclusionController(inclusionsController, inclusion, connection, fileManager));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addInclusionStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        addInclusionStage.setScene(new Scene(rootLog));
        addInclusionStage.show();
    }
}
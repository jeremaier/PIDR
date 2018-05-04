package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddLameController;
import src.table.HistologicLamella;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class AddLameView {
    public AddLameView(Stage parentStage, HistologicLamella lame, Connection connection, FileManager fileManager, Lesion lesion, int numAnapat) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage addLameStage = new Stage();

        addLameStage.setX(parentStage.getX() + parentStage.getWidth());
        addLameStage.setY(parentStage.getY());
        addLameStage.setTitle(lame == null ? "Ajout d'une lame" : "Modification d'une lame");
        addLameStage.setTitle("Ajout d'une lame");
        viewLoader.setLocation(getClass().getResource("/ressource/AddLame.fxml"));
        viewLoader.setControllerFactory(iC -> new AddLameController(connection, fileManager, lesion, lame,numAnapat));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        addLameStage.setResizable(false);
        addLameStage.setScene(new Scene(rootLog));
        addLameStage.show();
    }
}
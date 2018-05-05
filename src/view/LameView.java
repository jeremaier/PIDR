package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.LameController;
import src.table.Lesion;
import src.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;

public class LameView {
    public LameView(Connection connection, FileManager fileManager, Lesion lesion, int numanapat) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage stage = new Stage();

        stage.setTitle("Lame histologique");
        viewLoader.setLocation(getClass().getResource("/ressource/Lames.fxml"));
        viewLoader.setControllerFactory(iC -> new LameController(connection,lesion,fileManager, numanapat));

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
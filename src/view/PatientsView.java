package src.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.controller.PatientsController;

import java.io.IOException;
import java.sql.Connection;

public class PatientsView {
    public PatientsView(Connection connection, int inclusionId) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();

        Stage patientStage = new Stage();
        patientStage.setTitle("Patients");
        patientStage.setResizable(false);

        viewLoader.setLocation(getClass().getResource("../../ressource/Patients.fxml"));
        viewLoader.setControllerFactory(iC -> new PatientsController(connection, inclusionId));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        patientStage.setOnCloseRequest((WindowEvent event) -> Platform.exit());

        assert rootLog != null;
        patientStage.setScene(new Scene(rootLog));
        patientStage.show();
    }
}
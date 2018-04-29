package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddInclusionController;
import src.controller.PatientsController;

import java.io.IOException;
import java.sql.Connection;

public class PatientsView {
    public PatientsView(Connection connection, AddInclusionController addInclusionController) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage patientStage = new Stage();

        patientStage.setTitle("Patients");
        patientStage.setResizable(false);

        viewLoader.setLocation(getClass().getResource("/ressource/Patients.fxml"));
        viewLoader.setControllerFactory(iC -> new PatientsController(connection, addInclusionController));

        try {
            rootLog = viewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert rootLog != null;
        patientStage.setResizable(false);
        patientStage.setScene(new Scene(rootLog));
        patientStage.show();
    }
}
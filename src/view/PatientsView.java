package src.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.controller.AddInclusionController;
import src.controller.PatientsController;
import src.daoImpl.PatientDaoImpl;

import java.io.IOException;

public class PatientsView {
    public PatientsView(Stage parentStage, AddInclusionController addInclusionController, PatientDaoImpl patientDaoImpl) {
        Parent rootLog = null;
        FXMLLoader viewLoader = new FXMLLoader();
        Stage patientStage = new Stage();

        patientStage.setTitle("Patients");
        patientStage.setX(parentStage.getX() - 650);
        patientStage.setY(parentStage.getY());
        viewLoader.setLocation(getClass().getResource("/ressource/Patients.fxml"));
        viewLoader.setControllerFactory(iC -> new PatientsController(addInclusionController, patientDaoImpl));

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
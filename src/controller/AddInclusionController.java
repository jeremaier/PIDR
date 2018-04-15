package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.table.Inclusion;

import java.net.URL;
import java.util.ResourceBundle;

public class AddInclusionController implements Initializable {
    @FXML
    TextField inclusionIDField;

    @FXML
    TextField idAnapathField;

    @FXML
    DatePicker inclusionDatePicker;

    @FXML
    Button addPatientButton;

    @FXML
    Button reference1FileButton;

    @FXML
    Button reference2FileButton;

    @FXML
    Label patientLabel;

    @FXML
    Label reference1FileLabel;

    @FXML
    Label reference2FileLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /*@FXML
    private void addInclusionAction(ActionEvent actionEvent) {
        int id, int idPatient, File reference1, File reference2, Date dateInclusion, int numAnaPath
        Inclusion inclusion = new Inclusion(Integer.getInteger(this.idInclusionField.getText()),
                Integer.getInteger(this..getText()),
        this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        inclusionDaolmpl.insert(inclusion);
        this.populatePatient(inclusion);
    }

    @FXML
    private void updateInclusionInformations(ActionEvent actionEvent) {
        Inclusion patient = new Inclusion(Integer.getInteger(this.idInclusionField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        inclusionDaolmpl.update(patient, patient.getId());
    }*/

}
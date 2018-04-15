package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.table.Inclusion;
import src.utils.FileManager;

import java.net.URL;
import java.sql.Connection;
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

    private Inclusion inclusion;
    private Connection connection;

    public AddInclusionController(Inclusion inclusion, Connection connection) {
        this.inclusion = inclusion;
        this.connection = connection;

        if(this.inclusion != null)
            this.setInclusionInformations();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setInclusionInformations() {
        /*InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(connection);
        this.inclusion = inclusionDaoImpl.selectById(inclusion.getId());*/
        this.inclusionIDField.setText(Integer.toString(this.inclusion.getId()));
        this.patientLabel.setText(this.inclusion.getInitialesPatient());
        this.reference1FileLabel.setText(FileManager.getFileName(this.inclusion.getReference1()));
        this.reference2FileLabel.setText(FileManager.getFileName(this.inclusion.getReference2()));
        this.inclusionDatePicker.setValue(this.inclusion.getDateInclusion().toLocalDate());
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
    }

    private void populateInclusion(Inclusion patient) {
        this.inclusionsList.add(patient);
        this.populateInclusions(this.inclusionsList);
    }*/

}
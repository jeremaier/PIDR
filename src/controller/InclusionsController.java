package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.table.Inclusion;
import src.utils.Diag;

import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.util.ResourceBundle;

public class InclusionsController implements Initializable {
    @FXML
    TextField idInclusionField;
    @FXML
    TextField idAnapathField;
    @FXML
    TextField initialesField;
    @FXML
    TextField searchDocField;
    @FXML
    DatePicker inclusionDatePicker;
    @FXML
    ChoiceBox diagnosticChoiceBox;
    @FXML
    Button searchButton;
    @FXML
    Button displayAllButton;
    @FXML
    Button refDownloadButton;
    @FXML
    Button addButton;
    @FXML
    Button removeButton;
    @FXML
    Button editButton;
    @FXML
    Button searchDocButton;
    @FXML
    Button downloadDocButton;
    @FXML
    Button removeDocButton;
    @FXML
    Button importProcButton;
    @FXML
    Button importResultButton;
    @FXML
    TableView patientsTable;
    @FXML
    TableColumn<Inclusion, Integer> inclID;
    @FXML
    TableColumn<Inclusion, Date>  inclDate;
    @FXML
    TableColumn<Inclusion, Integer> inclAnapath;
    @FXML
    TableColumn<Inclusion, String> inclInitials;
    @FXML
    TableColumn<Inclusion, Diag> inclDiagnostic;

    Connection connection;
    private Stage inclusionsStage;
    private InclusionDaoImpl inclusionDaolmpl;
    private ObservableList<Inclusion> inclusionsList;

    public InclusionsController(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList("Baso-cellulaire", "Spino-cellulaire", "Kératose-actinique", "Pas de malignité", "Autre", "Fichier"));
    }

    /*private void populatePatients(ObservableList<Inclusion> patients) {
        patientsTable.setItems(patients);
    }

    private void populatePatient(Inclusion patient) {
        this.inclusionsList.add(patient);
        this.populatePatients(this.inclusionsList);
    }

    @FXML
    private void addPatientAction(ActionEvent actionEvent) {
        Inclusion patient = new Inclusion(Integer.getInteger(this.idInclusionField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        inclusionDaolmpl.insert(patient);
        this.populatePatient(patient);
    }

    @FXML
    private void searchAction(ActionEvent actionEvent) {
        inclusionsList = inclusionDaolmpl.selectByFilters(Integer.getInteger(this.idInclusionField.getText()), this.initialesField.getText());
        this.populatePatients(inclusionsList);
    }

    @FXML
    private void searchAllAction(ActionEvent actionEvent) {
        inclusionsList = inclusionDaolmpl.selectAll();
        this.populatePatients(inclusionsList);
    }

    @FXML
    private void removeAction(ActionEvent actionEvent) {
        inclusionDaolmpl.delete(Integer.getInteger(idInclusionField.getText()));
        inclusionsList.remove(selectedPatient);
        this.populatePatients(this.inclusionsList);
    }

    @FXML
    private void updatePatientInformations(ActionEvent actionEvent) {
        Inclusion patient = new Inclusion(Integer.getInteger(this.idInclusionField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        inclusionDaolmpl.update(patient, patient.getId());
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        this.inclusionsStage = (Stage) cancelButton.getScene().getWindow();
        this.inclusionsStage.close();
    }

    @FXML
    private void choosePatientAction(ActionEvent actionEvent) {
        InclusionDaoImpl inclusionDao = new InclusionDaoImpl(connection);
        inclusionDao.update(Integer.getInteger(this.idInclusionField.getText()), inclusionId);
        this.inclusionsStage = (Stage) chooseButton.getScene().getWindow();
        this.inclusionsStage.close();
    }*/
}
package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.PatientDaolmpl;
import src.table.Patient;
import src.utils.Genre;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class PatientsController implements Initializable {
    @FXML
    TextField idField;
    @FXML
    TextField initialesField;
    @FXML
    TextField dateField;
    @FXML
    TextField idSearchField;
    @FXML
    TextField initialesSearchField;
    @FXML
    TextField dateSearchField;
    @FXML
    ComboBox genreComboBox;
    @FXML
    Button addPatientButton;
    @FXML
    Button modifyButton;
    @FXML
    Button chooseButton;
    @FXML
    Button removeButton;
    @FXML
    Button cancelButton;
    @FXML
    Button searchButton;
    @FXML
    Button searchAllButton;
    @FXML
    TableView patientsTable;
    @FXML
    TableColumn<Patient, Integer> patIdCol;
    @FXML
    TableColumn<Patient, String> patInitialesCol;
    @FXML
    TableColumn<Patient, String> patGenreCol;
    @FXML
    TableColumn<Patient, Integer> patDateCol;

    private ObservableList<Genre> comboBoxValeurs = FXCollections.observableArrayList(Genre.M, Genre.F);
    private Stage patientsStage;
    private Connection connection;
    private PatientDaolmpl patientDaolmpl;
    private ObservableList<Patient> patientsList;
    private Patient selectedPatient;
    private int inclusionId;

    public PatientsController(Connection connection, int inclusionId) {
        this.connection = connection;
        this.inclusionId = inclusionId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.patIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.patInitialesCol.setCellValueFactory(cellData -> cellData.getValue().initialesProperty());
        this.patGenreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        this.patDateCol.setCellValueFactory(cellData -> cellData.getValue().dateNaissanceProperty().asObject());
        this.genreComboBox.getItems().addAll(comboBoxValeurs);

        this.patientDaolmpl = new PatientDaolmpl(connection);
        ObservableList<Patient> patients = patientDaolmpl.selectAll();
        this.populatePatients(patients);

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if(initialesField.getText().length() >= 2)
                    initialesField.setText(initialesField.getText().substring(0, 2));

            if(!patientsTable.getItems().contains(initialesField) && initialesField != null) {
                removeButton.setDisable(false);
            } else {
                modifyButton.setDisable(true);
            }
        });

        this.idField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.dateField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if(initialesField.getText().length() >= 4)
                    initialesField.setText(initialesField.getText().substring(0, 4));
        });

        this.dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.patientsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedPatient = (Patient) patientsTable.getSelectionModel().getSelectedItem();

            if(selectedPatient != null) {
                idField.setText(Integer.toString(selectedPatient.getId()));
                initialesField.setText(selectedPatient.getInitiales());
                dateField.setText(Integer.toString(selectedPatient.getAnneeNaissance()));
                genreComboBox.setValue(selectedPatient.getGenre());
                removeButton.setDisable(false);
                chooseButton.setDisable(false);
            } else {
                idField.setText("ID");
                initialesField.setText("Initiales");
                dateField.setText("Année de naissance");
                genreComboBox.setPromptText("Genre");
                removeButton.setDisable(true);
                chooseButton.setDisable(true);
            }
        });
    }

    private void populatePatients(ObservableList<Patient> patients) {
        if(!patients.isEmpty())
            patientsTable.setItems(patients);
        else patientsTable.refresh();
    }

    private void populatePatient(Patient patient) {
        this.patientsList.add(patient);
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void addPatientAction(ActionEvent actionEvent) {
        Patient patient = new Patient(Integer.getInteger(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        patientDaolmpl.insert(patient);
        this.populatePatient(patient);
    }

    @FXML
    private void searchAction(ActionEvent actionEvent) {
        patientsList = patientDaolmpl.selectByFilters(Integer.getInteger(this.idField.getText()), this.initialesField.getText());
        this.populatePatients(patientsList);
    }

    @FXML
    private void searchAllAction(ActionEvent actionEvent) {
        patientsList = patientDaolmpl.selectAll();
        this.populatePatients(patientsList);
    }

    @FXML
    private void removeAction(ActionEvent actionEvent) {
        patientDaolmpl.delete(Integer.getInteger(idField.getText()));
        patientsList.remove(selectedPatient);
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void updatePatientInformations(ActionEvent actionEvent) {
        Patient patient = new Patient(Integer.getInteger(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        patientDaolmpl.update(patient, patient.getId());
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        this.patientsStage = (Stage) cancelButton.getScene().getWindow();
        this.patientsStage.close();
    }

    @FXML
    private void choosePatientAction(ActionEvent actionEvent) {
        InclusionDaoImpl inclusionDao = new InclusionDaoImpl(connection);
        inclusionDao.update(Integer.getInteger(this.idField.getText()), inclusionId);
        this.patientsStage = (Stage) chooseButton.getScene().getWindow();
        this.patientsStage.close();
    }
}
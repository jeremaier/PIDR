package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    ComboBox<Genre> genreComboBox;
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
    TableView<Patient> patientsTable;
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
    private AddInclusionController addInclusionController;

    public PatientsController(Connection connection, AddInclusionController addInclusionController) {
        this.connection = connection;
        this.addInclusionController = addInclusionController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.patIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.patInitialesCol.setCellValueFactory(cellData -> cellData.getValue().initialesProperty());
        this.patGenreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        this.patDateCol.setCellValueFactory(cellData -> cellData.getValue().dateNaissanceProperty().asObject());
        this.genreComboBox.getItems().addAll(this.comboBoxValeurs);

        this.patientDaolmpl = new PatientDaolmpl(this.connection);
        ObservableList<Patient> patients = this.patientDaolmpl.selectAll();
        this.populatePatients(patients);

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if (this.initialesField.getText().length() >= 4)
                    this.initialesField.setText(this.initialesField.getText().substring(0, 4));

            /**TODO VERIFIER SI ID EXISTE DEJA
             * if(!this.patIdCol.getCellObservableValue(initialesField) && this.initialesField != null)
             *                 removeButton.setDisable(false);
             *             else modifyButton.setDisable(true);
             */
        });

        this.idField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                this.idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.dateField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if (this.initialesField.getText().length() >= 4)
                    this.initialesField.setText(this.initialesField.getText().substring(0, 4));
        });

        this.dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                this.idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.patientsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            this.selectedPatient = this.patientsTable.getSelectionModel().getSelectedItem();

            if (this.selectedPatient != null) {
                this.idField.setText(Integer.toString(this.selectedPatient.getId()));
                this.initialesField.setText(this.selectedPatient.getInitiales());
                this.dateField.setText(Integer.toString(this.selectedPatient.getAnneeNaissance()));
                this.genreComboBox.setValue(Genre.valueOf(this.selectedPatient.getGenre()));
                this.removeButton.setDisable(false);
                this.chooseButton.setDisable(false);
            } else {
                this.idField.setPromptText("ID");
                this.initialesField.setPromptText("Initiales");
                this.dateField.setPromptText("Année de naissance");
                this.genreComboBox.setPromptText("Genre");
                this.removeButton.setDisable(true);
                this.chooseButton.setDisable(true);
            }
        });
    }

    private void populatePatients(ObservableList<Patient> patients) {
        if(!patients.isEmpty())
            this.patientsTable.setItems(patients);
        else this.patientsTable.refresh();
    }

    private void populatePatient(Patient patient) {
        this.patientsList.add(patient);
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void addPatientAction() {
        Patient patient = new Patient(Integer.getInteger(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        this.patientDaolmpl.insert(patient);
        this.populatePatient(patient);
    }

    @FXML
    private void searchAction() {
        this.patientsList = this.patientDaolmpl.selectByFilters(Integer.getInteger(this.idField.getText()), this.initialesField.getText());
        this.populatePatients(patientsList);
    }

    @FXML
    private void searchAllAction() {
        this.patientsList = this.patientDaolmpl.selectAll();
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void removeAction() {
        this.patientDaolmpl.delete(Integer.getInteger(this.idField.getText()));
        this.patientsList.remove(this.selectedPatient);
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void updatePatientInformations() {
        Patient patient = new Patient(Integer.getInteger(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.getInteger(this.dateField.getText()));
        this.patientDaolmpl.update(patient, patient.getId());
    }

    @FXML
    private void cancelAction() {
        this.patientsStage = (Stage) this.cancelButton.getScene().getWindow();
        this.patientsStage.close();
    }

    @FXML
    private void choosePatientAction() {
        this.addInclusionController.setPatientInformations(Integer.getInteger(this.idField.getText()), this.initialesField.getText());
        this.patientsStage = (Stage) this.chooseButton.getScene().getWindow();
        this.patientsStage.close();
    }
}
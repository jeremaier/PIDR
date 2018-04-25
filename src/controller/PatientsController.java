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
import java.util.ArrayList;
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
    private ArrayList<String> idPatients = new ArrayList<>();

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
        this.patientsList = this.patientDaolmpl.selectAll();
        this.populatePatients(patientsList);
        this.populateIdPatients();

        this.idField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            /*TODO remet pas en ajouter quand on a selectonnie et changer l'id*/
            if (!oldValue.equals(newValue)) {
                if (this.isNotAlreadyExistant(idField.getText())) {
                    this.modifyButton.setDisable(false);
                    this.addPatientButton.setDisable(true);
                } else if (!this.idField.getText().equals("")) {
                    this.modifyButton.setDisable(true);
                    this.addPatientButton.setDisable(false);
                } else {
                    this.modifyButton.setDisable(true);
                    this.addPatientButton.setDisable(true);
                }
            }
        });

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if (this.initialesField.getText().length() >= 4)
                    this.initialesField.setText(this.initialesField.getText().substring(0, 4));
        });

        this.idField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                this.idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.dateField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue())
                if (this.dateField.getText().length() >= 4)
                    this.dateField.setText(this.dateField.getText().substring(0, 4));
        });

        this.dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                this.dateField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.patientsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            this.selectedPatient = this.patientsTable.getSelectionModel().getSelectedItem();

            if (this.selectedPatient != null) {
                this.idField.setText(Integer.toString(this.selectedPatient.getId()));
                this.initialesField.setText(this.selectedPatient.getInitiales());
                this.dateField.setText(Integer.toString(this.selectedPatient.getAnneeNaissance()));
                this.genreComboBox.setValue(this.selectedPatient.getGenre().equals("Homme") ? Genre.M : Genre.F);
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

    private void populateIdPatients() {
        for (Patient aPatientsList : this.patientsList)
            this.idPatients.add(Integer.toString(aPatientsList.getId()));
    }

    private boolean isNotAlreadyExistant(String id) {
        return this.idPatients.contains(id);
    }

    private void populatePatients(ObservableList<Patient> patients) {
        if(!patients.isEmpty())
            this.patientsTable.setItems(patients);
        else this.patientsTable.refresh();
    }

    private void populatePatient(Patient patient) {
        this.patientsList.add(patient);
        this.idPatients.add(Integer.toString(patient.getId()));
        this.populatePatients(this.patientsList);
    }

    private void cleanFields() {
        this.idField.setText("");
        this.initialesField.setText("");
        this.genreComboBox.setValue(null);
        this.dateField.setText("");
        this.idField.requestFocus();
    }

    @FXML
    private void addPatientAction() {
        Patient patient = new Patient(Integer.parseInt(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.parseInt(this.dateField.getText()));
        this.patientDaolmpl.insert(patient);
        this.idPatients.add(this.idField.getText());
        this.cleanFields();
        this.populatePatient(patient);
    }

    @FXML
    private void searchAction() {
        this.patientsList = this.patientDaolmpl.selectByFilters(Integer.parseInt(this.idField.getText()), this.initialesField.getText());
        this.populatePatients(patientsList);
    }

    @FXML
    private void searchAllAction() {
        this.patientsList = this.patientDaolmpl.selectAll();
        this.populatePatients(this.patientsList);
    }

    @FXML
    private void removeAction() {
        this.patientDaolmpl.delete(Integer.parseInt(this.idField.getText()));
        this.patientsList.remove(this.selectedPatient);
        System.out.println(this.idPatients.get(0));
        System.out.println(this.idField.getText());
        /*TODO remove pas l'id*/
        boolean zob = this.idPatients.remove(this.idField.getText());
        System.out.println(zob);
        this.cleanFields();
    }

    @FXML
    private void updatePatientInformations() {
        Patient patient = new Patient(Integer.parseInt(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.parseInt(this.dateField.getText()));
        this.patientDaolmpl.update(patient, patient.getId());
    }

    @FXML
    private void cancelAction() {
        this.patientsStage = (Stage) this.cancelButton.getScene().getWindow();
        this.patientsStage.close();
    }

    @FXML
    private void choosePatientAction() {
        this.addInclusionController.setPatientInformations(Integer.parseInt(this.idField.getText()), this.initialesField.getText());
        this.patientsStage = (Stage) this.chooseButton.getScene().getWindow();
        this.patientsStage.close();
    }
}
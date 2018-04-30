package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import src.daoImpl.PatientDaoImpl;
import src.table.Patient;
import src.utils.Genre;

import java.net.URL;
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
    private PatientDaoImpl patientDaoImpl;
    private ObservableList<Patient> patientsList;
    private int selectedId;
    private AddInclusionController addInclusionController;
    private ArrayList<String> idPatients = new ArrayList<>();
    private boolean searchMode;

    public PatientsController(AddInclusionController addInclusionController, PatientDaoImpl patientDaoImpl) {
        this.addInclusionController = addInclusionController;
        this.patientDaoImpl = patientDaoImpl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.patIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.patInitialesCol.setCellValueFactory(cellData -> cellData.getValue().initialesProperty());
        this.patGenreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        this.patDateCol.setCellValueFactory(cellData -> cellData.getValue().dateNaissanceProperty().asObject());
        this.genreComboBox.getItems().addAll(this.comboBoxValeurs);
        this.patientsList = this.patientDaoImpl.selectAll();
        this.populatePatients();
        this.populateIdPatients();

        this.idField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                this.selectedId = this.idPatients.indexOf(this.idField.getText());

                if (this.idPatients.contains(this.idField.getText())) {
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

        this.idField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.idField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.idSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.idSearchField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (this.initialesField.getText().length() >= 4)
                    this.initialesField.setText(this.initialesField.getText().substring(0, 4));
            }
        });

        this.initialesField.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        this.initialesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z]*"))
                this.initialesField.setText(newValue.replaceAll("[^A-Za-z]", ""));
        });

        this.initialesSearchField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (this.initialesSearchField.getText().length() >= 4)
                    this.initialesSearchField.setText(this.initialesSearchField.getText().substring(0, 4));
            }
        });

        this.initialesSearchField.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        this.initialesSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z]*"))
                this.initialesSearchField.setText(newValue.replaceAll("[^A-Za-z]", ""));
        });

        this.genreComboBox.itemsProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue))
                this.patientsTable.getSelectionModel().clearSelection();
        });

        this.dateField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (this.dateField.getText().length() >= 4)
                    this.dateField.setText(this.dateField.getText().substring(0, 4));
            }
        });

        this.dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                this.dateField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.dateSearchField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (this.dateSearchField.getText().length() >= 4)
                    this.dateSearchField.setText(this.dateSearchField.getText().substring(0, 4));
            }
        });

        this.dateSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.dateSearchField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.patientsTable.setOnMouseClicked((MouseEvent event) -> {
            int id = this.patientsTable.getSelectionModel().getSelectedIndex();
            if ((event.getButton().equals(MouseButton.PRIMARY) || event.getButton().equals(MouseButton.SECONDARY)) && id >= 0) {
                this.selectedId = id;
                Patient selectedPatient = this.patientsList.get(this.selectedId);

                if (selectedPatient != null) {
                    this.idField.setText(Integer.toString(selectedPatient.getId()));
                    this.initialesField.setText(selectedPatient.getInitiales());
                    this.dateField.setText(Integer.toString(selectedPatient.getAnneeNaissance()));
                    this.genreComboBox.setValue(selectedPatient.getGenre().equals("Homme") ? Genre.M : Genre.F);
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
            }
        });

        this.idSearchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                this.searchAction();
        });

        this.initialesSearchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                this.searchAction();
        });

        this.dateSearchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                this.searchAction();
        });
    }

    private void populateIdPatients() {
        for (Patient patient : this.patientsList)
            this.idPatients.add(Integer.toString(patient.getId()));
    }

    private void populatePatients() {
        if (!this.patientsList.isEmpty())
            this.patientsTable.setItems(this.patientsList);
        else this.patientsTable.setItems(FXCollections.observableArrayList());
    }

    private void populatePatient(Patient patient) {
        this.patientsList.add(patient);
        this.idPatients.add(Integer.toString(patient.getId()));
        this.populatePatients();
    }

    private void cleanFields() {
        this.idField.setText("");
        this.initialesField.setText("");
        this.genreComboBox.setValue(null);
        this.dateField.setText("");
        this.idField.requestFocus();
        this.patientsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void addPatientAction() {
        Patient patient = new Patient(Integer.parseInt(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.parseInt(this.dateField.getText()));

        if (this.searchMode) {
            this.searchMode = false;
            this.patientsList = this.patientDaoImpl.selectAll();
        }

        this.patientDaoImpl.insert(patient);
        this.idPatients.add(this.idField.getText());
        this.cleanFields();
        this.populatePatient(patient);
    }

    @FXML
    private void searchAction() {
        this.searchMode = true;
        this.patientsList = this.patientDaoImpl.selectByFilters(this.idSearchField.getText().isEmpty() ? 0 : Integer.parseInt(this.idSearchField.getText()), this.initialesSearchField.getText(), this.dateSearchField.getText());
        this.populatePatients();
    }

    @FXML
    private void searchAllAction() {
        this.patientsList = this.patientDaoImpl.selectAll();
        this.populatePatients();
    }

    @FXML
    private void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer le patient");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            /*TODO Supprimer les docs qui vont avec*/
            this.patientDaoImpl.delete(Integer.parseInt(this.idField.getText()));
            this.patientsList.remove(this.patientsList.get(this.selectedId));
            this.idPatients.clear();
            this.populateIdPatients();
            this.cleanFields();
        } else alert.close();
    }

    @FXML
    private void updatePatientInformations() {
        Patient selectedPatient = this.patientsList.get(this.selectedId);
        Patient patient = new Patient(Integer.parseInt(this.idField.getText()), this.initialesField.getText(), this.genreComboBox.getValue().toString(), Integer.parseInt(this.dateField.getText()));
        this.patientDaoImpl.update(patient, patient.getId());
        selectedPatient.setInitiales(this.initialesField.getText());
        selectedPatient.setGenre(this.genreComboBox.getValue().toString());
        selectedPatient.setDateNaissance(Integer.parseInt(this.dateField.getText()));
    }

    @FXML
    private void cancelAction() {
        if (this.patientsStage == null)
            this.patientsStage = (Stage) this.cancelButton.getScene().getWindow();

        this.patientsStage.close();
    }

    @FXML
    private void choosePatientAction() {
        if (this.patientsStage == null)
            this.patientsStage = (Stage) this.chooseButton.getScene().getWindow();

        this.addInclusionController.setPatientInformations(Integer.parseInt(this.idField.getText()), this.initialesField.getText());
        this.patientsStage.close();
    }
}
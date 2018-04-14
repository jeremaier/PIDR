package src.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    ComboBox genreComboBox;
    @FXML
    Button addPatientButton;
    @FXML
    Button chooseButton;
    @FXML
    Button removeButton;
    @FXML
    Button cancelButton;
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
        patIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        patInitialesCol.setCellValueFactory(cellData -> cellData.getValue().initialesProperty());
        patGenreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        patDateCol.setCellValueFactory(cellData -> cellData.getValue().dateNaissanceProperty().asObject());
        this.genreComboBox.getItems().addAll(Genre.M, Genre.F);

        patientDaolmpl = new PatientDaolmpl(connection);
        ObservableList<Patient> patients = patientDaolmpl.selectAll();
        this.populatePatients(patients);

        this.initialesField.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue())
                    if(initialesField.getText().length() >= 2)
                        initialesField.setText(initialesField.getText().substring(0, 2));
            }
        });

        this.idField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*"))
                    idField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        this.dateField.lengthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() > oldValue.intValue())
                    if(initialesField.getText().length() >= 4)
                        initialesField.setText(initialesField.getText().substring(0, 4));
            }
        });

        this.dateField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*"))
                    idField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        this.patientsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                selectedPatient = (Patient)patientsTable.getSelectionModel().getSelectedItem();

                if(selectedPatient != null) {
                    idField.setText(Integer.toString(selectedPatient.getId()));
                    initialesField.setText(selectedPatient.getInitiales());
                    dateField.setText(Integer.toString(selectedPatient.getAnneeNaissance()));
                    genreComboBox.setValue(selectedPatient.getGenre());
                } else {
                    idField.setText("");
                    initialesField.setText("");
                    dateField.setText("");
                    genreComboBox.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void populatePatients(ObservableList<Patient> patients) {
        patientsTable.setItems(patients);
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
    private void searchPatient(ActionEvent actionEvent) {
        patientsList = patientDaolmpl.selectByFilters(Integer.getInteger(this.idField.getText()), this.initialesField.getText());
        this.populatePatients(patientsList);
    }

    @FXML
    private void searchAll(ActionEvent actionEvent) {
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
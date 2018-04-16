package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.table.Inclusion;
import src.utils.FileManager;
import src.view.PatientsView;

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
    Button addButton;
    @FXML
    Button addPatientButton;
    @FXML
    Button reference1FileButton;
    @FXML
    Button reference2FileButton;
    @FXML
    Button cancelButton;
    @FXML
    Label patientLabel;
    @FXML
    Label reference1FileLabel;
    @FXML
    Label reference2FileLabel;

    private Inclusion inclusion;
    private InclusionDaoImpl inclusionDaoImpl;
    private Connection connection;
    private Stage addInclusionStage;
    private int patientId;
    private String initiales;
    private FileManager fileManager;

    public AddInclusionController(Inclusion inclusion, Connection connection, FileManager fileManager) {
        this.inclusion = inclusion;
        this.connection = connection;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inclusionDaoImpl = new InclusionDaoImpl(connection);

        if(this.inclusion != null)
            this.setInclusionInformations();

        if(this.inclusion != null)
            this.addButton.setText("Modifier");
    }

    private void setInclusionInformations() {
        /*InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(connection);
        this.inclusion = inclusionDaoImpl.selectById(inclusion.getId());*/
        this.inclusionIDField.setText(Integer.toString(this.inclusion.getId()));
        this.patientLabel.setText(this.inclusion.getInitialesPatient());
        this.reference1FileLabel.setText(FileManager.getFileName(this.inclusion.getReference1()));
        this.reference2FileLabel.setText(FileManager.getFileName(this.inclusion.getReference2()));
        this.inclusionDatePicker.setValue(this.inclusion.getDateInclusion().toLocalDate());
    }

    void setPatientInformations(int patientId, String initiales) {
        this.patientId = patientId;
        this.initiales = initiales;
    }

    @FXML
    private void addAction(ActionEvent actionEvent) {
        inclusion = new Inclusion();
        inclusionDaoImpl.update(inclusion, Integer.getInteger(this.inclusionIDField.getText()));
    }

    @FXML
    private void addPatientAction(ActionEvent actionEvent) {
        new PatientsView(connection, this);
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        if(this.addInclusionStage == null)
            this.addInclusionStage = (Stage) cancelButton.getScene().getWindow();

        fileManager.downloadFromUrl(addInclusionStage, this.inclusion.getReference1());
        this.addInclusionStage.close();
    }

    @FXML
    private void reference1FileAction(ActionEvent actionEvent) {
        if(this.reference1FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) reference1FileButton.getScene().getWindow();

        } else fileManager.removeFile(this.inclusion.getReference1());
    }

    @FXML
    private void reference2FileAction(ActionEvent actionEvent) {
        if(this.reference2FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) reference2FileButton.getScene().getWindow();

            fileManager.uploadToURL(this.addInclusionStage, "references");
        } else fileManager.removeFile(this.inclusion.getReference2());
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
package src.controller;

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
import java.sql.Date;
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
    private InclusionsController inclusionsController;
    private InclusionDaoImpl inclusionDaoImpl;
    private Connection connection;
    private Stage addInclusionStage;
    private FileManager fileManager;
    private int patientId;
    private String initiales;

    public AddInclusionController(InclusionsController inclusionsController, Inclusion inclusion, Connection connection, FileManager fileManager) {
        this.inclusionsController = inclusionsController;
        this.inclusion = inclusion;
        this.connection = connection;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inclusionDaoImpl = new InclusionDaoImpl(connection);

        if (this.inclusion != null) {
            this.setInclusionInformations();
            this.addButton.setText("Modifier");
        }
    }

    private void setInclusionInformations() {
        /*InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(connection);
        this.inclusion = inclusionDaoImpl.selectById(inclusion.getId());*/
        this.inclusionIDField.setText(Integer.toString(this.inclusion.getId()));
        this.patientLabel.setText(this.inclusion.getInitialesPatient());
        this.reference1FileLabel.setText(FileManager.getFileName(this.inclusion.getReference1(), false));
        this.reference2FileLabel.setText(FileManager.getFileName(this.inclusion.getReference2(), false));
        this.inclusionDatePicker.setValue(this.inclusion.getDateInclusion().toLocalDate());
    }

    void setPatientInformations(int patientId, String initiales) {
        this.patientId = patientId;
        this.initiales = initiales;
        this.patientLabel.setText(this.initiales);
    }

    @FXML
    private void addAction() {
        if (this.inclusion == null) {
            this.inclusion = new Inclusion(Integer.parseInt(this.inclusionIDField.getText()),
                    this.patientId,
                    this.initiales,
                    FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference1FileLabel.getText(),
                    FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference2FileLabel.getText(),
                    Date.valueOf(this.inclusionDatePicker.getValue()),
                    Integer.parseInt(this.idAnapathField.getText()),
                    null);
            this.inclusionDaoImpl.insert(inclusion);
            this.inclusionsController.populateInclusion(this.inclusion);
        } else {
            this.inclusion.setIdPatient(this.patientId);
            this.inclusion.setInitialesPatient(this.initiales);
            this.inclusion.setReference1(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference1FileLabel.getText());
            this.inclusion.setReference2(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference2FileLabel.getText());
            this.inclusion.setDateInclusion(Date.valueOf(this.inclusionDatePicker.getValue()));
            this.inclusion.setNumAnaPath(Integer.parseInt(this.idAnapathField.getText()));
            this.inclusionDaoImpl.update(inclusion, Integer.parseInt(this.inclusionIDField.getText()));
            this.inclusionsController.refreshInclusions();
        }

        if (addInclusionStage == null)
            this.addInclusionStage = (Stage) addButton.getScene().getWindow();

        this.addInclusionStage.close();
    }

    @FXML
    private void addPatientAction() {
        new PatientsView(connection, this);
    }

    @FXML
    private void cancelAction() {
        if(this.addInclusionStage == null)
            this.addInclusionStage = (Stage) cancelButton.getScene().getWindow();

        this.addInclusionStage.close();
    }

    @FXML
    private void reference1FileAction() {
        fileManager.openFTPConnection();

        if(this.reference1FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) reference1FileButton.getScene().getWindow();

            fileManager.uploadToURL(this.addInclusionStage, FileManager.getRefDirectoryName(inclusionIDField.getText()), null);
        } else fileManager.removeFile(this.inclusion.getReference1());

        fileManager.closeConnection();
    }

    @FXML
    private void reference2FileAction() {
        fileManager.openFTPConnection();

        if(this.reference2FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) reference2FileButton.getScene().getWindow();

            fileManager.uploadToURL(this.addInclusionStage, "references", null);
        } else fileManager.removeFile(this.inclusion.getReference2());

        fileManager.closeConnection();
    }
}
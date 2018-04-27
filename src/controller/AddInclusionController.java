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
    private boolean[] uploadedBeforeEdit = {true, true};

    public AddInclusionController(InclusionsController inclusionsController, Inclusion inclusion, InclusionDaoImpl inclusionDaoImpl, Connection connection, FileManager fileManager) {
        this.inclusionsController = inclusionsController;
        this.inclusion = inclusion;
        this.inclusionDaoImpl = inclusionDaoImpl;
        this.connection = connection;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.inclusion != null) {
            this.setInclusionInformations();
            this.addButton.setText("Modifier");
        } else this.inclusion = new Inclusion();
    }

    private void setInclusionInformations() {
        this.inclusionIDField.setText(this.inclusion.getId());
        this.patientLabel.setText(this.inclusion.getInitialesPatient());

        if (this.inclusion.getReference1() != null) {
            this.reference1FileLabel.setText(FileManager.getFileName(this.inclusion.getReference1(), false));
            this.reference1FileButton.setText("Modifier");
        }

        if (this.inclusion.getReference2() != null) {
            this.reference2FileLabel.setText(FileManager.getFileName(this.inclusion.getReference2(), false));
            this.reference2FileButton.setText("Modifier");
        }

        this.inclusionDatePicker.setValue(this.inclusion.getDateInclusion().toLocalDate());
    }

    void setPatientInformations(int patientId, String initiales) {
        this.patientId = patientId;
        this.initiales = initiales;
        this.patientLabel.setText(this.initiales);
    }

    @FXML
    private void addAction() {
        /*TODO Faire que les fichiers s'add et se suppr au moment de quitter la fenetre*/
        boolean newInclusion = this.inclusion.getId() == null;
        this.inclusion.setIdPatient(this.patientId);
        this.inclusion.setInitialesPatient(this.initiales);

        if (!this.reference1FileLabel.getText().equals("Aucun"))
            this.inclusion.setReference1(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference1FileLabel.getText());

        if (!this.reference2FileLabel.getText().equals("Aucun"))
            this.inclusion.setReference2(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference2FileLabel.getText());

        this.inclusion.setDateInclusion(Date.valueOf(this.inclusionDatePicker.getValue()));
        this.inclusion.setNumAnaPath(Integer.parseInt(this.idAnapathField.getText()));

        if (newInclusion) {
            this.inclusionDaoImpl.insert(this.inclusion);
            this.inclusionsController.populateInclusion(this.inclusion);
        } else {
            this.inclusionDaoImpl.update(this.inclusion, Integer.parseInt(this.inclusionIDField.getText()));
            this.inclusionsController.refreshInclusions();
        }

        if (this.addInclusionStage == null)
            this.addInclusionStage = (Stage) this.addButton.getScene().getWindow();

        this.addInclusionStage.close();
    }

    @FXML
    private void addPatientAction() {
        new PatientsView(this.connection, this);
    }

    @FXML
    private void cancelAction() {
        if (!this.reference1FileLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[0])
            this.fileManager.removeFile(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference1FileLabel.getText());

        if (!this.reference2FileLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[1])
            this.fileManager.removeFile(FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//" + this.reference2FileLabel.getText());

        if(this.addInclusionStage == null)
            this.addInclusionStage = (Stage) this.cancelButton.getScene().getWindow();

        this.addInclusionStage.close();
    }

    @FXML
    private void reference1FileAction() {
        if(this.reference1FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) this.reference1FileButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addInclusionStage, FileManager.getRefDirectoryName(this.inclusionIDField.getText()), null);

            if (addedFileName != null) {
                this.reference1FileButton.setText("Supprimer");
                this.reference1FileLabel.setText(addedFileName);

                if (this.addButton.getText().equals("Modifier"))
                    this.uploadedBeforeEdit[0] = false;
            }
        } else {
            this.fileManager.removeFile(this.inclusion.getReference1());
            this.reference1FileButton.setText("Ajouter");
            this.reference1FileLabel.setText("Aucun");
        }
    }

    @FXML
    private void reference2FileAction() {
        if(this.reference2FileLabel.getText().equals("Aucun")) {
            if(this.addInclusionStage == null)
                this.addInclusionStage = (Stage) this.reference2FileButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addInclusionStage, FileManager.getRefDirectoryName(this.inclusionIDField.getText()), null);

            if (addedFileName != null) {
                this.reference2FileButton.setText("Supprimer");
                this.reference2FileLabel.setText(addedFileName);

                if (this.addButton.getText().equals("Modifier"))
                    this.uploadedBeforeEdit[1] = false;
            }
        } else {
            this.fileManager.removeFile(this.inclusion.getReference2());
            this.reference2FileButton.setText("Ajouter");
            this.reference2FileLabel.setText("Aucun");
        }
    }
}
package src.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.PatientDaoImpl;
import src.table.Inclusion;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.utils.UploadTask;
import src.view.PatientsView;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddInclusionController extends Controller implements Initializable {
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

    private ObservableList<Inclusion> inclusionsList;
    private Inclusion inclusion;
    private InclusionsController inclusionsController;
    private InclusionDaoImpl inclusionDaoImpl;
    private PatientDaoImpl patientDaoImpl;
    private int patientId;

    public AddInclusionController(InclusionsController inclusionsController, ObservableList<Inclusion> inclusionsList, Inclusion inclusion, InclusionDaoImpl inclusionDaoImpl, Connection connection, FileManager fileManager) {
        this.inclusionsController = inclusionsController;
        this.inclusionsList = inclusionsList;
        this.inclusion = inclusion;
        this.inclusionDaoImpl = inclusionDaoImpl;
        this.fileManager = fileManager;
        this.patientDaoImpl = new PatientDaoImpl(connection);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.inclusion != null) {
            this.setInclusionInformations();
            this.addButton.setText("Modifier");
        } else this.inclusion = new Inclusion();

        this.inclusionIDField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.inclusionIDField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.idAnapathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.idAnapathField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.inclusionIDField.lengthProperty().addListener((observable, oldValue, newValue) -> this.enableButtons(this.inclusionIDField.getText().length() >= 1 && !this.idAlreadyExistant(Integer.parseInt(this.inclusionIDField.getText())), false));
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        if (this.addButton.getText().equals("Modifier"))
            this.inclusionIDField.setDisable(true);

        this.addButton.setDisable(!enable);
        this.reference1FileButton.setDisable(!enable);
        this.reference2FileButton.setDisable(!enable);

        if (all) {
            this.addPatientButton.setDisable(!enable);
            this.cancelButton.setDisable(!enable);
        }
    }

    private boolean idAlreadyExistant(int id) {
        if (!this.addButton.getText().equals("Modifier"))
            for (Inclusion anInclusionsList : this.inclusionsList)
                if (Integer.parseInt(anInclusionsList.getId()) == id)
                    return true;

        return false;
    }

    private void setInclusionInformations() {
        this.inclusionIDField.setText(this.inclusion.getId());
        Date dateInclusion = InclusionDaoImpl.stringToDate(this.inclusion.getDateInclusion());
        int numAna = this.inclusion.getNumAnaPat();

        if (this.inclusion.getIdPatient() != 0) {
            this.patientLabel.setText("ID : " + Integer.toString(this.inclusion.getIdPatient()));
            this.addPatientButton.setText("Supprimer");
            this.patientId = this.inclusion.getIdPatient();
        }

        if (!this.inclusion.getReference1().equals("Aucun")) {
            this.reference1FileLabel.setText(FileManager.getFileName(this.inclusion.getReference1(), false));
            this.reference1FileButton.setText("Supprimer");
        }

        if (!this.inclusion.getReference2().equals("Aucun")) {
            this.reference2FileLabel.setText(FileManager.getFileName(this.inclusion.getReference2(), false));
            this.reference2FileButton.setText("Supprimer");
        }

        if (dateInclusion != null)
            this.inclusionDatePicker.setValue(dateInclusion.toLocalDate());

        if (numAna != 0)
            this.idAnapathField.setText(Integer.toString(numAna));

        this.addButton.setDisable(false);
        this.reference1FileButton.setDisable(false);
        this.reference2FileButton.setDisable(false);
        this.inclusionIDField.setDisable(true);
    }

    void setPatientInformations(int patientId) {
        this.patientId = patientId;
        this.patientLabel.setText("ID : " + Integer.toString(this.patientId));
        this.addPatientButton.setText("Supprimer");
    }

    @FXML
    private void addAction() {
        String id = this.inclusionIDField.getText();
        boolean newInclusion = this.inclusion.getId() == null;
        this.inclusion.setId(Integer.parseInt(id));

        if (this.patientId != 0)
            this.inclusion.setIdPatient(this.patientId);

        if (!this.reference1FileLabel.getText().equals("Aucun"))
            this.inclusion.setReference1(FileManager.getRefDirectoryName(id) + "//" + this.reference1FileLabel.getText());

        if (!this.reference2FileLabel.getText().equals("Aucun"))
            this.inclusion.setReference2(FileManager.getRefDirectoryName(id) + "//" + this.reference2FileLabel.getText());

        if (this.inclusionDatePicker.getValue() != null)
            this.inclusion.setDateInclusion(Date.valueOf(this.inclusionDatePicker.getValue()));

        this.inclusion.setNumAnaPath(!this.idAnapathField.getText().equals("") ? Integer.parseInt(this.idAnapathField.getText()) : 0);

        if (newInclusion) {
            this.inclusionDaoImpl.insert(this.inclusion);
            this.inclusionsController.populateInclusion(this.inclusion);
        } else {
            this.inclusionDaoImpl.update(this.inclusion, Integer.parseInt(id));
            this.inclusionsController.refreshInclusions();
        }

        this.setStage(this.addButton);
        this.stage.close();
    }

    @FXML
    private void addPatientAction() {
        if (this.addPatientButton.getText().equals("Supprimer")) {
            this.patientId = 0;
            this.patientLabel.setText("Aucun");
            this.addPatientButton.setText("Ajouter");
            this.inclusion.setIdPatient(this.patientId);
        } else new PatientsView(this, this.patientDaoImpl);
    }

    @FXML
    private void cancelAction() {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.cancelButton);
        String ref1, ref2;
        String directory = FileManager.getRefDirectoryName(this.inclusionIDField.getText()) + "//";
        ArrayList<String> refs = new ArrayList<>();

        if (!this.addButton.getText().equals("Modifier")) {
            if (!(ref1 = this.reference1FileLabel.getText()).equals("Aucun"))
                refs.add(directory + ref1);

            if (!(ref2 = this.reference2FileLabel.getText()).equals("Aucun"))
                refs.add(directory + ref2);
        }

        removeTask.addUrls(refs);
        new Thread(removeTask).start();

        this.stage.close();
    }

    @FXML
    private void reference1FileAction() {
        if (this.reference1FileLabel.getText().equals("Aucun"))
            this.startUpload("ref1", this.reference1FileButton, this.reference1FileLabel);
        else this.removeFileFromFTP("ref1", reference1FileButton, reference1FileLabel);
    }

    @FXML
    private void reference2FileAction() {
        if (this.reference2FileLabel.getText().equals("Aucun"))
            this.startUpload("ref2", this.reference2FileButton, this.reference2FileLabel);
        else this.removeFileFromFTP("ref2", reference2FileButton, reference2FileLabel);
    }

    private void removeFileFromFTP(String buttonName, Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button);

        switch (buttonName) {
            case "ref1":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(inclusion.getReference1());
                }});

                this.inclusion.setReference1("Aucun");
                break;
            case "ref2":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(inclusion.getReference2());
                }});

                this.inclusion.setReference2("Aucun");
                break;
        }

        removeTask.setOnSucceeded(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        removeTask.setOnSucceeded(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        this.inclusionDaoImpl.update(inclusion, Integer.parseInt(this.inclusionIDField.getText()));
        new Thread(removeTask).start();
    }

    private void startUpload(String buttonName, Button button, Label label) {
        String directory = FileManager.getRefDirectoryName(this.inclusionIDField.getText());
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, null);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        uploadTask.setOnSucceeded(e -> this.endUpload(buttonName, uploadTask.getAddedFileName(), directory, button, label));
        uploadTask.setOnFailed(e -> this.endUpload(buttonName, uploadTask.getAddedFileName(), directory, button, label));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    private void endUpload(String buttonName, String addedFileName, String directory, Button button, Label label) {
        if (addedFileName != null) {
            button.setText("Supprimer");
            label.setText(addedFileName);
            String url = directory + "//" + addedFileName;

            switch (buttonName) {
                case "ref1":
                    this.inclusion.setReference1(url);
                    break;
                case "ref2":
                    this.inclusion.setReference2(url);
                    break;
            }
        }

        this.enableButtons(true, true);
    }
}
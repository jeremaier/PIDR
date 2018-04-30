package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.table.Inclusion;
import src.utils.Diag;
import src.utils.FileManager;
import src.view.AddInclusionsView;
import src.view.LesionsView;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
    ComboBox<Diag> diagnosticChoiceBox;
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
    Button docDownloadButton;
    @FXML
    Button removeDocButton;
    @FXML
    Button importProcButton;
    @FXML
    Button importResultButton;
    @FXML
    Button selectAllDocsButton;
    @FXML
    Button lesionsButton;
    @FXML
    ListView<String> procList;
    @FXML
    ListView<String> resList;
    @FXML
    TableView<Inclusion> inclusionsTable;
    @FXML
    TableColumn<Inclusion, String> inclID;
    @FXML
    TableColumn<Inclusion, String>  inclDate;
    @FXML
    TableColumn<Inclusion, Integer> inclAnapath;
    @FXML
    TableColumn<Inclusion, String> inclIDPatient;
    @FXML
    TableColumn<Inclusion, String> inclDiagnostic;

    private Connection connection;
    private FileManager fileManager;
    private Stage inclusionsStage;
    private InclusionDaoImpl inclusionDaoImpl;
    private ObservableList<Inclusion> inclusionsList;
    private ObservableList<String> procObservableList;
    private ObservableList<String> resObservableList;
    private Inclusion selectedInclusion;
    private String selectedDoc;

    public InclusionsController(Connection connection, FileManager fileManager) {
        this.connection = connection;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inclID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        this.inclDate.setCellValueFactory(cellData -> cellData.getValue().dateInclusionProperty().asString());
        this.inclAnapath.setCellValueFactory(cellData -> cellData.getValue().numAnaPathProperty().asObject());
        this.inclIDPatient.setCellValueFactory(cellData -> cellData.getValue().idPatientProperty().asString());
        this.inclDiagnostic.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList(Diag.BASO, Diag.SPINO, Diag.KERATOSE, Diag.AUTRE, Diag.FICHIER, Diag.RIEN));
        this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName(), false, false);

        if (this.procObservableList != null)
            this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName(), false, true);

        this.procList.setItems(this.procObservableList);
        this.resList.setItems(this.resObservableList);
        this.inclusionDaoImpl = new InclusionDaoImpl(this.connection);
        this.inclusionsList = this.inclusionDaoImpl.selectAll();
        this.populateInclusions();

        this.procList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (this.procList.getSelectionModel().getSelectedItem() != null)
                this.selectedDoc = FileManager.getProcDirectoryName() + "//" + newValue;
        });

        this.resList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (this.resList.getSelectionModel().getSelectedItem() != null)
                this.selectedDoc = FileManager.getResDirectoryName() + "//" + newValue;
        });

        this.idInclusionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.idInclusionField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.initialesField.getText().length() >= 4)
                    this.initialesField.setText(this.initialesField.getText().substring(0, 4));
        });

        this.resList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (this.resList.getSelectionModel().getSelectedItem() != null)
                this.procList.getSelectionModel().clearSelection();

            click.consume();
        });

        this.procList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (this.procList.getSelectionModel().getSelectedItem() != null)
                this.resList.getSelectionModel().clearSelection();

            click.consume();
        });

        this.inclusionsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            this.selectedInclusion = this.inclusionsTable.getSelectionModel().getSelectedItem();

            if (this.selectedInclusion == null) {
                this.removeButton.setDisable(true);
                this.editButton.setDisable(true);
                this.lesionsButton.setDisable(true);
                this.refDownloadButton.setDisable(true);
            } else {
                this.removeButton.setDisable(false);
                this.editButton.setDisable(false);
                this.lesionsButton.setDisable(false);

                if (!this.selectedInclusion.getReference1().equals("Aucun") || !this.selectedInclusion.getReference2().equals("Aucun"))
                    this.refDownloadButton.setDisable(false);
            }
        });

        this.procList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
        this.resList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
    }

    private void displayRemoveDownloadButtons() {
        if (this.resList.getSelectionModel().getSelectedItem() != null || this.procList.getSelectionModel().getSelectedItem() != null) {
            this.removeDocButton.setDisable(false);
            this.docDownloadButton.setDisable(false);
        } else {
            this.removeDocButton.setDisable(true);
            this.docDownloadButton.setDisable(true);
        }
    }

    void refreshInclusions() {
        this.inclusionsList = this.inclusionDaoImpl.selectAll();

        if (!this.inclusionsList.isEmpty())
            this.inclusionsTable.setItems(this.inclusionsList);
        else this.inclusionsTable.setItems(FXCollections.observableArrayList());
    }

    void populateInclusion(Inclusion inclusion) {
        this.inclusionsList.add(inclusion);
        this.inclusionsTable.setItems(this.inclusionsList);
    }

    private void populateInclusions() {
        if (!this.inclusionsList.isEmpty())
            this.inclusionsTable.setItems(this.inclusionsList);
        else this.inclusionsTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void searchAction() {
        this.inclusionsList = this.inclusionDaoImpl.selectByFilters(this.idInclusionField.getText().equals("") ? 0 : Integer.parseInt(this.idInclusionField.getText()),
                this.inclusionDatePicker.getValue() == null ? null : Date.valueOf(this.inclusionDatePicker.getValue()),
                this.idAnapathField.getText().equals("") ? 0 : Integer.parseInt(this.idAnapathField.getText()),
                this.initialesField.getText(),
                this.diagnosticChoiceBox.getValue());

        this.populateInclusions();
    }

    @FXML
    private void searchAllAction() {
        this.inclusionsList = this.inclusionDaoImpl.selectAll();
        this.populateInclusions();
    }

    @FXML
    private void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer l'inclusion et les documents attachés");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            this.removeFiles();
            this.inclusionDaoImpl.delete(Integer.parseInt(this.selectedInclusion.getId()));
            this.inclusionsList.remove(this.selectedInclusion);
            this.inclusionsTable.getSelectionModel().clearSelection();
        } else alert.close();
    }

    private void removeFiles() {
        String reference1Url = this.selectedInclusion.getReference1();
        String reference2Url = this.selectedInclusion.getReference2();

        if (!reference1Url.equals("Aucun"))
            this.fileManager.removeFile(reference1Url);
        if (!reference2Url.equals("Aucun"))
            this.fileManager.removeFile(reference2Url);
    }

    @FXML
    private void refDownloadAction() {
        String ref1Url = this.selectedInclusion.getReference1();
        String ref2Url = this.selectedInclusion.getReference2();
        File choosenDirectory = null;

        this.fileManager.openFTPConnection();

        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.refDownloadButton.getScene().getWindow();

        if (!ref1Url.equals("Aucun"))
            choosenDirectory = this.fileManager.downloadFromUrl(this.inclusionsStage, ref1Url, null, false, false);

        if (!ref2Url.equals("Aucun"))
            this.fileManager.downloadFromUrl(this.inclusionsStage, ref2Url, choosenDirectory, false, false);

        this.fileManager.closeFTPConnection();
    }

    private void populateDocs(ObservableList<String> res, ObservableList<String> proc) {
        if (!res.isEmpty())
            this.resList.setItems(this.resObservableList);
        else this.resList.setItems(FXCollections.observableArrayList());

        if (!proc.isEmpty())
            this.procList.setItems(this.procObservableList);
        else this.procList.setItems(FXCollections.observableArrayList());
    }

    private void populateProcDoc(String addedFileName) {
        this.procObservableList.add(addedFileName);
        this.procList.setItems(this.procObservableList);
    }

    private void populateResDoc(String addedFileName) {
        this.resObservableList.add(addedFileName);
        this.procList.setItems(this.resObservableList);
    }

    @FXML
    private void importProcAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.importProcButton.getScene().getWindow();

        String addedFileName = this.fileManager.uploadToURL(this.inclusionsStage, FileManager.getProcDirectoryName(), null);

        if (addedFileName != null)
            this.populateProcDoc(addedFileName);
    }

    @FXML
    private void importResultAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.importResultButton.getScene().getWindow();

        String addedFileName = this.fileManager.uploadToURL(this.inclusionsStage, FileManager.getResDirectoryName(), null);

        if (addedFileName != null)
            this.populateResDoc(addedFileName);
    }

    @FXML
    private void docDownloadAction() {
        if (this.selectedDoc != null) {
            if (this.inclusionsStage == null)
                this.inclusionsStage = (Stage) this.docDownloadButton.getScene().getWindow();

            this.fileManager.downloadFromUrl(this.inclusionsStage, this.selectedDoc, null, true, true);
        }
    }

    @FXML
    private void removeDocAction() {
        if (this.selectedDoc != null) {
            if (this.fileManager.removeFile(this.selectedDoc)) {
                this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName(), true, false);
                this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName(), false, true);
            }

            this.populateDocs(this.resObservableList, this.procObservableList);
        }
    }

    @FXML
    private void searchDocAction() {
        if (this.searchDocField.getText() != null) {
            this.resObservableList = this.fileManager.getFileFromFtp(this.searchDocField.getText(), FileManager.getResDirectoryName(), true, false);
            this.procObservableList = this.fileManager.getFileFromFtp(this.searchDocField.getText(), FileManager.getProcDirectoryName(), false, true);
            this.populateDocs(this.resObservableList, this.procObservableList);
        }
    }

    @FXML
    private void selectAllDocsAction() {
        this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName(), true, false);
        this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName(), false, true);
        this.procList.setItems(this.procObservableList);
        this.resList.setItems(this.resObservableList);
    }

    @FXML
    private void editAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.editButton.getScene().getWindow();

        new AddInclusionsView(this, this.inclusionsList, this.selectedInclusion, this.inclusionDaoImpl, this.connection, this.fileManager);
    }

    @FXML
    private void addAction() {
        if (this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.addButton.getScene().getWindow();

        new AddInclusionsView(this, this.inclusionsList, null, this.inclusionDaoImpl, this.connection, this.fileManager);
    }

    @FXML
    private void lesionsAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) this.lesionsButton.getScene().getWindow();

        new LesionsView(this.connection, this.fileManager, this.selectedInclusion);

        this.inclusionsStage.close();
    }
}
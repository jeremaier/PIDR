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
    TableColumn<Inclusion, Integer> inclID;
    @FXML
    TableColumn<Inclusion, String>  inclDate;
    @FXML
    TableColumn<Inclusion, Integer> inclAnapath;
    @FXML
    TableColumn<Inclusion, String> inclInitials;
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
        this.inclID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.inclDate.setCellValueFactory(cellData -> cellData.getValue().dateInclusionProperty().asString());
        this.inclAnapath.setCellValueFactory(cellData -> cellData.getValue().numAnaPathProperty().asObject());
        this.inclInitials.setCellValueFactory(cellData -> cellData.getValue().initialesPatientProperty());
        this.inclDiagnostic.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList(Diag.BASO, Diag.SPINO, Diag.KERATOSE, Diag.AUTRE, Diag.FICHIER, Diag.RIEN));
        this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName());
        this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName());
        this.fileManager.closeConnection();
        procList.setItems(this.procObservableList);
        resList.setItems(this.resObservableList);

        procList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (procList.getSelectionModel().getSelectedItem() != null)
                selectedDoc = FileManager.getProcDirectoryName() + "//" + newValue;
        });

        resList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (resList.getSelectionModel().getSelectedItem() != null)
                selectedDoc = FileManager.getProcDirectoryName() + "//" + newValue;
        });

        this.idInclusionField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                idInclusionField.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.initialesField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (initialesField.getText().length() >= 4)
                    initialesField.setText(initialesField.getText().substring(0, 4));
        });

        resList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (resList.getSelectionModel().getSelectedItem() != null)
                procList.getSelectionModel().clearSelection();

            click.consume();
        });

        procList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (procList.getSelectionModel().getSelectedItem() != null)
                resList.getSelectionModel().clearSelection();

            click.consume();
        });

        this.inclusionDaoImpl = new InclusionDaoImpl(connection);
        inclusionsList = inclusionDaoImpl.selectAll();

        this.populateInclusions();

        this.inclusionsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedInclusion = inclusionsTable.getSelectionModel().getSelectedItem();

            if(selectedInclusion == null) {
                this.removeButton.setDisable(true);
                this.editButton.setDisable(true);
                this.refDownloadButton.setDisable(true);
                this.lesionsButton.setDisable(true);
            } else {
                this.removeButton.setDisable(false);
                this.editButton.setDisable(false);
                this.refDownloadButton.setDisable(false);
                this.lesionsButton.setDisable(false);
            }
        });

        this.procList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
        this.resList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
    }

    private void displayRemoveDownloadButtons() {
        if (resList.getSelectionModel().getSelectedItem() != null || procList.getSelectionModel().getSelectedItem() != null) {
            removeDocButton.setDisable(false);
            docDownloadButton.setDisable(false);
        } else {
            removeDocButton.setDisable(true);
            docDownloadButton.setDisable(true);
        }
    }

    void refreshInclusions() {
        this.inclusionsList = this.inclusionDaoImpl.selectAll();

        if (!inclusionsList.isEmpty())
            inclusionsTable.setItems(inclusionsList);
        else inclusionsTable.setItems(FXCollections.observableArrayList());
    }

    void populateInclusion(Inclusion inclusion) {
        this.inclusionsList.add(inclusion);
        inclusionsTable.setItems(this.inclusionsList);
    }

    private void populateInclusions() {
        if (!this.inclusionsList.isEmpty())
            inclusionsTable.setItems(this.inclusionsList);
        else inclusionsTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void searchAction() {
        inclusionsList = inclusionDaoImpl.selectByFilters(this.idInclusionField.getText().equals("") ? 0 : Integer.parseInt(this.idInclusionField.getText()),
                this.inclusionDatePicker.getValue() == null ? null : Date.valueOf(this.inclusionDatePicker.getValue()),
                this.idAnapathField.getText().equals("") ? 0 : Integer.parseInt(this.idAnapathField.getText()),
                this.initialesField.getText(),
                this.diagnosticChoiceBox.getValue());

        this.populateInclusions();
    }

    @FXML
    private void searchAllAction() {
        inclusionsList = inclusionDaoImpl.selectAll();
        this.populateInclusions();
    }

    @FXML
    private void removeAction() {
        inclusionDaoImpl.delete(Integer.parseInt(idInclusionField.getText()));
        inclusionsList.remove(selectedInclusion);
        this.populateInclusions();
    }

    @FXML
    private void refDownloadAction() {
        String ref1Url = this.selectedInclusion.getReference1();
        String ref2Url = this.selectedInclusion.getReference2();
        fileManager.openFTPConnection();

        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) refDownloadButton.getScene().getWindow();

        if(ref1Url != null)
            this.fileManager.downloadFromUrl(this.inclusionsStage, this.selectedInclusion.getReference1(), null);

        if(ref2Url != null)
            this.fileManager.downloadFromUrl(this.inclusionsStage, this.selectedInclusion.getReference2(), null);

        fileManager.closeConnection();
    }

    private void populateDocs(ObservableList<String> res, ObservableList<String> proc) {
        if (!res.isEmpty())
            this.resList.setItems(resObservableList);
        else this.resList.setItems(FXCollections.observableArrayList());

        if (!proc.isEmpty())
            this.procList.setItems(procObservableList);
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
            this.inclusionsStage = (Stage) importProcButton.getScene().getWindow();

        fileManager.openFTPConnection();
        String addedFileName = this.fileManager.uploadToURL(inclusionsStage, FileManager.getProcDirectoryName(), null);
        fileManager.closeConnection();

        if (addedFileName != null)
            this.populateProcDoc(addedFileName);
    }

    @FXML
    private void importResultAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) importResultButton.getScene().getWindow();

        fileManager.openFTPConnection();
        String addedFileName = this.fileManager.uploadToURL(inclusionsStage, FileManager.getResDirectoryName(), null);
        fileManager.closeConnection();

        if (addedFileName != null)
            this.populateResDoc(addedFileName);
    }

    @FXML
    private void docDownloadAction() {
        if (selectedDoc != null) {
            if (this.inclusionsStage == null)
                this.inclusionsStage = (Stage) docDownloadButton.getScene().getWindow();

            fileManager.openFTPConnection();
            this.fileManager.downloadFromUrl(this.inclusionsStage, this.selectedDoc, null);
            fileManager.closeConnection();
        }
    }

    @FXML
    private void removeDocAction() {
        if (this.selectedDoc != null) {
            fileManager.openFTPConnection();
            boolean removed = this.fileManager.removeFile(this.selectedDoc);

            if (removed) {
                this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName());
                this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName());
            }

            fileManager.closeConnection();
            this.populateDocs(resObservableList, procObservableList);
        }
    }

    @FXML
    private void searchDocAction() {
        if (this.searchDocField.getText() != null) {
            fileManager.openFTPConnection();
            resObservableList = this.fileManager.getFileFromFtp(this.searchDocField.getText(), FileManager.getResDirectoryName());
            procObservableList = this.fileManager.getFileFromFtp(this.searchDocField.getText(), FileManager.getProcDirectoryName());
            fileManager.closeConnection();
            this.populateDocs(resObservableList, procObservableList);
        }
    }

    @FXML
    private void selectAllDocsAction() {
        fileManager.openFTPConnection();
        this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName());
        this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName());
        this.fileManager.closeConnection();
        procList.setItems(this.procObservableList);
        resList.setItems(this.resObservableList);
    }

    @FXML
    private void editAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) editButton.getScene().getWindow();

        new AddInclusionsView(this.inclusionsStage, this, this.selectedInclusion, this.connection, this.fileManager);
    }

    @FXML
    private void addAction() {
        if (this.inclusionsStage == null)
            this.inclusionsStage = (Stage) addButton.getScene().getWindow();

        new AddInclusionsView(this.inclusionsStage, this, null, this.connection, this.fileManager);
    }

    @FXML
    private void lesionsAction() {
        if(this.inclusionsStage == null)
            this.inclusionsStage = (Stage) addButton.getScene().getWindow();

        new LesionsView(this.connection, this.fileManager);

        this.inclusionsStage.close();
    }
}
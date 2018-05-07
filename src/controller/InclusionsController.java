package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.LesionDaoImpl;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.utils.UploadTask;
import src.view.AddInclusionsView;
import src.view.LesionsView;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InclusionsController extends Controller implements Initializable {
    @FXML
    TextField idInclusionField;
    @FXML
    TextField idAnapathField;
    @FXML
    TextField idPatientField;
    @FXML
    TextField searchDocField;
    @FXML
    DatePicker inclusionDatePicker;
    @FXML
    ComboBox<Diag> diagnosticChoiceBox;
    @FXML
    Button searchButton;
    @FXML
    Button searchAllButton;
    @FXML
    Button refDownloadButton;
    @FXML
    Button addButton;
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
    TableColumn<Inclusion, String> inclAnapath;
    @FXML
    TableColumn<Inclusion, String> inclIDPatient;
    @FXML
    TableColumn<Inclusion, String> inclDiagnostic;
    @FXML
    ProgressBar progressDocBar;
    @FXML
    Label progressDocLabel;

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
        this.enableButtons(false, false);

        if (!this.inclusionsList.isEmpty())
            this.inclusionsTable.setItems(this.inclusionsList);
        else this.inclusionsTable.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void searchAction() {
        this.inclusionsList = this.inclusionDaoImpl.selectByFilters(this.idInclusionField.getText().equals("") ? 0 : Integer.parseInt(this.idInclusionField.getText()),
                this.inclusionDatePicker.getValue() == null ? null : Date.valueOf(this.inclusionDatePicker.getValue()),
                this.idAnapathField.getText(),
                this.idPatientField.getText(),
                this.diagnosticChoiceBox.getValue());

        this.populateInclusions();
    }

    @FXML
    private void searchAllAction() {
        this.inclusionsList = this.inclusionDaoImpl.selectAll();
        this.populateInclusions();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inclID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        this.inclDate.setCellValueFactory(cellData -> cellData.getValue().dateInclusionProperty());
        this.inclAnapath.setCellValueFactory(cellData -> cellData.getValue().numAnaPathProperty());
        this.inclIDPatient.setCellValueFactory(cellData -> cellData.getValue().idPatientProperty());
        this.inclDiagnostic.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList(Diag.NULL, Diag.BASO, Diag.SPINO, Diag.KERATOSE, Diag.AUTRE, Diag.FICHIER, Diag.RIEN));
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

        this.idPatientField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.idPatientField.getText().length() >= 4)
                    this.idPatientField.setText(this.idPatientField.getText().substring(0, 4));
        });

        this.resList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (this.resList.getSelectionModel().getSelectedItem() != null)
                this.procList.getSelectionModel().clearSelection();

            this.displayRemoveDownloadButtons();
            click.consume();
        });

        this.procList.addEventFilter(MouseEvent.MOUSE_CLICKED, click -> {
            if (this.procList.getSelectionModel().getSelectedItem() != null)
                this.resList.getSelectionModel().clearSelection();

            this.displayRemoveDownloadButtons();
            click.consume();
        });

        this.inclusionsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            this.selectedInclusion = this.inclusionsTable.getSelectionModel().getSelectedItem();
            this.enableButtons(this.selectedInclusion != null, false);
        });

        this.procList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
        this.resList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> this.displayRemoveDownloadButtons());
    }

    public void enableButtons(boolean enable, boolean all) {
        this.removeButton.setDisable(!enable);
        this.editButton.setDisable(!enable);
        this.lesionsButton.setDisable(!enable);

        if (this.selectedInclusion != null) {
            if (!this.selectedInclusion.getReference1().equals("Aucun") || !this.selectedInclusion.getReference2().equals("Aucun"))
                this.refDownloadButton.setDisable(!enable);
            else this.refDownloadButton.setDisable(true);
        }

        if (all) {
            this.addButton.setDisable(!enable);
            this.searchButton.setDisable(!enable);
            this.searchAllButton.setDisable(!enable);
            this.searchDocButton.setDisable(!enable);
            this.docDownloadButton.setDisable(!enable);
            this.removeDocButton.setDisable(!enable);
            this.importProcButton.setDisable(!enable);
            this.importResultButton.setDisable(!enable);
            this.selectAllDocsButton.setDisable(!enable);
        }
    }

    @FXML
    private void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer l'inclusion et ce qui y est attaché");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            this.enableButtons(true, true);
            this.remove();
            this.inclusionsList.remove(this.selectedInclusion);
            this.inclusionsTable.getSelectionModel().clearSelection();
            this.enableButtons(false, true);
        } else alert.close();
    }

    private void remove() {
        ArrayList<Lesion> lesionsToRemove = LesionDaoImpl.removeLesions(this.selectedInclusion.getId());
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.removeButton, null, this.progressBar, this.progressLabel);
        InclusionsController.removeFTPSQL(removeTask, this.selectedInclusion);

        if (lesionsToRemove.size() != 0)
            LesionsController.remove(removeTask, lesionsToRemove);
        else new Thread(removeTask).start();
    }

    private static void removeFTPSQL(RemoveTask removeTask, Inclusion inclusion) {
        InclusionsController.removeFTP(removeTask, inclusion);
        InclusionsController.removeSQL(inclusion);
    }

    private static void removeSQL(Inclusion inclusion) {
        InclusionDaoImpl.delete(Integer.parseInt(inclusion.getId()));
    }

    private static void removeFTP(RemoveTask removeTask, Inclusion inclusion) {
        ArrayList<String> urls = new ArrayList<>();
        String reference1Url, reference2Url;

        if (!(reference1Url = inclusion.getReference1()).equals("Aucun"))
            urls.add(reference1Url);

        if (!(reference2Url = inclusion.getReference2()).equals("Aucun"))
            urls.add(reference2Url);

        removeTask.addUrls(urls);
    }

    @FXML
    private void refDownloadAction() {
        ArrayList<String> refs = new ArrayList<>();
        String ref1Url, ref2Url;

        if (!(ref1Url = this.selectedInclusion.getReference1()).equals("Aucun"))
            refs.add(ref1Url);

        if (!(ref2Url = this.selectedInclusion.getReference2()).equals("Aucun"))
            refs.add(ref2Url);

        this.refDownloadButton.setVisible(false);
        this.startDownload(refs, this.refDownloadButton);
    }

    private void populateDocs(ObservableList<String> res, ObservableList<String> proc) {
        if (!res.isEmpty())
            this.resList.setItems(this.resObservableList);
        else this.resList.setItems(FXCollections.observableArrayList());
        this.resList.refresh();

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
        this.resList.setItems(this.resObservableList);
    }

    @FXML
    private void importProcAction() {
        this.startUpload("proc", this.importProcButton, FileManager.getProcDirectoryName());
    }

    @FXML
    private void importResultAction() {
        this.startUpload("res", this.importResultButton, FileManager.getResDirectoryName());
    }

    @FXML
    private void docDownloadAction() {
        if (this.selectedDoc != null)
            this.startDocDownload(this.selectedDoc, this.docDownloadButton, progressDocBar, progressDocLabel);
    }

    @FXML
    private void removeDocAction() {
        if (this.selectedDoc != null) {
            RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.removeDocButton, this.docDownloadButton, this.progressDocBar, this.progressDocLabel);

            removeTask.addUrls(new ArrayList<String>() {{
                add(selectedDoc);
            }});

            new Thread(removeTask).start();
        }
    }

    public void endRemove() {
        this.resObservableList = this.fileManager.listFiles(FileManager.getResDirectoryName(), true, false);
        this.procObservableList = this.fileManager.listFiles(FileManager.getProcDirectoryName(), false, true);
        this.populateDocs(this.resObservableList, this.procObservableList);
        this.enableButtons(true, true);
        this.docDownloadButton.setVisible(true);
        this.progressDocBar.setVisible(false);
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
        this.setStage(this.editButton);

        new AddInclusionsView(this.stage, this, this.inclusionsList, this.selectedInclusion, this.inclusionDaoImpl, this.connection, this.fileManager);
    }

    @FXML
    private void addAction() {
        this.setStage(this.addButton);

        new AddInclusionsView(this.stage, this, this.inclusionsList, null, this.inclusionDaoImpl, this.connection, this.fileManager);
    }

    @FXML
    private void lesionsAction() {
        this.setStage(this.lesionsButton);

        new LesionsView(this.stage, this.connection, this.fileManager, this.selectedInclusion);

        this.setStage(this.lesionsButton);
        this.stage.close();
    }

    private void startUpload(String buttonName, Button button, String directory) {
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, null);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressDocBar.progressProperty().bind(uploadTask.progressProperty());
        this.progressDocBar.setVisible(true);
        this.docDownloadButton.setVisible(false);
        uploadTask.setOnSucceeded(e -> this.endUpload(buttonName, uploadTask.getAddedFileName(), null, 0));
        uploadTask.setOnFailed(e -> this.endUpload(buttonName, uploadTask.getAddedFileName(), null, 0));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    @Override
    void endUpload(String listName, String addedFileName, Label label, int num) {
        this.progressDocBar.setVisible(false);
        this.docDownloadButton.setVisible(true);

        if (addedFileName != null) {
            switch (listName) {
                case "proc":
                    this.populateProcDoc(addedFileName);
                    break;
                case "res":
                    this.populateResDoc(addedFileName);
                    break;
            }
        }

        this.enableButtons(true, true);
    }
}
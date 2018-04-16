package src.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.table.Inclusion;
import src.utils.Diag;
import src.utils.FileManager;
import src.view.AddInclusionsView;

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
    ComboBox diagnosticChoiceBox;
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
    Button downloadDocButton;
    @FXML
    Button removeDocButton;
    @FXML
    Button importProcButton;
    @FXML
    Button importResultButton;
    @FXML
    ListView procList;
    @FXML
    ListView resList;
    @FXML
    TableView inclusionsTable;
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
    private Stage inclusionsStage;
    private InclusionDaoImpl inclusionDaolmpl;
    private ObservableList<Inclusion> inclusionsList;
    private Inclusion selectedInclusion;
    private String selectedDoc;

    public InclusionsController(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.inclID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.inclDate.setCellValueFactory(cellData -> cellData.getValue().dateInclusionProperty().asString());
        this.inclAnapath.setCellValueFactory(cellData -> cellData.getValue().numAnaPathProperty().asObject());
        this.inclInitials.setCellValueFactory(cellData -> cellData.getValue().initialesPatientProperty());
        this.inclDiagnostic.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList(Diag.BASO, Diag.SPINO, Diag.KERATOSE, Diag.AUTRE, Diag.FICHIER, Diag.RIEN));

        procList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> selectedDoc = newValue);
        resList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> selectedDoc = newValue);

        this.inclusionDaolmpl = new InclusionDaoImpl(connection);
        inclusionsList = inclusionDaolmpl.selectAll();

        this.populateInclusions(inclusionsList);
    }

    private void populateInclusions(ObservableList<Inclusion> inclusions) {
        if(!inclusions.isEmpty())
            inclusionsTable.setItems(inclusions);
        else inclusionsTable.refresh();
    }

    @FXML
    private void searchAction(ActionEvent actionEvent) {
        inclusionsList = inclusionDaolmpl.selectByFilters(this.idInclusionField.getText().equals("") ? 0 : Integer.getInteger(this.idInclusionField.getText()),
                this.inclusionDatePicker.getValue() == null ? null : Date.valueOf(this.inclusionDatePicker.getValue()),
                this.idAnapathField.getText().equals("") ? 0 : Integer.getInteger(this.idAnapathField.getText()),
                this.initialesField.getText(),
                this.diagnosticChoiceBox.getValue() == null ? null : (Diag)this.diagnosticChoiceBox.getValue());

        this.populateInclusions(inclusionsList);
    }

    @FXML
    private void searchAllAction(ActionEvent actionEvent) {
        inclusionsList = inclusionDaolmpl.selectAll();
        this.populateInclusions(inclusionsList);
    }

    @FXML
    private void removeAction(ActionEvent actionEvent) {
        inclusionDaolmpl.delete(Integer.getInteger(idInclusionField.getText()));
        inclusionsList.remove(selectedInclusion);
        this.populateInclusions(this.inclusionsList);
    }

    @FXML
    private void importProcAction(ActionEvent actionEvent) {
        FileManager.uploadToURL(inclusionsStage, "procedures");
    }

    @FXML
    private void importResultAction(ActionEvent actionEvent) {
        FileManager.uploadToURL(inclusionsStage, "resultats");
    }

    @FXML
    private void downloadDocAction(ActionEvent actionEvent) {
        this.inclusionsStage = (Stage) downloadDocButton.getScene().getWindow();
        FileManager.downloadFromUrl(this.inclusionsStage, this.selectedDoc);
    }

    @FXML
    private void removeDocAction(ActionEvent actionEvent) {
        FileManager.removeFile(this.selectedDoc);
    }

    @FXML
    private void searchDocAction(ActionEvent actionEvent) {
        FileManager.searchFile();
    }

    @FXML
    private void editAction(ActionEvent actionEvent) {
        this.inclusionsStage = (Stage) editButton.getScene().getWindow();
        new AddInclusionsView(this.inclusionsStage, this.selectedInclusion, this.connection);
    }

    @FXML
    private void refDownloadAction(ActionEvent actionEvent) {

    }

    @FXML
    private void addAction(ActionEvent actionEvent) {
        this.inclusionsStage = (Stage) editButton.getScene().getWindow();
        new AddInclusionsView(this.inclusionsStage, this.selectedInclusion, this.connection);
    }
}
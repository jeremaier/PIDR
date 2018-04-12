package src.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.table.Inclusion;
import src.utils.Diag;

import java.net.URL;
import java.util.Date;
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
    ChoiceBox diagnosticChoiceBox;

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
    TableColumn<Inclusion, Integer> inclID;

    @FXML
    TableColumn<Inclusion, Date>  inclDate;

    @FXML
    TableColumn<Inclusion, Integer> inclAnapath;

    @FXML
    TableColumn<Inclusion, String> inclInitials;

    @FXML
    TableColumn<Inclusion, Diag> inclDiagnostic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.diagnosticChoiceBox.setItems(FXCollections.observableArrayList("Baso-cellulaire", "Spino-cellulaire", "Kératose-actinique", "Pas de malignité", "Autre", "Fichier"));
    }
}
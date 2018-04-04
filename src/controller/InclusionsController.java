package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
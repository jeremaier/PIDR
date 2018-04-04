package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddAnalyseController implements Initializable {
    @FXML
    TextField RNAField;

    @FXML
    TextField concentrationField;

    @FXML
    TextField ARNcField;

    @FXML
    TextField cyanineField;

    @FXML
    TextField rendementField;

    @FXML
    TextField serialNumberField;

    @FXML
    TextArea specificActivityArea;

    @FXML
    TextArea exclusionCriterionArea;

    @FXML
    Button addRawFileButton;

    @FXML
    Button addCutFileButton;

    @FXML
    Button addDQRButton;

    @FXML
    Label rawFileLabel;

    @FXML
    Label cutFileLabel;

    @FXML
    Label DQRLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
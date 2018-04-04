package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddInclusionController implements Initializable {
    @FXML
    TextField inclusionIDField;

    @FXML
    TextField idAnapathField;

    @FXML
    DatePicker inclusionDatePicker;

    @FXML
    Button addPatientButton;

    @FXML
    Button reference1FileButton;

    @FXML
    Button reference2FileButton;

    @FXML
    Label patientLabel;

    @FXML
    Label reference1FileLabel;

    @FXML
    Label reference2FileLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
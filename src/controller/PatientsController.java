package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientsController implements Initializable {
    @FXML
    TextField initialesField;

    @FXML
    ComboBox genderComboBox;

    @FXML
    DatePicker birthDatePicker;

    @FXML
    Button addPatientButton;

    @FXML
    Button chooseButton;

    @FXML
    Button removeButton;

    @FXML
    Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
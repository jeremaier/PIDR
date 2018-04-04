package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AddLameController implements Initializable {
    @FXML
    ChoiceBox lamellaChoiceBox;

    @FXML
    ChoiceBox cutAreaChoiceBox;

    @FXML
    ChoiceBox blackOrientationChoiceBox;

    @FXML
    ChoiceBox greenOrientationChoiceBox;

    @FXML
    Button addPictureButton;

    @FXML
    Button cancelButton;

    @FXML
    Button addButton;

    @FXML
    Label photoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
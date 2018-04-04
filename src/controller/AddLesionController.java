package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddLesionController implements Initializable {

    @FXML
    TextField siteAnatomique;

    @FXML
    ChoiceBox diag;

    @FXML
    Button autre;

    @FXML
    Label checkDiag;

    @FXML
    Button ajoutDiag;

    @FXML
    Label labelDiag;

    @FXML
    Button ajoutSpectre;

    @FXML
    Label labelSpectre;

    @FXML
    Button annule;

    @FXML
    Button ajouter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

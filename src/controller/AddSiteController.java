package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSiteController implements Initializable {

    @FXML
    TextField siteAnatomique;

    @FXML
    CheckBox sain;

    @FXML
    TextField orientation;

    @FXML
    ChoiceBox diag;

    @FXML
    Button aurteDiag;

    @FXML
    Button addFicherDiag;

    @FXML
    Label checkFichierDiag;

    @FXML
    Button addFichierSpectre;

    @FXML
    Label checkFichierSpectre;

    @FXML
    Button addFichierMoy;

    @FXML
    Label checkFichierMoy;

    @FXML
    Button annuler;

    @FXML
    Button ajouter;


    public AddSiteController() {
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

}

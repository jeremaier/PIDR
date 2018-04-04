package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class siteController implements Initializable {
    @FXML
    Button retour;

    @FXML
    Button ajouter;

    @FXML
    Button supprimer;

    @FXML
    Button modifier;

    @FXML
    ListView spectreList;

    @FXML
    Button suprSpectre;

    @FXML
    Button fichierMoy;

    @FXML
    Button transcriptomique;

    @FXML
    TableView affecteTab;

    @FXML
    TableView sainTab;

    @FXML
    Tab affecte;

    @FXML
    Tab sain;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}

package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.SiteCutaneDaompl;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.SiteCutane;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddSiteController implements Initializable {

    @FXML
    ComboBox<SiteCutane> siteCutane;

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

    private Connection connection;
    private Stage addSiteStage;
    private FileManager fileManager;
    private CutaneousSite site;
    private Lesion lesion;
    private SiteCutaneDaompl siteCutaneDaompl;
    private ObservableList<SiteCutane> ComboBoxValeur = FXCollections.observableArrayList(SiteCutane.L,SiteCutane.PL,SiteCutane.NL);




    public AddSiteController(CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        this.connection=connection;
        this.fileManager=fileManager;
        this.site=site;
        this.lesion=lesion;
    }

    public void initialize(URL location, ResourceBundle resources) {

        sain.selectedProperty().addListener(observable -> {
            if(sain.isSelected()) {
                siteCutane.setDisable(true);
                orientation.setDisable(true);
            }

        });

        this.siteCutaneDaompl=new SiteCutaneDaompl(connection);
    }

    @FXML
    private void cancelButtonAction(ActionEvent actionEvent){
        this.addSiteStage = (Stage) addSiteStage.getScene().getWindow();
        this.addSiteStage.close();

    }

    @FXML
    private void accepteButtonAction(ActionEvent actionEvent){
        SiteCutane SITE;
        

    }



}

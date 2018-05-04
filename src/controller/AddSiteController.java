package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.SiteCutaneDaoImpl;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.Diag;
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
    ComboBox<Diag> diag;

    @FXML
    TextField autreDiag;

    @FXML
    Button addFicherDiag;

    @FXML
    Label checkFichierDiag;

    @FXML
    Button addFichierSpectre;

    @FXML
    Label checkFichierSpectre;


    @FXML
    Button annuler;

    @FXML
    Button ajouter;

    @FXML
    TextField numMesur;

    private Connection connection;
    private Stage addSiteStage;
    private FileManager fileManager;
    private CutaneousSite site;
    private Lesion lesion;
    private SiteCutaneDaoImpl siteCutaneDaoImpl;
    private ObservableList<SiteCutane> siteValeur = FXCollections.observableArrayList(SiteCutane.L, SiteCutane.PL, SiteCutane.NL);
    private ObservableList<Diag> diagValeur = FXCollections.observableArrayList(Diag.BASO, Diag.FICHIER, Diag.KERATOSE, Diag.SPINO, Diag.RIEN);
    private String fichierDiagPath;
    private String spectrePath;
    private int healthy;


    public AddSiteController(CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.site = site;
        this.lesion = lesion;
    }

    public void initialize(URL location, ResourceBundle resources) {
        siteCutane.setItems(siteValeur);
        diag.setItems(diagValeur);

        sain.selectedProperty().addListener(observable -> {
            if (sain.isSelected()) {
                siteCutane.setDisable(true);
                orientation.setDisable(true);
                healthy = 1;
            } else {
                siteCutane.setDisable(false);
                orientation.setDisable(false);
                healthy = 0;
            }

        });

        numMesur.lengthProperty().addListener((observable, oldValue, newValue)->{
            if(numMesur.getText().length()>0){
                addFichierSpectre.setDisable(false);
            }else{
                addFichierSpectre.setDisable(true);
            }
        });


        this.siteCutaneDaoImpl = new SiteCutaneDaoImpl(connection);
    }

    @FXML
    private void cancelButtonAction(ActionEvent actionEvent) {
        this.addSiteStage = (Stage) addSiteStage.getScene().getWindow();
        this.addSiteStage.close();

    }

    @FXML
    private void accepteButtonAction(ActionEvent actionEvent) {
        String SITE;
        int Orientation;
        String diagnostique;
        String AutreDiag;


        if (site == null) {
            SITE = siteCutane.getSelectionModel().getSelectedItem().toString();
            Orientation = Integer.parseInt(orientation.getText());
            diagnostique = diag.getSelectionModel().getSelectedItem().toString();
            AutreDiag = autreDiag.getText();


            CutaneousSite newSite = new CutaneousSite(lesion.getId(), healthy, SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, spectrePath);
            siteCutaneDaoImpl.insert(newSite);
        } else {
            if (siteCutane.getSelectionModel().getSelectedItem() != null) {
                SITE = siteCutane.getSelectionModel().getSelectedItem().toString();
            } else {
                SITE = site.getSite();
            }

            if (orientation.getText().length() > 0) {
                Orientation = Integer.parseInt(orientation.getText());
            } else {
                Orientation = site.getOrientation();
            }

            if (diag.getSelectionModel().getSelectedItem() != null) {
                diagnostique = diag.getSelectionModel().getSelectedItem().toString();
            } else {
                diagnostique = site.getDiag();
            }

            if (autreDiag.getText().length() > 0 && diag.getSelectionModel().getSelectedItem() == null) {
                AutreDiag = autreDiag.getText();
            } else if (autreDiag.getText().length() == 0 && diag.getSelectionModel().getSelectedItem() != null) {
                AutreDiag = null;
            } else {
                AutreDiag = site.getAutreDiag();
            }

            if (fichierDiagPath == null)
                fichierDiagPath = site.getFichierDiag();


            if (spectrePath == null)
                spectrePath = site.getSpectre();

            CutaneousSite newSite = new CutaneousSite(lesion.getId(), healthy, SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, spectrePath);
            siteCutaneDaoImpl.update(newSite, site.getId());
        }

    }

    @FXML
    private void fichierDiag(ActionEvent actionEvent) {
        String fileName = fileManager.uploadToURL(addSiteStage, "//siteCutane//", null);
        fichierDiagPath = "//siteCutane//" + fileName;
        checkFichierDiag.setText(fileName);
    }

    @FXML
    private void spectreButtonAction(ActionEvent actionEvent) {

        String fileName = fileManager.uploadToURL(addSiteStage, "//siteCutane//", numMesur.getText() );
        fichierDiagPath = "//siteCutane//" + fileName;
        checkFichierDiag.setText(fileName);
    }


}

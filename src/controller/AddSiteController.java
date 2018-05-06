package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.daoImpl.SiteCutaneDaoImpl;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;
import src.utils.SiteCutane;
import src.utils.UploadTask;
import src.view.SiteView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddSiteController extends Controller implements Initializable {
    @FXML
    ComboBox<SiteCutane> siteCutane;

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

    private CutaneousSite site;
    private Lesion lesion;
    private SiteCutaneDaoImpl siteCutaneDaoImpl;
    private ObservableList<SiteCutane> siteValeur = FXCollections.observableArrayList(SiteCutane.SAIN, SiteCutane.NULL, SiteCutane.L, SiteCutane.PL, SiteCutane.NL);
    private ObservableList<Diag> diagValeur = FXCollections.observableArrayList(Diag.BASO, Diag.FICHIER, Diag.KERATOSE, Diag.SPINO, Diag.RIEN);
    private String fichierDiagPath = null;
    private String spectrePath = null;
    private SiteController siteController;

    public AddSiteController(SiteController siteController, CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.site = site;
        this.lesion = lesion;
        this.siteController = siteController;
    }

    public void initialize(URL location, ResourceBundle resources) {
        siteCutane.getItems().addAll(this.siteValeur);
        diag.getItems().addAll(this.diagValeur);

        siteCutane.itemsProperty().addListener(observable -> {
            if (siteCutane.getValue().equals(SiteCutane.SAIN))
                this.enableButtons(false, false);
            else this.enableButtons(true, false);
        });

        numMesur.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (numMesur.getText().length() > 0)
                addFichierSpectre.setDisable(false);
            else addFichierSpectre.setDisable(true);
        });


        this.siteCutaneDaoImpl = new SiteCutaneDaoImpl(connection);
    }

    @FXML
    private void cancelButtonAction() {
        this.setStage(this.annuler);

        this.stage.close();
    }

    @FXML
    private void accepteButtonAction() {
        String SITE = SiteCutane.NULL.toString();
        Integer Orientation = 0;
        String diagnostique = Diag.NULL.toString();
        String AutreDiag;

        if (site == null) {
            if (siteCutane.getValue() != null)
                SITE = siteCutane.getValue().toString();

            if (orientation.getText().length() > 0)
                Orientation = Integer.parseInt(orientation.getText());

            if (siteCutane.getValue() != null)
                diagnostique = diag.getValue().toString();

            AutreDiag = autreDiag.getText();

            CutaneousSite newSite = new CutaneousSite(lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, spectrePath);
            this.siteController.populateSingleSite(newSite);
            siteCutaneDaoImpl.insert(newSite);
        } else {
            SITE = siteCutane.getSelectionModel().getSelectedItem() != null ? siteCutane.getSelectionModel().getSelectedItem().toString() : site.getSite();

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
                if (this.site.getFichierDiag() != null)
                    fichierDiagPath = site.getFichierDiag();
                else
                    fichierDiagPath = null;


            if (spectrePath == null)
                if (this.site.getSpectre() != null)
                    spectrePath = site.getSpectre();
                else
                    spectrePath = null;
            else if (this.site.getSpectre() != null)
                spectrePath = spectrePath + "~#" +  numMesur.getText() + "="+ site.getSpectre();

            System.out.println(spectrePath);
            CutaneousSite newSite = new CutaneousSite(lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, spectrePath);
            newSite.setFichierDiag(fichierDiagPath);
            newSite.setSpectre(spectrePath);

            siteCutaneDaoImpl.update(newSite, site.getId());
            this.siteController.refreshSite();
        }

        this.setStage(this.ajouter);
        this.stage.close();
    }


    @FXML
    private void fichierDiag() {
        this.startUpload(this.addFichierSpectre, checkFichierDiag, "//siteCutane//", null, 1);
    }

    @FXML
    private void spectreButtonAction() {
        if (numMesur.getText().length() > 0) {
            this.startUpload(this.addFichierSpectre, checkFichierSpectre, "//siteCutane//", numMesur.getText(), 2);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Il n'y a pas de numéro de mesure précisé");
            alert.showAndWait();
        }
    }


    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.siteCutane.setDisable(!enable);
        this.orientation.setDisable(!enable);
    }

    private void startUpload(Button button, Label label, String directory, String mesure, int num) {
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, mesure);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        uploadTask.setOnSucceeded(e -> this.endUpload(uploadTask.getAddedFileName(), directory, label, num));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    private void endUpload(String addedFileName, String directory, Label label, int num) {
        if (addedFileName != null) {
            if (num == 1)
                this.fichierDiagPath = directory + addedFileName;
            else

            this.spectrePath = directory + addedFileName;
            System.out.println("ccoucou "+spectrePath);


            label.setText(addedFileName);
        }

        this.enableButtons(true, true);
    }
}

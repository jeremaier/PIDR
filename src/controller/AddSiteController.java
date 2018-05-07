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
import src.utils.RemoveTask;
import src.utils.SiteCutane;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ObservableList<SiteCutane> siteValeur = FXCollections.observableArrayList(SiteCutane.NULL, SiteCutane.SAIN, SiteCutane.L, SiteCutane.PL, SiteCutane.NL);
    private ObservableList<Diag> diagValeur = FXCollections.observableArrayList(FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(Diag.values()))));
    private String fichierDiagPath = null;
    private String fichierMoyPath = null;
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

        if (site != null) {


            this.siteCutane.getSelectionModel().select(this.site.getSite());

            this.orientation.setText(Integer.toString(this.site.getOrientation()));
            this.diag.getSelectionModel().select(this.site.getDiag());
            this.autreDiag.setText(this.site.getAutreDiag());

            if (this.site.getFichierDiag() != null)
                this.checkFichierDiag.setText(this.site.getFichierDiag());
            else this.checkFichierDiag.setText("Aucun");

            /*if (this.site.getFichierDiag() != null)
                this.fichier.setText(this.site.getFichierMoy());
            else this.checkFichierDiag.setText("Aucun");*/

            if (this.site.getSpectre() != null)
                this.checkFichierSpectre.setText("Non vide");
        }

        siteCutane.itemsProperty().addListener(observable -> this.enableButtons(!siteCutane.getValue().equals(SiteCutane.SAIN), false));
        numMesur.lengthProperty().addListener((observable, oldValue, newValue) -> addFichierSpectre.setDisable(numMesur.getText().length() <= 0));

        checkFichierDiag.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Aucun")) {
                addFicherDiag.setText("Supprimer");
            }
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
            if (siteCutane.getValue() != null) {
                SITE = siteCutane.getValue().toString();
                diagnostique = diag.getValue().toString();
            }

            if (orientation.getText().length() > 0)
                Orientation = Integer.parseInt(orientation.getText());

            AutreDiag = autreDiag.getText();

            CutaneousSite newSite = new CutaneousSite(lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, fichierMoyPath, spectrePath);
            this.siteController.populateSingleSite(newSite);
            siteCutaneDaoImpl.insert(newSite);
        } else {
            SITE = siteCutane.getSelectionModel().getSelectedItem() != null ? siteCutane.getSelectionModel().getSelectedItem().toString() : site.getSite().toString();
            Orientation = orientation.getText().length() > 0 ? Integer.parseInt(orientation.getText()) : site.getOrientation();
            diagnostique = diag.getSelectionModel().getSelectedItem() != null ? diag.getSelectionModel().getSelectedItem().toString() : site.getDiag().toString();

            if (autreDiag.getText().length() > 0 && diag.getSelectionModel().getSelectedItem() == null)
                AutreDiag = autreDiag.getText();
            else
                AutreDiag = autreDiag.getText().length() == 0 && diag.getSelectionModel().getSelectedItem() != null ? null : site.getAutreDiag();

            if (fichierDiagPath == null)
                fichierDiagPath = site.getFichierDiag();

            if (fichierMoyPath == null)
                fichierMoyPath = site.getFichierMoy();

            if (spectrePath == null)
                spectrePath = site.getSpectre();
            else if (this.site.getSpectre() != null)
                spectrePath = spectrePath + "~#" + site.getSpectre();

            CutaneousSite newSite = new CutaneousSite(lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, fichierDiagPath, fichierMoyPath, spectrePath);
            newSite.setFichierDiag(fichierDiagPath);
            newSite.setFichierMoy(fichierMoyPath);
            newSite.setSpectre(spectrePath);

            this.siteCutaneDaoImpl.update(newSite, site.getId());
            this.siteController.refreshSite();
        }

        this.setStage(this.ajouter);
        this.stage.close();
    }

    @FXML
    private void fichierDiag() {
        if (site != null) {
            if (this.checkFichierDiag.equals("Aucun"))
                this.startUpload(this.addFichierSpectre, checkFichierDiag, "//siteCutane//" + Integer.toString(this.site.getId()), null, 1);
            else {
                this.removeFileFromFTP(this.addFicherDiag, this.checkFichierDiag);
            }
        } else if (checkFichierDiag.getText().equals("Aucun"))
            this.startUpload(this.addFichierSpectre, checkFichierDiag, "//siteCutane//" + Integer.toString(siteCutaneDaoImpl.getLastid() + 1), null, 1);
        else this.removeFileFromFTP(this.addFicherDiag, this.checkFichierDiag);


    }

    private void removeFileFromFTP(Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        removeTask.addUrls(new ArrayList<String>() {{
            add(site.getFichierDiag());
        }});

        removeTask.setOnSucceeded(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        removeTask.setOnFailed(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        this.siteCutaneDaoImpl.update(site, site.getId());
        new Thread(removeTask).start();

    }

    @FXML
    private void spectreButtonAction() {
        if (numMesur.getText().length() > 0)
            if (this.site == null)
                this.startUpload(this.addFichierSpectre, checkFichierSpectre, "//siteCutane//" + Integer.toString(siteCutaneDaoImpl.getLastid() + 1), numMesur.getText(), 2);
            else
                this.startUpload(this.addFichierSpectre, checkFichierDiag, "//siteCutane//" + Integer.toString(this.site.getId()), null, 2);

        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Il n'y a pas de num�ro de mesure pr�cis�");
            alert.showAndWait();
        }
    }


    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.siteCutane.setDisable(!enable);
        this.orientation.setDisable(!enable);
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
        if (addedFileName != null) {
            if (num == 1)
                this.fichierDiagPath = directory + addedFileName;
            else this.spectrePath = directory + numMesur.getText() + "=" + addedFileName;

            label.setText(addedFileName);
        }

        this.enableButtons(true, true);
    }
}

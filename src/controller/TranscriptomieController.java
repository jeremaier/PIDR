package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.daoImpl.LesionDaoImpl;
import src.daoImpl.SiteCutaneDaoImpl;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.Lesion;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.view.AddTranscriptomieView;
import src.view.SiteView;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class TranscriptomieController extends Controller implements Initializable {
    @FXML
    Label ID;

    @FXML
    Label emplacement;

    @FXML
    Label numSerie;

    @FXML
    Label concentration;

    @FXML
    Label ARNc;

    @FXML
    Label cy3;

    @FXML
    Label rendement;

    @FXML
    Label activitesSpec;

    @FXML
    Label critExclusion;

    @FXML
    Label RIN;

    @FXML
    Button fichierBrut;

    @FXML
    Button qualityReport;

    @FXML
    Button retour;

    @FXML
    Button modifier;

    @FXML
    Button ajouter;

    @FXML
    Button supprimer;

    private Connection connection;
    private Stage transcriptomieStage;
    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private FileManager fileManager;
    private int siteId;

    public TranscriptomieController(Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.fileManager = fileManager;
        this.siteId = siteId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        this.display(this.transcriptomicAnalysis);


        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);

    }

    public void display(TranscriptomicAnalysis transcriptomicAnalysis) {
        if (transcriptomicAnalysis != null) {

            this.ID.setText(Integer.toString(transcriptomicAnalysis.getId()));
        } else {
            this.ID.setText("");
        }

        if (transcriptomicAnalysis != null) {
            this.emplacement.setText(Integer.toString(transcriptomicAnalysis.getLamellaLocation()));
        } else {
            this.emplacement.setText("");

        }

        if (transcriptomicAnalysis != null) {
            this.numSerie.setText(Integer.toString(transcriptomicAnalysis.getSerialNumber()));
        } else {
            this.numSerie.setText("");

        }

        if (transcriptomicAnalysis != null) {
            this.cy3.setText(Double.toString(transcriptomicAnalysis.getCyanine()));
        } else {
            this.cy3.setText("");

        }

        if (transcriptomicAnalysis != null) {
            this.rendement.setText(Double.toString(transcriptomicAnalysis.getYield()));
        } else {
            this.rendement.setText("");
        }


        if (transcriptomicAnalysis != null) {
            this.concentration.setText(Double.toString(transcriptomicAnalysis.getConcentration()));
        } else {
            this.concentration.setText("");
        }


        if (transcriptomicAnalysis != null) {
            this.critExclusion.setText(transcriptomicAnalysis.getExclusionCriteria());
        } else {
            this.critExclusion.setText("");
        }
        if (transcriptomicAnalysis != null) {
            this.activitesSpec.setText(transcriptomicAnalysis.getSpecificActivity());
        } else {
            this.activitesSpec.setText("");
        }


        if (transcriptomicAnalysis != null) {
            this.ARNc.setText(Double.toString(transcriptomicAnalysis.getARNC()));
        } else {
            this.ARNc.setText("");
        }

        if (transcriptomicAnalysis != null) {
            this.RIN.setText(Double.toString(transcriptomicAnalysis.getRIN()));
        } else {
            this.RIN.setText("");
        }

    }

    @FXML
    private void fichierBrutAction() {
        this.startDownload(this.transcriptomicAnalysis.getFichierBrut(), this.fichierBrut);
    }


    @FXML
    private void qualityReportAction() {
        this.startDownload(this.transcriptomicAnalysis.getFichierBrut(), this.qualityReport);
    }


    @FXML
    private void retour() {
        this.transcriptomieStage = (Stage) retour.getScene().getWindow();

        SiteCutaneDaoImpl siteCutaneDaompl = new SiteCutaneDaoImpl(connection);
        LesionDaoImpl lesionDaoImlp = new LesionDaoImpl(connection);

        Lesion lesion = lesionDaoImlp.selectById(siteCutaneDaompl.selectById(siteId).getIdLesion());

        new SiteView(lesion, connection, fileManager);

        this.transcriptomieStage.close();
    }

    @FXML
    private void updateButtonAction() {
        this.transcriptomicAnalysis=transcriptomieDaoImpl.selectBySite(siteId);

        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();
        if (this.transcriptomicAnalysis != null) {
            new AddTranscriptomieView(this, connection, fileManager, this.transcriptomicAnalysis, siteId);
        } else {
            JOptionPane.showMessageDialog(null, "Il n'y a pas analyse trascriptomique");
        }
    }

    @FXML
    private void addButtonAction() {
        this.transcriptomicAnalysis=transcriptomieDaoImpl.selectBySite(siteId);

        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();

        if (this.transcriptomicAnalysis == null) {
            new AddTranscriptomieView(this, connection, fileManager, null, siteId);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Il y a déjà une analyse trascriptomique enregistrée pour ce site cutané");
            alert.showAndWait();

        }
    }

    @FXML
    private void dellButtonAction() {
        this.transcriptomicAnalysis=transcriptomieDaompl.selectBySite(siteId);
        if (this.transcriptomicAnalysis != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une analyse transcriptomique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                TranscriptomieDaoImpl.delete(this.transcriptomicAnalysis.getId());
                this.transcriptomicAnalysis = null;
                display(transcriptomicAnalysis);
            } else {
                alert.close();
            }
        }
    }

    private void startDownload(String url, Button button) {
        this.startDownload(new ArrayList<String>() {{
            add(url);
        }}, button);
    }

    private void endDownload() {
        this.enableButtons(true, true);
        this.progressBar.setVisible(false);
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.fichierBrut.setDisable(!enable);
        this.qualityReport.setDisable(!enable);
        this.retour.setDisable(!enable);
        this.modifier.setDisable(!enable);
        this.ajouter.setDisable(!enable);
        this.supprimer.setDisable(!enable);
    }
}

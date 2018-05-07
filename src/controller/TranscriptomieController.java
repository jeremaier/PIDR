package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import src.daoImpl.LesionDaoImpl;
import src.daoImpl.SiteCutaneDaoImpl;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.RemoveTask;
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

    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private int siteId;

    public TranscriptomieController(Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.fileManager = fileManager;
        this.siteId = siteId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*TODO supprimer un bouton*/
        this.display(this.transcriptomicAnalysis);
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);
    }

    void display(TranscriptomicAnalysis transcriptomicAnalysis) {
        if (transcriptomicAnalysis != null) {
            this.ID.setText(Integer.toString(transcriptomicAnalysis.getId()));
            this.emplacement.setText(Integer.toString(transcriptomicAnalysis.getLamellaLocation()));
            this.numSerie.setText(Integer.toString(transcriptomicAnalysis.getSerialNumber()));
            this.cy3.setText(Double.toString(transcriptomicAnalysis.getCyanine()));
            this.rendement.setText(Double.toString(transcriptomicAnalysis.getYield()));
            this.concentration.setText(Double.toString(transcriptomicAnalysis.getConcentration()));
            this.critExclusion.setText(transcriptomicAnalysis.getExclusionCriteria());
            this.activitesSpec.setText(Double.toString(transcriptomicAnalysis.getSpecificActivity()));
            this.ARNc.setText(Double.toString(transcriptomicAnalysis.getARNC()));
            this.RIN.setText(Double.toString(transcriptomicAnalysis.getRIN()));
        } else {
            this.ID.setText("");
            this.emplacement.setText("");
            this.numSerie.setText("");
            this.cy3.setText("");
            this.rendement.setText("");
            this.concentration.setText("");
            this.critExclusion.setText("");
            this.activitesSpec.setText("");
            this.ARNc.setText("");
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
        this.setStage(this.retour);
        new SiteView(this.stage, new LesionDaoImpl(connection).selectById(new SiteCutaneDaoImpl(connection).selectById(siteId).getIdLesion()), connection, fileManager);

        this.stage.close();
    }

    @FXML
    private void updateButtonAction() {
        this.transcriptomicAnalysis = transcriptomieDaoImpl.selectBySite(siteId);
        this.setStage(this.modifier);

        if (this.transcriptomicAnalysis != null) {
            new AddTranscriptomieView(this.stage, this, connection, fileManager, this.transcriptomicAnalysis, siteId);
            this.stage.close();
        } else JOptionPane.showMessageDialog(null, "Il n'y a pas analyse trascriptomique");
    }

    @FXML
    private void addButtonAction() {
        this.transcriptomicAnalysis = transcriptomieDaoImpl.selectBySite(siteId);
        this.setStage(this.ajouter);

        if (this.transcriptomicAnalysis == null) {
            new AddTranscriptomieView(this.stage, this, connection, fileManager, null, siteId);
            this.stage.close();
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
        this.transcriptomicAnalysis = transcriptomieDaoImpl.selectBySite(siteId);
        if (this.transcriptomicAnalysis != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une analyse transcriptomique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                this.enableButtons(false, true);
                this.remove(new RemoveTask(this, this.fileManager).setParameters(this.supprimer, null, this.progressBar, this.progressLabel), transcriptomicAnalysis);
                this.transcriptomicAnalysis = null;
                this.display(null);
                this.enableButtons(true, true);
            } else alert.close();
        }
    }

    private void remove(RemoveTask removeTask, TranscriptomicAnalysis transcriptomicAnalysis) {
        TranscriptomieController.remove(removeTask, new ArrayList<TranscriptomicAnalysis>() {{
            add(transcriptomicAnalysis);
        }});
    }

    static void remove(RemoveTask task, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        TranscriptomieController.removeFTPSQL(task, transcriptomicAnalyses);

        new Thread(task).start();
    }

    private static void removeFTPSQL(RemoveTask removeTask, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        TranscriptomieController.removeFTP(removeTask, transcriptomicAnalyses);
        TranscriptomieController.removeSQL(transcriptomicAnalyses);
    }

    private static void removeSQL(ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        for (TranscriptomicAnalysis transcriptomicAnalysis : transcriptomicAnalyses)
            TranscriptomieDaoImpl.delete(transcriptomicAnalysis.getId());
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<TranscriptomicAnalysis> transcriptomicAnalyses) {
        ArrayList<String> urls = new ArrayList<>();

        for (TranscriptomicAnalysis transcriptomicAnalysis : transcriptomicAnalyses) {
            String fichierBrut, qualityReport;

            if ((fichierBrut = transcriptomicAnalysis.getFichierBrut()) != null)
                urls.add(fichierBrut);

            if ((qualityReport = transcriptomicAnalysis.getQualityReport()) != null)
                urls.add(qualityReport);
        }

        removeTask.addUrls(urls);
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

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }
}

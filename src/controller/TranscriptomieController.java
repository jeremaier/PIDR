package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.daoImpl.TranscriptomieDaompl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.view.AddTranscriptomieView;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

//import src.view.AddTranscriptomieView;

public class TranscriptomieController implements Initializable {


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
    Button fichierCut;

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
    private TranscriptomieDaompl transcriptomieDaompl;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private FileManager fileManager;
    private int siteId;

    public TranscriptomieController(Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.fileManager=fileManager;
        this.siteId=siteId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        this.ID.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.ID.setText(Integer.toString(this.transcriptomicAnalysis.getId()));
                    }
                }
        );

        this.emplacement.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.emplacement.setText(Integer.toString(this.transcriptomicAnalysis.getLamellaLocation()));
                    }
                }
        );

        this.numSerie.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.numSerie.setText(Integer.toString(this.transcriptomicAnalysis.getSerialNumber()));
                    }
                }
        );

        this.cy3.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.cy3.setText(Double.toString(this.transcriptomicAnalysis.getCyanine()));
                    }
                }
        );

        this.rendement.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.rendement.setText(Double.toString(this.transcriptomicAnalysis.getYield()));
                    }
                }
        );

        this.concentration.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.concentration.setText(Double.toString(this.transcriptomicAnalysis.getConcentration()));
                    }
                }
        );

        this.critExclusion.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.critExclusion.setText(this.transcriptomicAnalysis.getExclusionCriteria());
                    }
                }
        );

        this.activitesSpec.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.activitesSpec.setText(this.transcriptomicAnalysis.getSpecificActivity());
                    }
                }
        );

        this.ARNc.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.ARNc.setText(Double.toString(this.transcriptomicAnalysis.getARNC()));
                    }
                }
        );

        this.RIN.labelForProperty().addListener(observable -> {
                    if (transcriptomicAnalysis != null) {
                        this.RIN.setText(Double.toString(this.transcriptomicAnalysis.getRIN()));
                    }
                }
        );



        this.transcriptomieDaompl = new TranscriptomieDaompl(connection);

    }

    @FXML
    private void fichierBrutAction() {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) fichierBrut.getScene().getWindow();
        }

        this.fileManager.downloadFromUrl(transcriptomieStage,this.transcriptomicAnalysis.getFichierBrut(),null, true, true);
    }

    @FXML
    private void ficherCutAction() {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) fichierCut.getScene().getWindow();
        }

        this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getFichierCut(), null, true, true);
    }

    @FXML
    private void qualityReportAction() {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();
        }
        this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getQualityReport(), null, true, true);
    }


    @FXML
    private void retour() {
        this.transcriptomieStage = (Stage) transcriptomieStage.getScene().getWindow();
        this.transcriptomieStage.close();
    }

    @FXML
    private void updateButtonAction() {
        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();
        if (this.transcriptomicAnalysis != null) {
            new AddTranscriptomieView( connection, fileManager, this.transcriptomicAnalysis, siteId);
        } else {
            JOptionPane.showMessageDialog(null, "Il n'y a pas analyse trascriptomique");

        }
    }

    @FXML
    private void addButtonAction() {
        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();

        if (this.transcriptomicAnalysis == null) {
            new AddTranscriptomieView( connection, fileManager, null, siteId);
        } else {
            JOptionPane.showMessageDialog(null, "Il y a déjà une analyse trascriptomique");

        }
    }

    @FXML
    private void dellButtonAction() {
        if (this.transcriptomicAnalysis != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une analyse transcriptomique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                transcriptomieDaompl.delete(this.transcriptomicAnalysis.getId());
                this.transcriptomicAnalysis=null;

            } else {
                alert.close();
            }
        }
    }

    @FXML
    private void refresh(ActionEvent actionEvent){
        this.transcriptomicAnalysis=transcriptomieDaompl.selectById(siteId);
    }
}

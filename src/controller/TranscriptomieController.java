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

    public TranscriptomieController(Connection connection, TranscriptomicAnalysis transcriptomicAnalysis) {
        this.connection = connection;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ID.setText(Integer.toString(this.transcriptomicAnalysis.getId()));
        this.emplacement.setText(Integer.toString(this.transcriptomicAnalysis.getLamellaLocation()));
        this.numSerie.setText(Integer.toString(this.transcriptomicAnalysis.getSerialNumber()));
        this.cy3.setText(Double.toString(this.transcriptomicAnalysis.getCyanine()));
        this.rendement.setText(Double.toString(this.transcriptomicAnalysis.getYield()));
        this.concentration.setText(Double.toString(this.transcriptomicAnalysis.getConcentration()));
        this.critExclusion.setText(this.transcriptomicAnalysis.getExclusionCriteria());
        this.activitesSpec.setText(this.transcriptomicAnalysis.getSpecificActivity());
        this.ARNc.setText(Double.toString(this.transcriptomicAnalysis.getARNC()));
        this.RIN.setText(Double.toString(this.transcriptomicAnalysis.getRIN()));
        this.transcriptomieDaompl = new TranscriptomieDaompl(connection);
    }

    @FXML
    private void fichierBrutAction(ActionEvent actionEvent) {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) fichierBrut.getScene().getWindow();
        }

        //this.fileManager.downloadFromUrl(transcriptomieStage,this.transcriptomicAnalysis.getFichierBrut(), true);
    }

    @FXML
    private void ficherCutAction(ActionEvent actionEvent) {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) fichierCut.getScene().getWindow();
        }

        this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getFichierCut(), null, true);
    }

    @FXML
    private void qualityReportAction(ActionEvent actionEvent) {
        if (this.transcriptomieStage == null) {
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();
        }
        this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getQualityReport(), null, true);
    }


    @FXML
    private void retour(ActionEvent actionEvent) {
        this.transcriptomieStage = (Stage) transcriptomieStage.getScene().getWindow();
        this.transcriptomieStage.close();
    }

    @FXML
    private void updateButtonAction(ActionEvent actionEvent) {
        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();

        //new AddTranscriptomieView(transcriptomieStage, connection, fileManager, this.transcriptomicAnalysis, this.transcriptomicAnalysis.getIdCutaneousSite());
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent) {
        if (this.transcriptomieStage == null)
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();

        //new AddTranscriptomieView(transcriptomieStage, connection, fileManager, null, this.transcriptomicAnalysis.getIdCutaneousSite());
    }

    @FXML
    private void dellButtonAction(ActionEvent actionEvent) {
        if (this.transcriptomicAnalysis != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une analyse transcriptomique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                transcriptomieDaompl.delete(this.transcriptomicAnalysis.getId());

            } else {
                alert.close();
            }
        }
    }
}

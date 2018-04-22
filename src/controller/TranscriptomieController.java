package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import src.daoImpl.TranscriptomieDaompl;
import src.table.CutaneousSite;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;

import javax.management.relation.RoleInfoNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

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


    private Connection connection;
    private Stage transcriptomieStage;
    private TranscriptomieDaompl transcriptomieDaompl;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private FileManager fileManager;

    public TranscriptomieController(Connection connection, TranscriptomicAnalysis transcriptomicAnalysis){
        this.connection=connection;
        this.transcriptomicAnalysis=transcriptomicAnalysis;

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
        if ( this.transcriptomieStage== null) {
            this.transcriptomieStage = (Stage) fichierBrut.getScene().getWindow();
        }
        //openFTPConnection();
        //this.fileManager.downloadFromUrl(transcriptomieStage,this.transcriptomicAnalysis.getFichierBrut());
        //closeConnection();
    }

    @FXML
    private void ficherCutAction(ActionEvent actionEvent){
        if (this.transcriptomieStage == null){
            this.transcriptomieStage = (Stage) fichierCut.getScene().getWindow();
        }
        /**TODO C KOI LE BORDEL AVEC FILEMANAGEUR
        //openFTPConnection();
        //this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getFichierCut());
        //closeConnection();
         **/
    }

    @FXML
    private void qualityReportAction(ActionEvent actionEvent){
        if(this.transcriptomieStage == null){
            this.transcriptomieStage = (Stage) qualityReport.getScene().getWindow();
        }
        //this.fileManager.downloadFromUrl(transcriptomieStage, this.transcriptomicAnalysis.getQualityReport());
    }



    @FXML
    private void retour(ActionEvent actionEvent) {
        this.transcriptomieStage = (Stage) transcriptomieStage.getScene().getWindow();
        this.transcriptomieStage.close();
    }


}

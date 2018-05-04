package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.view.TranscriptomieView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddTransciptomieController implements Initializable {
    @FXML
    TextField id;

    @FXML
    TextField emplacement;

    @FXML
    TextField rendement;

    @FXML
    TextField concentration;

    @FXML
    TextField ARNc;

    @FXML
    TextField cy3;

    @FXML
    TextField numeroSerie;

    @FXML
    TextField activiteSpecifique;

    @FXML
    TextField critere;

    @FXML
    TextField RIN;

    @FXML
    Button fichierBrut;

    @FXML
    Button qualityReport;

    @FXML
    Label checkFichierBrut;

    @FXML
    Label checkQualityReport;

    @FXML
    Button accepte;

    @FXML
    Button cancel;


    private Connection connection;
    private Stage addTranscriptomieStage;
    private FileManager fileManager;
    private TranscriptomicAnalysis transcriptomicAnalysis;
    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private int siteId;
    private String fichierBrutPath;
    private String qualityReportPath;



    public AddTransciptomieController( Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.siteId = siteId;


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (transcriptomicAnalysis == null) {

            id.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() == 0) {
                    accepte.setDisable(true);
                    fichierBrut.setDisable(true);
                    qualityReport.setDisable(true);
                    fichierBrutPath=null;
                    qualityReportPath=null;

                } else {
                    accepte.setDisable(false);
                    fichierBrut.setDisable(false);
                    qualityReport.setDisable(false);

                }
            });
        }
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);
    }

    @FXML
    private void accepteButton(ActionEvent actionEvent) {
        int Id;
        int Emplacement;
        int NumSerie;
        Double Cy3;
        Double Rendement;
        Double Concentration;
        String Crit;
        String Activ;
        Double Arnc;
        int Rin;

        if (transcriptomicAnalysis == null) {
            Id = Integer.parseInt(id.getText());
            Emplacement = Integer.parseInt(emplacement.getText());
            NumSerie = Integer.parseInt(numeroSerie.getText());
            Cy3 = Double.parseDouble(cy3.getText());
            Rendement = Double.parseDouble(rendement.getText());
            Concentration = Double.parseDouble(concentration.getText());
            Crit = critere.getText();
            Activ = activiteSpecifique.getText();
            Arnc = Double.parseDouble(ARNc.getText());
            Rin = Integer.parseInt(RIN.getText());

            TranscriptomicAnalysis newTranscr = new TranscriptomicAnalysis(Id, siteId, fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            transcriptomieDaoImpl.insert(newTranscr);

            if (this.addTranscriptomieStage == null)
                this.addTranscriptomieStage = (Stage) accepte.getScene().getWindow();
            this.addTranscriptomieStage.close();

        } else {


            if (emplacement.getText().length() > 0) {
                Emplacement = Integer.parseInt(emplacement.getText());
            } else {
                Emplacement = transcriptomicAnalysis.getLamellaLocation();
            }

            if (numeroSerie.getText().length() > 0) {
                NumSerie = Integer.parseInt(numeroSerie.getText());
            } else {
                NumSerie = transcriptomicAnalysis.getSerialNumber();
            }

            if (cy3.getText().length() > 0) {
                Cy3 = Double.parseDouble(cy3.getText());
            } else {
                Cy3 = transcriptomicAnalysis.getCyanine();
            }

            if (rendement.getText().length() > 0) {
                Rendement = Double.parseDouble(rendement.getText());
            } else {
                Rendement = transcriptomicAnalysis.getYield();
            }

            if (concentration.getText().length() > 0) {
                Concentration = Double.parseDouble(concentration.getText());
            } else {
                Concentration = transcriptomicAnalysis.getConcentration();
            }

            if (critere.getText().length() > 0) {
                Crit = critere.getText();
            } else {
                Crit = transcriptomicAnalysis.getExclusionCriteria();
            }

            if (activiteSpecifique.getText().length() > 0) {
                Activ = activiteSpecifique.getText();
            } else {
                Activ = transcriptomicAnalysis.getSpecificActivity();
            }

            if (ARNc.getText().length() > 0) {
                Arnc = Double.parseDouble(ARNc.getText());
            } else {
                Arnc = transcriptomicAnalysis.getARNC();
            }

            if (RIN.getText().length() > 0) {
                Rin = Integer.parseInt(RIN.getText());
            } else {
                Rin = transcriptomicAnalysis.getRIN();
            }

            if (fichierBrutPath==null){
                fichierBrutPath=this.transcriptomicAnalysis.getFichierBrut();
            }

            if (qualityReportPath==null){
                qualityReportPath=this.transcriptomicAnalysis.getQualityReport();
            }

            TranscriptomicAnalysis newTranscr = new TranscriptomicAnalysis( transcriptomicAnalysis.getId(), siteId, fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            transcriptomieDaoImpl.update(newTranscr, transcriptomicAnalysis.getId());

            if (this.addTranscriptomieStage == null)
                this.addTranscriptomieStage = (Stage) accepte.getScene().getWindow();
            this.addTranscriptomieStage.close();
        }

    }

    @FXML
    private void fichierBrutButton(ActionEvent actionEvent) {
        String fileName;
        if (transcriptomicAnalysis != null) {
            fileName=fileManager.uploadToURL(addTranscriptomieStage, "//trancriptomie//" + String.valueOf(this.transcriptomicAnalysis.getId()), null);
            fichierBrutPath = "//trancriptomie//" + String.valueOf(this.transcriptomicAnalysis.getId()) +"//" + fileName ;
            checkFichierBrut.setText(fileName);

        }else {
            fileName=fileManager.uploadToURL(addTranscriptomieStage, "trancriptomie//" + id.getText(), null);
            fichierBrutPath = "//trancriptomie//" + id.getText() +"//" + fileName;
            checkFichierBrut.setText(fileName);
        }
    }


    @FXML
    private void qualityReportButtonAction(ActionEvent actionEvent){
        String fileName;
        if (transcriptomicAnalysis != null) {
            fileName=fileManager.uploadToURL(addTranscriptomieStage, "//trancriptomie//" + String.valueOf(this.transcriptomicAnalysis.getId()), null);
            qualityReportPath = "//trancriptomie//" + String.valueOf(this.transcriptomicAnalysis.getId()) +"//"+ fileName;
            checkQualityReport.setText(fileName);
        }else {
            fileName=fileManager.uploadToURL(addTranscriptomieStage, "trancriptomie//" + id.getText(), null);
            qualityReportPath = "//trancriptomie//" + id.getText() +"//"+ fileName;
            checkQualityReport.setText(fileName);
        }
    }

    @FXML
    private void retour(ActionEvent actionEvent) {
        this.addTranscriptomieStage = (Stage) cancel.getScene().getWindow();

        new TranscriptomieView(connection,fileManager,transcriptomicAnalysis,siteId);

        this.addTranscriptomieStage.close();
    }
}



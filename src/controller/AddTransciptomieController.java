package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.UploadTask;
import src.view.TranscriptomieView;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddTransciptomieController extends Controller implements Initializable {
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

    private TranscriptomicAnalysis transcriptomicAnalysis;
    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private int siteId;
    private String fichierBrutPath;
    private String qualityReportPath;
    private TranscriptomieController transcriptomieController;

    public AddTransciptomieController(TranscriptomieController transcriptomieController, Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.siteId = siteId;
        this.transcriptomieController = transcriptomieController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (transcriptomicAnalysis == null) {
            id.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() == 0) {
                    this.enableButtons(false, false);
                    fichierBrutPath = null;
                    qualityReportPath = null;
                } else this.enableButtons(false, false);
            });
        }

        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);
    }

    @FXML
    private void accepteButton() {
        TranscriptomicAnalysis newTranscr;
        int Id, Emplacement, NumSerie, Rin;
        Double Cy3, Rendement, Concentration, Arnc;
        String Crit, Activ;

        if (transcriptomicAnalysis == null) {
            if(id.getText().length()>0)
            Id = Integer.parseInt(id.getText());
            else Id=0;

            if(emplacement.getText().length()>0)
            Emplacement = Integer.parseInt(emplacement.getText());
            else Emplacement=0;

            if (numeroSerie.getText().length()>0)
            NumSerie = Integer.parseInt(numeroSerie.getText());
            else NumSerie=0;

            if (cy3.getText().length()>0)
            Cy3 = Double.parseDouble(cy3.getText());
            else Cy3=0.0;

            if(rendement.getText().length()>0)
            Rendement = Double.parseDouble(rendement.getText());
            else Rendement=0.0;

            if(concentration.getText().length()>0)
            Concentration = Double.parseDouble(concentration.getText());
            else Concentration=0.0;

            Crit = critere.getText();
            Activ = activiteSpecifique.getText();

            if(ARNc.getText().length()>0)
            Arnc = Double.parseDouble(ARNc.getText());
            else Arnc=0.0;

            if(RIN.getText().length()>0)
            Rin = Integer.parseInt(RIN.getText());
            else Rin=0;

            newTranscr = new TranscriptomicAnalysis(Id, siteId, fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            transcriptomieDaoImpl.insert(newTranscr);
            transcriptomieController.display(newTranscr);

            this.setStage(this.accepte);
            new TranscriptomieView(connection,fileManager,newTranscr,siteId);
            this.stage.close();

        } else {
            Id = id.getText().length() > 0 ? Integer.parseInt(id.getText()) : transcriptomicAnalysis.getId();
            Emplacement = emplacement.getText().length() > 0 ? Integer.parseInt(emplacement.getText()) : transcriptomicAnalysis.getLamellaLocation();
            NumSerie = numeroSerie.getText().length() > 0 ? Integer.parseInt(numeroSerie.getText()) : transcriptomicAnalysis.getSerialNumber();
            Cy3 = cy3.getText().length() > 0 ? Double.parseDouble(cy3.getText()) : transcriptomicAnalysis.getCyanine();
            Rendement = rendement.getText().length() > 0 ? Double.parseDouble(rendement.getText()) : transcriptomicAnalysis.getYield();
            Concentration = concentration.getText().length() > 0 ? Double.parseDouble(concentration.getText()) : transcriptomicAnalysis.getConcentration();
            Crit = critere.getText().length() > 0 ? critere.getText() : transcriptomicAnalysis.getExclusionCriteria();
            Activ = activiteSpecifique.getText().length() > 0 ? activiteSpecifique.getText() : transcriptomicAnalysis.getSpecificActivity();
            Arnc = ARNc.getText().length() > 0 ? Double.parseDouble(ARNc.getText()) : transcriptomicAnalysis.getARNC();
            Rin = RIN.getText().length() > 0 ? Integer.parseInt(RIN.getText()) : transcriptomicAnalysis.getRIN();

            if (fichierBrutPath == null) {
                if (this.transcriptomicAnalysis.getFichierBrut() == null) {
                    fichierBrutPath = null;
                } else {
                    fichierBrutPath = this.transcriptomicAnalysis.getFichierBrut();
                }
            }

            if (qualityReportPath == null) {
                if (this.transcriptomicAnalysis.getFichierBrut() == null) {
                    qualityReportPath = null;
                } else {
                    fichierBrutPath = this.transcriptomicAnalysis.getQualityReport();
                }
            }

            TranscriptomicAnalysis newTranscr = new TranscriptomicAnalysis(Id, siteId, fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            transcriptomieDaoImpl.update(newTranscr, transcriptomicAnalysis.getId());
            transcriptomieController.display(newTranscr);

            this.setStage(this.accepte);
            new TranscriptomieView(connection,fileManager,newTranscr,siteId);
            this.stage.close();
        }

    }

    @FXML
    private void fichierBrutButton() {
        this.startUpload(qualityReport, checkFichierBrut, "//Transcriptomie//",null,1 );
    }


    @FXML
    private void qualityReportButtonAction() {
        this.startUpload(qualityReport, checkQualityReport, "//Transcriptomie//",null,2 );
    }

    @FXML
    private void retour() {
        this.setStage(this.cancel);

        new TranscriptomieView(connection, fileManager, transcriptomicAnalysis, siteId);
        transcriptomieController.display(transcriptomicAnalysis);

        this.stage.close();
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
                this.fichierBrutPath = directory + addedFileName;
            else this.qualityReportPath = directory + addedFileName;

            label.setText(addedFileName);
            this.enableButtons(true, true);
        }
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.accepte.setDisable(!enable);
        this.fichierBrut.setDisable(!enable);
        this.qualityReport.setDisable(!enable);
    }
}



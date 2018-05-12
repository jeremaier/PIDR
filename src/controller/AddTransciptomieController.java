package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.TranscriptomieView;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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
    private int lastId;

    public AddTransciptomieController(TranscriptomieController transcriptomieController, Connection connection, FileManager fileManager, TranscriptomicAnalysis transcriptomicAnalysis, int siteId) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.transcriptomicAnalysis = transcriptomicAnalysis;
        this.siteId = siteId;
        this.transcriptomieController = transcriptomieController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);
        this.lastId = getIdLast();

        if (transcriptomicAnalysis == null) {
            this.id.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.intValue() == 0) {
                    this.enableButtons(false, false);
                    fichierBrutPath = null;
                    qualityReportPath = null;
                } else this.enableButtons(true, false);
            });
        } else {

            if (this.transcriptomicAnalysis.getFichierBrut() != null) {
                this.fichierBrut.setText("Supprimer");
                String[] s0 = this.transcriptomicAnalysis.getFichierBrut().split("//");
                this.checkFichierBrut.setText(s0[3]);
            }

            if (this.transcriptomicAnalysis.getQualityReport() != null) {
                this.qualityReport.setText("Supprimer");
                String[] s0 = this.transcriptomicAnalysis.getQualityReport().split("//");
                this.checkQualityReport.setText(s0[3]);
            }

            this.id.setText(Integer.toString(this.transcriptomicAnalysis.getIdBdd()));
            this.emplacement.setText(Integer.toString(this.transcriptomicAnalysis.getLamellaLocation()));
            this.rendement.setText(Double.toString(this.transcriptomicAnalysis.getYield()));
            this.concentration.setText(Double.toString(this.transcriptomicAnalysis.getConcentration()));
            this.ARNc.setText(Double.toString(this.transcriptomicAnalysis.getARNC()));
            this.cy3.setText(Double.toString(this.transcriptomicAnalysis.getCyanine()));
            this.numeroSerie.setText(Integer.toString(this.transcriptomicAnalysis.getSerialNumber()));
            this.activiteSpecifique.setText(Double.toString(this.transcriptomicAnalysis.getSpecificActivity()));
            this.critere.setText(this.transcriptomicAnalysis.getExclusionCriteria());
        }

        Pattern pattern = Pattern.compile("\\d*|\\d+\\,\\d*");
        TextFormatter formatterARNc = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formattercy3 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatterActiviteSpecifique = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatterRendement = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatterConcentration = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter formatterRIN = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null);

        this.ARNc.setTextFormatter(formatterARNc);
        this.cy3.setTextFormatter(formattercy3);
        this.activiteSpecifique.setTextFormatter(formatterActiviteSpecifique);
        this.rendement.setTextFormatter(formatterRendement);
        this.concentration.setTextFormatter(formatterConcentration);
        this.RIN.setTextFormatter(formatterRIN);

        this.id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.id.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.id.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.id.getText().length() >= 6)
                    this.id.setText(this.id.getText().substring(0, 6));
        });

        this.emplacement.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.emplacement.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.emplacement.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.emplacement.getText().length() >= 3)
                    this.emplacement.setText(this.emplacement.getText().substring(0, 3));
        });

        this.numeroSerie.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.numeroSerie.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.numeroSerie.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.numeroSerie.getText().length() >= 14)
                    this.numeroSerie.setText(this.numeroSerie.getText().substring(0, 14));
        });

        this.checkFichierBrut.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.checkFichierBrut.getText().equals("Aucun"))
                this.fichierBrut.setText("Supprimer");

        });

        this.checkQualityReport.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.checkQualityReport.getText().equals("Aucun"))
                this.qualityReport.setText("Supprimer");

        });
    }

    @FXML
    private void accepteButton() {
        TranscriptomicAnalysis newTranscr;
        int IdBdd, Emplacement, NumSerie;
        Double Activ, Cy3, Rendement, Concentration, Arnc, Rin;
        String Crit;

        if (transcriptomicAnalysis == null) {
            IdBdd = id.getText().length() > 0 ? Integer.parseInt(id.getText()) : 0;
            Emplacement = emplacement.getText().length() > 0 ? Integer.parseInt(emplacement.getText()) : 0;
            NumSerie = numeroSerie.getText().length() > 0 ? Integer.parseInt(numeroSerie.getText()) : 0;
            Cy3 = cy3.getText().length() > 0 ? Double.parseDouble(cy3.getText().replace(",", ".")) : (double) 0;
            Rendement = rendement.getText().length() > 0 ? Double.parseDouble(rendement.getText().replace(",", ".")) : (double) 0;
            Concentration = concentration.getText().length() > 0 ? Double.parseDouble(concentration.getText().replace(",", ".")) : (double) 0;
            Crit = critere.getText();
            Activ = activiteSpecifique.getText().length() > 0 ? Double.parseDouble(activiteSpecifique.getText().replace(",", ".")) : (double) 0;
            Arnc = ARNc.getText().length() > 0 ? Double.parseDouble(ARNc.getText()) : (double) 0;
            Rin = RIN.getText().length() > 0 ? Double.parseDouble(RIN.getText().replace(",", ".")) : 0.0;

            newTranscr = new TranscriptomicAnalysis(this.lastId, IdBdd, this.siteId, this.fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            this.transcriptomieDaoImpl.insert(newTranscr);
            this.transcriptomieController.display(newTranscr);

            this.setStage(this.accepte);
            new TranscriptomieView(this.stage, this.connection, this.fileManager, newTranscr, this.siteId);
            this.stage.close();
        } else {
            IdBdd = id.getText().length() > 0 ? Integer.parseInt(id.getText()) : transcriptomicAnalysis.getIdBdd();
            Emplacement = emplacement.getText().length() > 0 ? Integer.parseInt(emplacement.getText()) : transcriptomicAnalysis.getLamellaLocation();
            NumSerie = numeroSerie.getText().length() > 0 ? Integer.parseInt(numeroSerie.getText()) : transcriptomicAnalysis.getSerialNumber();
            Cy3 = cy3.getText().length() > 0 ? Double.parseDouble(cy3.getText()) : transcriptomicAnalysis.getCyanine();
            Rendement = rendement.getText().length() > 0 ? Double.parseDouble(rendement.getText()) : transcriptomicAnalysis.getYield();
            Concentration = concentration.getText().length() > 0 ? Double.parseDouble(concentration.getText()) : transcriptomicAnalysis.getConcentration();
            Crit = critere.getText().length() > 0 ? critere.getText() : transcriptomicAnalysis.getExclusionCriteria();
            Activ = activiteSpecifique.getText().length() > 0 ? Double.parseDouble(activiteSpecifique.getText()) : transcriptomicAnalysis.getSpecificActivity();
            Arnc = ARNc.getText().length() > 0 ? Double.parseDouble(ARNc.getText()) : transcriptomicAnalysis.getARNC();
            Rin = RIN.getText().length() > 0 ? Double.parseDouble(RIN.getText()) : transcriptomicAnalysis.getRIN();

            if (fichierBrutPath == null)
                fichierBrutPath = this.transcriptomicAnalysis.getFichierBrut();

            if (qualityReportPath == null) {
                if (this.transcriptomicAnalysis.getQualityReport() == null) {
                    qualityReportPath = null;
                } else {
                    qualityReportPath = this.transcriptomicAnalysis.getQualityReport();
                }
            }

            newTranscr = new TranscriptomicAnalysis(transcriptomicAnalysis.getId(),IdBdd, siteId, fichierBrutPath, Rin, Concentration, Arnc, Cy3, Rendement, Activ, Crit, NumSerie, Emplacement, qualityReportPath);
            transcriptomieDaoImpl.update(newTranscr, transcriptomicAnalysis.getId());
            transcriptomieController.display(newTranscr);

            this.setStage(this.accepte);
            new TranscriptomieView(this.stage, this.connection, this.fileManager, newTranscr, this.siteId);
            this.stage.close();
        }

    }

    @FXML
    private void fichierBrutButton() {
        if (this.checkFichierBrut.getText().equals("Aucun"))
            this.startUpload(this.fichierBrut, this.checkFichierBrut, transcriptomicAnalysis != null ? "//Transcriptomie//" + Integer.toString(this.transcriptomicAnalysis.getId()) + "//" : "//Transcriptomie//" + Integer.toString(lastId)  + "//", null, 1);
        else this.removeFileFromFTP("brut", this.fichierBrut, this.checkFichierBrut);
    }

    @FXML
    private void qualityReportButtonAction() {
        if (this.checkQualityReport.getText().equals("Aucun"))
            this.startUpload(this.qualityReport, this.checkQualityReport, transcriptomicAnalysis != null ? "//Transcriptomie//" + Integer.toString(this.transcriptomicAnalysis.getId()) + "//" : "//Transcriptomie//" + Integer.toString(lastId)  + "//", null, 2);
        else this.removeFileFromFTP("quality", this.qualityReport, this.checkQualityReport);
    }

    private void removeFileFromFTP(String buttonName, Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        switch (buttonName) {
            case "brut":
                removeTask.addUrls(new ArrayList<String>() {{
                    if (transcriptomicAnalysis != null && transcriptomicAnalysis.getFichierBrut() != null)
                        add(transcriptomicAnalysis.getFichierBrut());
                    else add(fichierBrutPath);
                }});

                this.checkFichierBrut.setText("Aucun");
                if (transcriptomicAnalysis != null)
                    this.transcriptomicAnalysis.setFichierBrut(null);
                else fichierBrutPath = null;
                break;
            case "quality":
                System.out.println("salut");
                removeTask.addUrls(new ArrayList<String>() {{
                    if (transcriptomicAnalysis != null && transcriptomicAnalysis.getQualityReport() != null)
                        add(transcriptomicAnalysis.getQualityReport());
                    else add(qualityReportPath);
                }});

                this.checkQualityReport.setText("Aucun");
                if (transcriptomicAnalysis != null)
                    this.transcriptomicAnalysis.setQualityReport(null);
                else qualityReport = null;
                break;
        }

        removeTask.setOnSucceeded(e -> {
            super.endRemove(null, this.progressBar, this.progressLabel);
            button.setText("Ajouter");
            label.setText("Aucun");
        });

        removeTask.setOnFailed(e -> removeTask.getOnSucceeded());

        if(this.transcriptomicAnalysis!=null)
        this.transcriptomieDaoImpl.update(transcriptomicAnalysis, transcriptomicAnalysis.getId());
        new Thread(removeTask).start();
    }


    @FXML
    private void retour() {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.cancel, null, this.progressBar, this.progressLabel);
        ArrayList<String> files = new ArrayList<>();
        String directory;

        directory = "//Transcriptomie//" + Integer.toString(this.lastId + 1) + "//";

        if (this.transcriptomicAnalysis == null) {
            if (!(this.checkFichierBrut.getText()).equals("Aucun"))
                files.add(directory + checkFichierBrut.getText());

            if (!(this.checkQualityReport.getText()).equals("Aucun"))
                files.add(directory + checkQualityReport.getText());

            removeTask.addUrls(files);

            new Thread(removeTask).start();
        }

        new TranscriptomieView(this.stage, connection, fileManager, transcriptomicAnalysis, siteId);
        transcriptomieController.display(transcriptomicAnalysis);

        this.stage.close();
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
        if (addedFileName != null) {
            if (num == 1) {
                this.fichierBrutPath = directory + addedFileName;
            } else this.qualityReportPath = directory + addedFileName;

            this.progressBar.setVisible(false);
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

    private int getIdLast(){
        ArrayList<Integer> ints = transcriptomieDaoImpl.idList();
        int i = 0;

        while (ints.contains(i))
            i++;

        return i;
    }
}



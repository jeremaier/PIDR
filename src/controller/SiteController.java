package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.SiteCutaneDaoImpl;
import src.daoImpl.TranscriptomieDaoImpl;
import src.table.CutaneousSite;
import src.table.Inclusion;
import src.table.Lesion;
import src.table.TranscriptomicAnalysis;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.AddSiteView;
import src.view.LesionsView;
import src.view.TranscriptomieView;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class SiteController extends Controller implements Initializable {
    @FXML
    Button retour;

    @FXML
    Button ajouter;

    @FXML
    Button supprimer;

    @FXML
    Button modifier;

    @FXML
    ListView<String> spectreList;

    @FXML
    Button downloadSpectre;

    @FXML
    Button suprSpectre;

    @FXML
    Button fichierDiag;

    @FXML
    Button transcriptomique;

    @FXML
    Button fichierMoyButton;

    @FXML
    TableView<CutaneousSite> affecteTab;

    @FXML
    TableColumn<CutaneousSite, String> siteCutane;

    @FXML
    TableColumn<CutaneousSite, Integer> Orientation;

    @FXML
    TableColumn<CutaneousSite, String> diag;

    @FXML
    TableColumn<CutaneousSite, String> autreDiag;

    private SiteCutaneDaoImpl siteCutaneDaoImpl;
    private ObservableList<CutaneousSite> siteListe;
    private ObservableList<String> spectre = FXCollections.observableArrayList();
    private CutaneousSite selectedSite;
    private String selectedSpectre;
    private Lesion lesion;
    private Integer selectedSpectreId;
    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private String[] s;

    public SiteController(Connection connection, Lesion lesion, FileManager fileManager) {
        this.connection = connection;
        this.lesion = lesion;
        this.fileManager = fileManager;
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<CutaneousSite> cutaneousSites) {
        ArrayList<String> urls = new ArrayList<>();

        for (CutaneousSite cutaneousSite : cutaneousSites) {
            urls.addAll(Arrays.asList(cutaneousSite.getSpectre().split("~#")));
            String fichierDiag, fichierMoy;

            if ((fichierDiag = cutaneousSite.getFichierDiag()) != null)
                urls.add(fichierDiag);

            if ((fichierMoy = cutaneousSite.getFichierMoy()) != null)
                urls.add(fichierMoy);
        }

        removeTask.addUrls(urls);
    }

    @FXML
    public void fichierDiagButtonAction() {
        if (this.selectedSite != null) {
            if (this.selectedSite.getFichierDiag() != null)
                this.startDownload(this.selectedSite.getFichierDiag(), this.fichierDiag);
            else JOptionPane.showMessageDialog(null, "Pas de fichier de diagnostic specifié pour ce site");
        } else JOptionPane.showMessageDialog(null, "Veuillez selectionner un site cutané");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.siteCutane.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        this.Orientation.setCellValueFactory(cellData -> cellData.getValue().orientationProperty().asObject());
        this.diag.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.autreDiag.setCellValueFactory(cellDate -> cellDate.getValue().autreDiagProperty());


        this.siteCutaneDaoImpl = new SiteCutaneDaoImpl(connection);
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);

        this.siteListe = siteCutaneDaoImpl.selectByLesion(this.lesion.getId());

        this.populateSite(siteListe);

        this.affecteTab.getSelectionModel().selectedItemProperty().addListener(observable -> {
            this.selectedSite = affecteTab.getSelectionModel().getSelectedItem();
            this.enableButtons(selectedSite != null, false);
            this.spectre.clear();
            if (selectedSite != null) {
                if (this.selectedSite.getSpectre()!= null && this.selectedSite.getSpectre().length()>0) {
                    System.out.println(selectedSite.getSpectre());
                    String[] s0 = this.selectedSite.getSpectre().split("~#");

                    for (String aS0 : s0) {
                        String[] s1 = aS0.split("//");

                        this.spectre.add("mesure_" + s1[2].charAt(0));
                    }

                    populateSpectre(this.spectre);
                }
            }
        });

        this.spectreList.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            this.selectedSpectre = this.spectreList.getSelectionModel().getSelectedItem();
            this.selectedSpectreId = this.spectreList.getSelectionModel().getSelectedIndex();

            if (this.selectedSpectre != null) {
                this.suprSpectre.setDisable(false);
                this.downloadSpectre.setDisable(false);
                this.fichierMoyButton.setDisable(false);
            } else {
                this.suprSpectre.setDisable(true);
                this.downloadSpectre.setDisable(true);
                this.fichierMoyButton.setDisable(true);
            }
        }));
    }

    private void populateSite(ObservableList<CutaneousSite> siteListe) {
        if (!siteListe.isEmpty())
            this.affecteTab.setItems(siteListe);

        else this.affecteTab.setItems(FXCollections.observableArrayList());
    }

    private void populateSpectre(ObservableList<String> spectre) {
        this.spectreList.setItems(!spectre.isEmpty() ? spectre : FXCollections.observableArrayList());
    }

    void populateSingleSite(CutaneousSite site) {
        this.siteListe.add(site);
        this.affecteTab.setItems(this.siteListe);
    }

    @FXML
    private void retour() {
        InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(connection);
        Inclusion inclusion = inclusionDaoImpl.selectById(this.lesion.getIdInclusion());

        this.setStage(this.retour);

        new LesionsView(this.stage, this.connection, this.fileManager, inclusion);

        this.stage.close();
    }

    @FXML
    private void addButtonAction() {
        this.setStage(this.retour);
        new AddSiteView(this.stage, this, null, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    private void updateButtonAction() {
        this.setStage(this.retour);
        new AddSiteView(this.stage, this, this.selectedSite, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    private void delButtonAction() {
        if (this.selectedSite != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer un site cutané");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                this.enableButtons(false, true);
                this.remove(new RemoveTask(this, this.fileManager).setParameters(this.supprimer, null, this.progressBar, this.progressLabel), this.selectedSite);
                this.siteListe.remove(selectedSite);
                this.affecteTab.getSelectionModel().clearSelection();
                this.enableButtons(true, true);
            } else alert.close();
        }
    }

    private void remove(RemoveTask removeTask, CutaneousSite cutaneousSite) {
        SiteController.remove(removeTask, new ArrayList<CutaneousSite>() {{
            add(cutaneousSite);
        }});
    }

    static void remove(RemoveTask task, ArrayList<CutaneousSite> cutaneousSites) {
        ArrayList<TranscriptomicAnalysis> analysesToRemove = new ArrayList<>();

        for (CutaneousSite cutaneousSite : cutaneousSites)
            analysesToRemove.addAll(TranscriptomieDaoImpl.removeTranscriptomie(Integer.toString(cutaneousSite.getId())));

        SiteController.removeFTPSQL(task, cutaneousSites);

        if (analysesToRemove.size() != 0)
            TranscriptomieController.remove(task, analysesToRemove);
        else new Thread(task).start();
    }

    private static void removeFTPSQL(RemoveTask removeTask, ArrayList<CutaneousSite> cutaneousSite) {
        SiteController.removeFTP(removeTask, cutaneousSite);
        SiteController.removeSQL(cutaneousSite);
    }

    private static void removeSQL(ArrayList<CutaneousSite> cutaneousSites) {
        for (CutaneousSite cutaneousSite : cutaneousSites)
            SiteCutaneDaoImpl.delete(cutaneousSite.getId());
    }

    public void fichierMoyAction() {
        this.startDownload(this.selectedSite.getFichierMoy(), this.fichierMoyButton);
    }

    @FXML
    private void downloadSpectreButtonAction() {
        if (this.selectedSpectre != null && this.selectedSpectreId != null) {
            this.s = this.selectedSite.getSpectre().split("~#");
            this.startDownload(s[this.selectedSpectreId], this.downloadSpectre);
        }
    }

    @FXML
    private void removeSpectreButtonAction() {
        if (this.selectedSpectreId != null && this.selectedSite != null) {
            this.s = this.selectedSite.getSpectre().split("~#");

            RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.supprimer, null, this.progressBar, this.progressLabel);
            removeTask.addUrls(new ArrayList<String>() {{
                add(s[selectedSpectreId]);
            }});
            new Thread(removeTask).start();
        }
    }

    @FXML
    private void transcriptomieButtonAction() {
        this.setStage(this.transcriptomique);

        if (transcriptomieDaoImpl.selectBySite(this.selectedSite.getId()) != null)
            new TranscriptomieView(this.stage, connection, fileManager, transcriptomieDaoImpl.selectBySite(this.selectedSite.getId()), this.selectedSite.getId());
        else new TranscriptomieView(this.stage, connection, fileManager, null, this.selectedSite.getId());

        this.stage.close();
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.supprimer.setDisable(!enable);
        this.modifier.setDisable(!enable);
        this.transcriptomique.setDisable(!enable);
        this.fichierDiag.setDisable(!enable);
    }

    @Override
    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel) {
        super.endRemove(button, progressBar, progressLabel);
        StringBuilder newSpectre = new StringBuilder();

        for (int i = 0; i < s.length - 1; i++)
            if (i != selectedSpectreId)
                newSpectre.append("~#").append(s[i]);

        this.selectedSite.setSpectre(newSpectre.substring(0));
        this.siteCutaneDaoImpl.update(this.selectedSite, this.selectedSite.getId());
        this.spectre.remove(this.selectedSpectre);

        populateSpectre(this.spectre);
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }

    void refreshSite() {
        this.siteListe = this.siteCutaneDaoImpl.selectAll();

        if (!this.siteListe.isEmpty())
            this.affecteTab.setItems(this.siteListe);

        else this.affecteTab.setItems(FXCollections.observableArrayList());
    }
}





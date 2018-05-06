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
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.AddSiteView;
import src.view.LesionsView;
import src.view.TranscriptomieView;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
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
    Button transcriptomique;

    @FXML
    TableView<CutaneousSite> affecteTab;

    @FXML
    TableColumn<CutaneousSite, String>  siteCutane;

    @FXML
    TableColumn<CutaneousSite, Integer> Orientation;

    @FXML
    TableColumn<CutaneousSite, String> diag;


    private SiteCutaneDaoImpl siteCutaneDaoImpl;
    private ObservableList<CutaneousSite> siteListe;
    private ObservableList<String> spectre;
    private CutaneousSite selectedSite;
    private String selectedSpectre;
    private Lesion lesion;
    private Integer selectedSpectreId;
    private TranscriptomieDaoImpl transcriptomieDaoImpl;
    private String[] s;

    public SiteController(Connection connection, Lesion lesion, FileManager fileManager){
        this.connection = connection;
        this.lesion = lesion;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.siteCutane.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        this.Orientation.setCellValueFactory(cellData -> cellData.getValue().orientationProperty().asObject());
        this.diag.setCellValueFactory(cellData -> cellData.getValue().diagProperty());

        this.siteCutaneDaoImpl = new SiteCutaneDaoImpl(connection);
        this.transcriptomieDaoImpl = new TranscriptomieDaoImpl(connection);

        this.siteListe = siteCutaneDaoImpl.selectByLesion(this.lesion.getId());

        this.populateSite(siteListe);

        this.affecteTab.getSelectionModel().selectedItemProperty().addListener(observable    -> {
            selectedSite = affecteTab.getSelectionModel().getSelectedItem();
            this.enableButtons(selectedSite != null, false);

            String[] s0 = this.selectedSite.getSpectre().split("~#");

            for (int i = 0; i < s0.length - 1; i++) {
                String[] s1 = s0[i].split("//");

                this.spectre.add("mesure_"+ Integer.toString(s1[2].charAt(0)) );
            }

            spectreList.setItems(spectre);
        });



        this.spectreList.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) ->{
            selectedSpectre = spectreList.getSelectionModel().getSelectedItem();
            selectedSpectreId= spectreList.getSelectionModel().getSelectedIndex();

            if(selectedSpectre != null){
                suprSpectre.setDisable(false);
                downloadSpectre.setDisable(false);
            } else {
                suprSpectre.setDisable(true);
                downloadSpectre.setDisable(true);
            }
        } ));
    }

    private void populateSite(ObservableList<CutaneousSite> siteListe) {
        if(!siteListe.isEmpty())
            affecteTab.setItems(siteListe);


        else affecteTab.setItems(FXCollections.observableArrayList());

    }

    private void populateSpectre(ObservableList<String> spectre ) {
        if (!spectre.isEmpty()){
            spectreList.setItems(spectre);
        }
        else spectreList.setItems(FXCollections.observableArrayList());
    }


    void populateSingleSite(CutaneousSite site) {
            siteListe.add(site);
            this.affecteTab.setItems(siteListe);
    }

    @FXML
    private void retour() {
        InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(connection);
        Inclusion inclusion = inclusionDaoImpl.selectById(this.lesion.getIdInclusion());

        this.setStage(this.retour);
        new LesionsView(connection, fileManager, inclusion);

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
            if(this.selectedSite!=null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmer la suppresion");
                alert.setHeaderText("Vous allez supprimer un site cutané");
                alert.setContentText("Confirmer?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    siteCutaneDaoImpl.delete(this.selectedSite.getId());
                    this.siteListe.remove(selectedSite);
                } else {
                    alert.close();
                }
            }
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

            RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.supprimer);
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
                new TranscriptomieView(connection, fileManager, transcriptomieDaoImpl.selectBySite(this.selectedSite.getId()), this.selectedSite.getId());
            else new TranscriptomieView(connection, fileManager, null, this.selectedSite.getId());

        this.stage.close();
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        supprimer.setDisable(!enable);
        modifier.setDisable(!enable);
        transcriptomique.setDisable(!enable);
    }

    @Override
    public void endRemove() {
        this.endDownload();
        StringBuilder newSpectre = new StringBuilder();

        for (int i = 0; i < s.length - 1; i++)
            if (i != selectedSpectreId)
                newSpectre.append("~#").append(s[i]);

        this.selectedSite.setSpectre(newSpectre.substring(1));
        this.siteCutaneDaoImpl.update(this.selectedSite, this.selectedSite.getId());
        this.spectre.remove(this.selectedSpectre);

        populateSpectre(spectre);
    }
}





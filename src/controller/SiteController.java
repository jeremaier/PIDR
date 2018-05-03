package src.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.LesionDaoImpl;
import src.daoImpl.SiteCutaneDaompl;
import src.daoImpl.TranscriptomieDaompl;
import src.table.CutaneousSite;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.FileManager;
import src.view.AddSiteView;
import src.view.LesionsView;
import src.view.TranscriptomieView;

import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public class SiteController implements Initializable {
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
    Button fichierMoy;

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

    @FXML
    TableColumn<CutaneousSite, String> siteSain;

    @FXML
     TableColumn<CutaneousSite, String> diagSain;

    private Connection connection;
    private Stage siteStage;
    private SiteCutaneDaompl siteCutaneDaompl;
    private ObservableList<CutaneousSite> siteListe;
    private ObservableList<String> spectre;
    private FileManager fileManager;
    private CutaneousSite selectedSite;
    private String selectedSpectre;
    private Lesion lesion;
    private Integer selectedSpectreId;
    private TranscriptomieDaompl transcriptomieDaompl;


    public SiteController(Connection connection, Lesion lesion, FileManager fileManager){
        this.connection=connection;
        this.lesion = lesion;
        this.fileManager = fileManager;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.siteCutane.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        this.Orientation.setCellValueFactory(cellData -> cellData.getValue().orientationProperty().asObject());
        this.diag.setCellValueFactory(cellData -> cellData.getValue().diagProperty());

        this.siteCutaneDaompl = new SiteCutaneDaompl(connection);
        this.transcriptomieDaompl = new TranscriptomieDaompl(connection);

        this.siteListe = siteCutaneDaompl.selectByLesion(this.lesion.getId());

        this.populateSite(siteListe);

        this.affecteTab.getSelectionModel().selectedItemProperty().addListener(observable    -> {
            selectedSite = (CutaneousSite) affecteTab.getSelectionModel().getSelectedItem();

            if(selectedSite != null) {
                supprimer.setDisable(false);
                fichierMoy.setDisable(false);
                modifier.setDisable(false);
                transcriptomique.setDisable(false);
            } else {
                supprimer.setDisable(true);
                fichierMoy.setDisable(true);
                modifier.setDisable(true);
                transcriptomique.setDisable(true);
            }

            String[] s0 = this.selectedSite.getSpectre().split("|");


            for(int i = 0; i<s0.length-1; i++ ){
                String[] s1 = s0[i].split("//");

                this.spectre.add("mesure_"+ Integer.toString(s1[2].charAt(0)) );
            }

            spectreList.setItems(spectre);


        });



        this.spectreList.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) ->{
            selectedSpectre = (String) spectreList.getSelectionModel().getSelectedItem();
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


    public void populateSingleSite(CutaneousSite site){

            siteListe.add(site);
            this.affecteTab.setItems(siteListe);
    }



    @FXML
    private void retour(ActionEvent actionEvent) {
        InclusionDaoImpl inclusionDaomlp = new InclusionDaoImpl(connection);
        Inclusion inclusion = inclusionDaomlp.selectById(this.lesion.getIdInclusion());

        this.siteStage = (Stage) retour.getScene().getWindow();

        new LesionsView(connection, fileManager,inclusionDaomlp, inclusion);

        this.siteStage.close();
    }


    @FXML
    private void fichierMoyAction(ActionEvent actionEvent) {
        if ( this.siteStage== null) {
            this.siteStage = (Stage) fichierMoy.getScene().getWindow();
        }

        this.fileManager.downloadFromUrl(siteStage, this.lesion.getFichierMoy(), null, true, true);
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent){
        if(this.siteStage == null)
            this.siteStage = (Stage) ajouter.getScene().getWindow();

        new AddSiteView(this, null, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    private void updateButtonAction(ActionEvent actionEvent){
        if(this.siteStage == null)
            this.siteStage = (Stage) ajouter.getScene().getWindow();

        new AddSiteView(this, this.selectedSite, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    private void delButtonAction(ActionEvent actionEvent){
            if(this.selectedSite!=null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmer la suppresion");
                alert.setHeaderText("Vous allez supprimer un site cutané");
                alert.setContentText("Confirmer?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){

                    siteCutaneDaompl.delete(this.selectedSite.getId());

                        this.siteListe.remove(selectedSite);
                } else {
                    alert.close();
                }
            }
    }

    @FXML
    private void downloadSpectreButtonAction(ActionEvent actionEvent){
        if(this.selectedSpectre!=null && this.selectedSpectreId!=null){
            String[] s = this.selectedSite.getSpectre().split("|");


           this.fileManager.downloadFromUrl(siteStage,s[selectedSpectreId],null,true,true);

        }
    }

    @FXML
    private void removeSpectreButtonAction(ActionEvent actionEvent){
        if(this.selectedSpectreId!=null && this.selectedSite!=null){
            String[] s = this.selectedSite.getSpectre().split("|");

            //openFTPConnection();
            this.fileManager.removeFile(s[selectedSpectreId]);

            String newSpectre ="";

            for(int i=0; i<s.length-1; i++){
                if(i!=selectedSpectreId) {
                    newSpectre = newSpectre + "|" +s[i];
                }
            }

            this.selectedSite.setSpectre(newSpectre.substring(1));
            this.siteCutaneDaompl.update(this.selectedSite, this.selectedSite.getId());
            this.spectre.remove(this.selectedSpectre);

            populateSpectre(spectre);
        }
    }

    @FXML
    private void transcriptomieButtonAction(ActionEvent actionEvent){
        if(transcriptomieDaompl.selectBySite(this.selectedSite.getId())!=null){
            if(this.siteStage==null){
                new TranscriptomieView(connection,fileManager,transcriptomieDaompl.selectBySite(this.selectedSite.getId()), this.selectedSite.getId());
            }
        }else{
            if(this.siteStage==null){
                new TranscriptomieView(connection,fileManager,null,this.selectedSite.getId());
            }
        }
    }
}





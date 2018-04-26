package src.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.SiteCutaneDaompl;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.FileManager;
import src.view.AddSiteView;

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
    ListView spectreList;

    @FXML
    Button downloadSpectre;

    @FXML
    Button suprSpectre;

    @FXML
    Button fichierMoy;

    @FXML
    Button transcriptomique;

    @FXML
    TableView affecteTab;

    @FXML
    TableView sainTab;

    @FXML
    Tab affecte;

    @FXML
    Tab sain;

    @FXML
    TableColumn<CutaneousSite, String> siteCutane;

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
    private ObservableList<CutaneousSite> siteListeSain;
    private ObservableList<CutaneousSite> siteListeNonSain;
    private ObservableList<String> spectre;
    private FileManager fileManager;
    private CutaneousSite selectedSite;
    private String selectedSpectre;
    private Lesion lesion;
    private Integer selectedSpectreId;


    public SiteController(Connection connection, Lesion lesion){
        this.connection=connection;
        this.lesion = lesion;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.siteCutane.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        this.Orientation.setCellValueFactory(cellData -> cellData.getValue().orientationProperty().asObject());
        this.diag.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.siteSain.setCellValueFactory(cellData -> cellData.getValue().siteProperty());
        this.diagSain.setCellValueFactory(cellData -> cellData.getValue().diagProperty());

        this.siteCutaneDaompl = new SiteCutaneDaompl(connection);
        this.siteListeSain = siteCutaneDaompl.selectBySain(this.lesion.getId(), 1);
        this.siteListeNonSain = siteCutaneDaompl.selectBySain(this.lesion.getId(), 2);

        this.populateSite(siteListeNonSain,siteListeSain);


        this.sainTab.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedSite = (CutaneousSite) sainTab.getSelectionModel().getSelectedItem();

            if(selectedSite != null) {
                supprimer.setDisable(false);
                fichierMoy.setDisable(false);
                modifier.setDisable(false);
            } else {
                supprimer.setDisable(true);
                fichierMoy.setDisable(true);
                modifier.setDisable(true);
            }

            String[] s0 = this.selectedSite.getSpectre().split("|");


            for(int i = 0; i<s0.length-1; i++ ){
                String[] s1 = s0[i].split("//");
                this.spectre.add("mesure_"+ Integer.toString(s1[2].charAt(0)) );
            }

            spectreList.setItems(spectre);
        });

        this.affecteTab.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedSite = (CutaneousSite) affecteTab.getSelectionModel().getSelectedItem();

            if(selectedSite != null) {
                supprimer.setDisable(false);
                fichierMoy.setDisable(false);
                modifier.setDisable(false);
            } else {
                supprimer.setDisable(true);
                fichierMoy.setDisable(true);
                modifier.setDisable(true);
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

    private void populateSite(ObservableList<CutaneousSite> siteListeNonSain, ObservableList<CutaneousSite> siteListeSain) {
        if(!siteListeNonSain.isEmpty())
            affecteTab.setItems(siteListeNonSain);

        else affecteTab.refresh();

        if(!siteListeSain.isEmpty())
            sainTab.setItems(siteListeSain);

        else sainTab.refresh();
    }

    private void populateSpectre(ObservableList<String> spectre ) {
        if (!spectre.isEmpty()){
            spectreList.setItems(spectre);
        }
        else spectreList.refresh();
    }



    @FXML
    private void retour(ActionEvent actionEvent) {
        this.siteStage = (Stage) siteStage.getScene().getWindow();
        this.siteStage.close();
    }


    @FXML
    private void fichierMoyAction(ActionEvent actionEvent) {
        if ( this.siteStage== null) {
            this.siteStage = (Stage) fichierMoy.getScene().getWindow();
        }
        //openFTPConnection();
        this.fileManager.downloadFromUrl(siteStage,this.lesion.getFichierMoy(), null);
        //closeConnection();
    }

    @FXML
    private void addButtonAction(ActionEvent actionEvent){
        if(this.siteStage == null)
            this.siteStage = (Stage) ajouter.getScene().getWindow();

        new AddSiteView(this.siteStage, null, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    private void updateButtonAction(ActionEvent actionEvent){
        if(this.siteStage == null)
            this.siteStage = (Stage) ajouter.getScene().getWindow();

        new AddSiteView(this.siteStage, this.selectedSite, this.connection, this.fileManager, this.lesion);
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
                    if(this.selectedSite.getHealthy()==0){
                        this.siteListeNonSain.remove(selectedSite);
                    }else{
                        this.siteListeSain.remove(selectedSite);
                    }

                    populateSite(siteListeNonSain,siteListeSain);
                } else {
                    alert.close();
                }
            }
    }

    @FXML
    private void downloadSpectreButtonAction(ActionEvent actionEvent){
        if(this.selectedSpectre!=null && this.selectedSpectreId!=null){
            String[] s = this.selectedSite.getSpectre().split("|");

            //openFTPConnection();
           //this.fileManager.downloadFromUrl(siteStage,s[selectedSpectreId]);
            //closeConnection();
        }
    }

    @FXML
    private void removeSpectreButtonAction(ActionEvent actionEvent){
        if(this.selectedSpectreId==null && this.selectedSpectreId!=null){
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
            //closeConnection();
        }
    }
}





package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.daoImpl.SiteCutaneDaoImpl;
import src.table.CutaneousSite;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.utils.SiteCutane;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddSiteController extends Controller implements Initializable {
    @FXML
    ComboBox<SiteCutane> siteCutane;

    @FXML
    TextField orientation;

    @FXML
    ComboBox<Diag> diag;

    @FXML
    TextField autreDiag;

    @FXML
    Button addFicherDiag;

    @FXML
    Label checkFichierDiag;

    @FXML
    Button addFichierSpectre;

    @FXML
    Button imagesSpectresButton;

    @FXML
    Label checkFichierSpectre;

    @FXML
    Label checkFichierImages;

    @FXML
    Button annuler;

    @FXML
    Button ajouter;

    @FXML
    TextField numMesur;

    private CutaneousSite site;
    private Lesion lesion;
    private SiteCutaneDaoImpl siteCutaneDaoImpl;
    private ObservableList<SiteCutane> siteValeur = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(SiteCutane.values())));
    private ObservableList<Diag> diagValeur = FXCollections.observableArrayList(FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(Diag.values()))));
    private String fichierDiagPath = null;
    private String imagesSpectresPath = null;
    private String spectrePath = null;
    private SiteController siteController;
    private int lastId;
    private ArrayList<String> spectreList = new ArrayList<>();

    public AddSiteController(SiteController siteController, CutaneousSite site, Connection connection, FileManager fileManager, Lesion lesion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.site = site;
        this.lesion = lesion;
        this.siteController = siteController;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.siteCutane.getItems().addAll(this.siteValeur);
        this.diag.getItems().addAll(this.diagValeur);
        this.siteCutaneDaoImpl = new SiteCutaneDaoImpl(this.connection);

        if (this.site != null) {
            this.ajouter.setText("Modifier");
            this.siteCutane.getSelectionModel().select(this.site.getSite());

            this.orientation.setText(Integer.toString(this.site.getOrientation()));
            this.diag.getSelectionModel().select(this.site.getDiag());
            this.autreDiag.setText(this.site.getAutreDiag());
            this.lastId = this.site.getId();

            if (this.site.getFichierDiag() != null && this.site.getFichierDiag().length()>0) {
                this.checkFichierDiag.setText(this.site.getFichierDiag().split("//")[3]);
                this.addFicherDiag.setText("Supprimer");
            }else this.checkFichierDiag.setText("Aucun");

            if (this.site.getImagesSpectres() != null && this.site.getImagesSpectres().length()>0 ) {
                this.imagesSpectresButton.setText("Supprimer");
                this.checkFichierImages.setText(this.site.getImagesSpectres().split("//")[3]);
            }else this.checkFichierImages.setText("Aucun");

            if (this.site.getSpectre()!=null && this.site.getSpectre().length() >0 ) {
                System.out.println(site.getSpectre().length());
                this.checkFichierSpectre.setText("Non vide");
            }else this.checkFichierSpectre.setText("Aucun");



        } else {
            this.lastId = getIdLast();
        }

        this.siteCutane.itemsProperty().addListener(observable -> this.enableButtons(!this.siteCutane.getValue().equals(SiteCutane.SAIN), false));
        this.numMesur.lengthProperty().addListener((observable, oldValue, newValue) -> this.addFichierSpectre.setDisable(this.numMesur.getText().length() <= 0));

        this.checkFichierDiag.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Aucun")) {
                this.addFicherDiag.setText("Supprimer");
            }
        });

        this.checkFichierImages.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Aucun")) {
                this.imagesSpectresButton.setText("Supprimer");
            }
        });

        this.checkFichierSpectre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.site.getSpectre()!=null) {
                this.checkFichierSpectre.setText("Non vide");
            }
        });
    }

    @FXML
    private void cancelButtonAction() {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.annuler, null, this.progressBar, this.progressLabel);
        ArrayList<String> files = new ArrayList<>();
        String directory;
        directory = "//siteCutane//" + Integer.toString(this.lastId) + "//";

        if (this.site == null) {
            if (!(this.checkFichierDiag.getText()).equals("Aucun"))
                files.add(directory + this.checkFichierDiag.getText());
            if (!(this.checkFichierImages.getText()).equals("Aucun"))
                files.add(directory + this.checkFichierImages.getText());

            files.addAll(spectreList);

            removeTask.addUrls(files);

            new Thread(removeTask).start();
        }

        this.setStage(this.annuler);
        this.stage.close();
    }

    @FXML
    private void accepteButtonAction() {
        String SITE = SiteCutane.NULL.toString();
        Integer Orientation = 0;
        String diagnostique = Diag.NULL.toString();
        String AutreDiag;

        if (this.site == null) {
            if (this.siteCutane.getValue() != null)
                SITE = this.siteCutane.getValue().toString();

            if (this.diag.getValue() != null)
                diagnostique = this.diag.getValue().toString();

            if (this.orientation.getText().length() > 0)
                Orientation = Integer.parseInt(this.orientation.getText());

            AutreDiag = this.autreDiag.getText();

            CutaneousSite newSite = new CutaneousSite(this.lastId, this.lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, this.fichierDiagPath, this.imagesSpectresPath, this.spectrePath);
            this.siteController.populateSingleSite(newSite);

            this.siteCutaneDaoImpl.insert(newSite);
        } else {
            SITE = this.siteCutane.getSelectionModel().getSelectedItem() != null ? this.siteCutane.getSelectionModel().getSelectedItem().toString() : this.site.getSite().toString();
            Orientation = this.orientation.getText().length() > 0 ? Integer.parseInt(this.orientation.getText()) : this.site.getOrientation();
            diagnostique = this.diag.getSelectionModel().getSelectedItem() != null ? this.diag.getSelectionModel().getSelectedItem().toString() : this.site.getDiag().toString();

            if (this.autreDiag.getText() != null)
                AutreDiag = this.autreDiag.getText();
            else
                AutreDiag = this.site.getAutreDiag();


            if (this.fichierDiagPath == null)
                this.fichierDiagPath = this.site.getFichierDiag();
            else if(this.fichierDiagPath.equals(""))
                this.fichierDiagPath=null;

            if (this.imagesSpectresPath == null)
                this.imagesSpectresPath = site.getImagesSpectres();
            else if(this.imagesSpectresPath.equals(""))
                this.imagesSpectresPath=null;

            if (this.spectrePath == null)
                this.spectrePath = this.site.getSpectre();
            else if (this.site.getSpectre() != null)
                this.spectrePath = this.spectrePath + "~#" + site.getSpectre();

            CutaneousSite newSite = new CutaneousSite(this.site.getId(), lesion.getId(), SITE, Orientation, diagnostique, AutreDiag, this.fichierDiagPath, this.imagesSpectresPath, this.spectrePath);
            newSite.setFichierDiag(this.fichierDiagPath);
            newSite.setImagesSpectres(this.imagesSpectresPath);
            newSite.setSpectre(this.spectrePath);

            this.siteCutaneDaoImpl.update(newSite, this.site.getId());
            this.siteController.refreshSite();
        }

        this.setStage(this.ajouter);
        this.stage.close();
    }

    @FXML
    private void fichierDiag() {
        if (this.checkFichierDiag.getText().equals("Aucun"))
            this.startUpload(this.addFicherDiag, checkFichierDiag, "//siteCutane//" + (site != null ? Integer.toString(this.site.getId())+"//" : Integer.toString(this.lastId)+"//"), null, 1);
        else
            this.removeFileFromFTP(this.addFicherDiag, this.checkFichierDiag, "//siteCutane//" + (site != null ? Integer.toString(this.site.getId())+"//" : Integer.toString(this.lastId)+"//") + this.checkFichierDiag.getText(),1);
    }

    @FXML
    private void imagesSpectresAction() {
        if (this.checkFichierImages.getText().equals("Aucun"))
            this.startUpload(this.imagesSpectresButton, checkFichierImages, "//siteCutane//" + (site != null ? Integer.toString(this.site.getId())+"//" : Integer.toString(this.lastId)+"//" ), null, 3);
        else
            this.removeFileFromFTP(this.imagesSpectresButton, this.checkFichierImages, "//siteCutane//" + (site != null ? Integer.toString(this.site.getId())+"//" : Integer.toString(this.lastId)+"//") + this.checkFichierImages.getText(),3);
    }

    private void removeFileFromFTP(Button button, Label label, String file, int num) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        removeTask.addUrls(new ArrayList<String>() {{
            add(file);
        }});

        removeTask.setOnSucceeded(e -> {
            this.endRemove(null, this.progressBar, this.progressLabel, num);
            button.setText("Ajouter");
            label.setText("Aucun");
        });

        removeTask.setOnFailed(e -> removeTask.getOnSucceeded());
        if(this.site!=null)
        this.siteCutaneDaoImpl.update(this.site, this.site.getId());

        new Thread(removeTask).start();
    }

    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel, int num) {
        super.endDownload(button, progressBar, progressLabel);
        if (num==1)
            this.fichierDiagPath="";
        else this.imagesSpectresPath="";


    }

    @FXML
    private void spectreButtonAction() {
        if (this.numMesur.getText().length() > 0) {
            if (this.site == null)
                this.startUpload(this.addFichierSpectre, this.checkFichierSpectre, "//siteCutane//" + Integer.toString(this.lastId)+"//", numMesur.getText(), 2);
            else
                this.startUpload(this.addFichierSpectre, this.checkFichierSpectre, "//siteCutane//" + Integer.toString(this.site.getId())+"//", numMesur.getText(), 2);


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Pas de numéro de mesure précisé");
            alert.showAndWait();
        }
    }


    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.siteCutane.setDisable(!enable);
        this.orientation.setDisable(!enable);
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
        if (addedFileName != null) {
            if (num == 1)
                this.fichierDiagPath = directory + addedFileName;
            else if(num==2) this.spectrePath = directory + numMesur.getText() + "=" + addedFileName;
            else this.imagesSpectresPath = directory + addedFileName;

            label.setText(addedFileName);
        }
        spectreList.add("//siteCutane//" + Integer.toString(this.lastId)+"//" +numMesur.getText()+"="+checkFichierSpectre.getText());

        this.progressBar.setVisible(false);
        this.enableButtons(true, true);
    }

    private int getIdLast(){
        ArrayList<Integer> ints = siteCutaneDaoImpl.idList();
        int i = 0;

        while(ints.contains(i)){
            System.out.println(ints.contains(i));
            i++;
            System.out.println(i);
        }

        return i;
    }
}

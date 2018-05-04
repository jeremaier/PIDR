package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.LameHistologiqueDaoImpl;
import src.table.HistologicLamella;
import src.table.Lesion;
import src.utils.FileManager;
import src.view.AddLameView;
import src.view.SiteView;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LameController extends Controller implements Initializable {

    @FXML
    Button retour;

    @FXML
    TableView<HistologicLamella> tab;

    @FXML
    TableColumn<HistologicLamella, Integer> numeroLame;

    @FXML
    TableColumn<HistologicLamella, String> siteCoupe;

    @FXML
    TableColumn<HistologicLamella, Integer> orientationColeurVert;

    @FXML
    TableColumn<HistologicLamella, Integer> orientationColeurNoire;

    @FXML
    TableColumn<HistologicLamella, String> Coloration;

    @FXML
    Button photo;

    @FXML
    Button modifier;

    @FXML
    Button ajouter;

    @FXML
    Button supprimer;

    private Connection connection;
    private Stage lameStage;
    private LameHistologiqueDaoImpl lameHistologiqueDaoImpl;
    private ObservableList<HistologicLamella> lameList;
    private FileManager fileManager;
    private HistologicLamella selectedHistologicLamella;
    private Lesion lesion;
    private int numAnapat;


    public LameController(Connection connection, Lesion lesion, FileManager fileManager, int numAnapat) {
        this.connection = connection;
        this.lesion = lesion;
        this.fileManager = fileManager;
        this.numAnapat = numAnapat;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.numeroLame.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        this.siteCoupe.setCellValueFactory(cellData -> cellData.getValue().siteCoupeProperty());
        this.orientationColeurVert.setCellValueFactory(cellData -> cellData.getValue().orientationVertProperty().asObject());
        this.orientationColeurNoire.setCellValueFactory(cellData -> cellData.getValue().orientationNoirProperty().asObject());
        this.Coloration.setCellValueFactory(cellData -> cellData.getValue().colorationProperty());

        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);

        this.lameList = lameHistologiqueDaoImpl.selectByLesion(this.lesion.getId());
        this.populate();

        this.tab.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            selectedHistologicLamella = this.tab.getSelectionModel().getSelectedItem();

            if (selectedHistologicLamella != null)
                this.enableButtons(true, false);
            else this.enableButtons(false, false);

        });

    }

    private void populate() {
        if (!lameList.isEmpty()) {
            this.tab.setItems(lameList);
        } else {
            this.tab.setItems(FXCollections.observableArrayList());
        }
    }


    @FXML
    public void cancelButtonEvent() {

        this.lameStage = (Stage) retour.getScene().getWindow();


        new SiteView(this.lesion, this.connection, this.fileManager);


        this.lameStage.close();


    }

    @FXML
    public void ajoutButtonAction() {
        if (lameStage == null)
            this.lameStage = (Stage) ajouter.getScene().getWindow();

        new AddLameView(null, connection,fileManager,lesion,numAnapat);


    }

    @FXML
    public void modifActionButton() {

        if (lameStage == null)
            this.lameStage = (Stage) modifier.getScene().getWindow();
        if (selectedHistologicLamella != null) {
            new AddLameView(null, connection, fileManager, lesion, numAnapat);
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner une lame");
        }
    }

    @FXML
    public void removeButtonAction() {
        if (this.selectedHistologicLamella != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmer la suppresion");
            alert.setHeaderText("Vous allez supprimer une lame histologique");
            alert.setContentText("Confirmer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                lameHistologiqueDaoImpl.delete(this.selectedHistologicLamella.getId());
                this.lameList.remove(selectedHistologicLamella);

                populate();
            } else {
                alert.close();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Veuillez selectionner une lame");
        }
    }

    @FXML
    public void photoButtonAction() {
        if(this.selectedHistologicLamella.getPhoto() != null){
            this.startDownload(this.selectedHistologicLamella.getPhoto(), this.photo);
        }else{
            JOptionPane.showMessageDialog(null, "Pas de photo specifié pour cette lame");
        }
    }

    private void startDownload(String url, Button button) {
        this.startDownload(new ArrayList<String>() {{
            add(url);
        }}, button);
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        photo.setDisable(!enable);
        supprimer.setDisable(!enable);
    }
}

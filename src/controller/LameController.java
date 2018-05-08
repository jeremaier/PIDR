package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.LameHistologiqueDaoImpl;
import src.table.HistologicLamella;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.AddLameView;
import src.view.LesionsView;

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
    TableColumn<HistologicLamella, String> numeroLame;

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

    private ObservableList<HistologicLamella> lameList;
    private HistologicLamella selectedHistologicLamella;
    private Lesion lesion;
    private LameHistologiqueDaoImpl lameHistologiqueDaoImpl;

    public LameController(Connection connection, Lesion lesion, FileManager fileManager) {
        this.connection = connection;
        this.lesion = lesion;
        this.fileManager = fileManager;
        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);
    }

    static void remove(RemoveTask task, ArrayList<HistologicLamella> histologicLamellas, boolean start) {
        LameController.removeFTPSQL(task, histologicLamellas);

        if (start)
            new Thread(task).start();
    }

    private void populate() {
        if (!lameList.isEmpty()) {
            this.tab.setItems(lameList);
        } else {
            this.tab.setItems(FXCollections.observableArrayList());
        }
    }

    void populateSingleLame(HistologicLamella lamella) {
        this.lameList.add(lamella);
        this.tab.setItems(this.lameList);
    }

    @FXML
    public void cancelButtonEvent() {
        this.setStage(this.retour);
        InclusionDaoImpl inclusionDaoImpl = new InclusionDaoImpl(this.connection);

        new LesionsView(this.stage, this.connection, this.fileManager, inclusionDaoImpl.selectById(this.lesion.getIdInclusion()));

        this.stage.close();
    }

    @FXML
    public void ajoutButtonAction() {
        this.setStage(this.ajouter);
        new AddLameView(this.stage, this, null, this.connection, this.fileManager, this.lesion);
    }

    @FXML
    public void modifActionButton() {
        this.setStage(this.modifier);

        if (selectedHistologicLamella != null)
            new AddLameView(this.stage, this, this.selectedHistologicLamella, this.connection, this.fileManager, this.lesion);
        else JOptionPane.showMessageDialog(null, "Veuillez selectionner une lame");
    }

    private static void removeFTPSQL(RemoveTask removeTask, ArrayList<HistologicLamella> histologicLamellas) {
        LameController.removeFTP(removeTask, histologicLamellas);
        LameController.removeSQL(histologicLamellas);
    }

    private static void removeSQL(ArrayList<HistologicLamella> histologicLamellas) {
        for (HistologicLamella histologicLamella : histologicLamellas)
            LameHistologiqueDaoImpl.delete(histologicLamella.getId());
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<HistologicLamella> histologicLamellas) {
        ArrayList<String> urls = new ArrayList<>();

        for (HistologicLamella histologicLamella : histologicLamellas) {
            String photo;

            if ((photo = histologicLamella.getPhoto()) != null)
                urls.add(photo);
        }

        removeTask.addUrls(urls);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.numeroLame.setCellValueFactory(cellData -> cellData.getValue().numLameProperty());
        this.siteCoupe.setCellValueFactory(cellData -> cellData.getValue().siteCoupeProperty());
        this.orientationColeurVert.setCellValueFactory(cellData -> cellData.getValue().orientationVertProperty().asObject());
        this.orientationColeurNoire.setCellValueFactory(cellData -> cellData.getValue().orientationNoirProperty().asObject());
        this.Coloration.setCellValueFactory(cellData -> cellData.getValue().colorationProperty());

        LameHistologiqueDaoImpl lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);
        this.lameList = lameHistologiqueDaoImpl.selectByLesion(this.lesion.getId());
        this.populate();

        this.tab.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            selectedHistologicLamella = this.tab.getSelectionModel().getSelectedItem();
            this.enableButtons(selectedHistologicLamella != null, false);
        });
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
                this.enableButtons(false, true);
                this.remove(new RemoveTask(this, this.fileManager).setParameters(this.supprimer, null, this.progressBar, this.progressLabel), this.selectedHistologicLamella);
                this.lameList.remove(this.selectedHistologicLamella);
                this.tab.getSelectionModel().clearSelection();
                populate();
                this.enableButtons(true, true);
            } else alert.close();
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez selectionner une lame");
        }
    }

    private void remove(RemoveTask removeTask, HistologicLamella histologicLamella) {
        LameController.remove(removeTask, new ArrayList<HistologicLamella>() {{
            add(histologicLamella);
        }}, true);
    }

    @FXML
    public void photoButtonAction() {
        if (this.selectedHistologicLamella != null) {
            if (this.selectedHistologicLamella.getPhoto() != null)
                this.startDownload(this.selectedHistologicLamella.getPhoto(), this.photo);
            else JOptionPane.showMessageDialog(null, "Pas de photo specifié pour cette lame");
        } else JOptionPane.showMessageDialog(null, "Veuillez selectionner une lame");
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.supprimer.setDisable(!enable);
        this.modifier.setDisable(!enable);

        if (selectedHistologicLamella != null) {
            if (this.selectedHistologicLamella.getPhoto() != null)
                this.photo.setDisable(!enable);
        } else this.photo.setDisable(!enable);

        if (all) {
            this.ajouter.setDisable(!enable);
            this.retour.setDisable(!enable);
        }
    }

    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel) {
        this.photo.setDisable(true);
        this.supprimer.setDisable(true);
        this.modifier.setDisable(true);
        this.ajouter.setDisable(false);
        this.retour.setDisable(false);

        if (button != null)
            button.setVisible(true);

        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }

    void refreshLesions() {
        this.lameList = this.lameHistologiqueDaoImpl.selectByLesion(this.lesion.getId());

        if (!this.lameList.isEmpty())
            this.tab.setItems(this.lameList);
        else this.tab.setItems(FXCollections.observableArrayList());
    }
}

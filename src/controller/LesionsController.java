package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import src.daoImpl.LameHistologiqueDaoImpl;
import src.daoImpl.LesionDaoImpl;
import src.daoImpl.SiteCutaneDaoImpl;
import src.table.CutaneousSite;
import src.table.HistologicLamella;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.view.AddLesionView;
import src.view.InclusionsView;
import src.view.LameView;
import src.view.SiteView;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LesionsController extends Controller implements Initializable {
    @FXML
    Button returnButton;
    @FXML
    Button photosButton;
    @FXML
    Button fileDiagButton;
    @FXML
    Button siteCutaneButton;
    @FXML
    Button histologicLamellaButton;
    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    TableView<Lesion> lesionsTable;
    @FXML
    TableColumn<Lesion, String> lesionSite;
    @FXML
    TableColumn<Lesion, String> lesionDiag;
    @FXML
    TableColumn<Lesion, String> lesionAutreDiag;

    private Inclusion inclusion;
    private Lesion selectedLesion;
    private int selectedIdLesion = -1;
    private ObservableList<Lesion> lesionsList;
    private LesionDaoImpl lesionDaoImpl;

    public LesionsController(Connection connection, FileManager fileManager, Inclusion inclusion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.inclusion = inclusion;
    }

    void populateLesions(Lesion lesion) {
        this.lesionsList.add(lesion);
        this.lesionsTable.setItems(this.lesionsList);
    }

    private void populateLesions() {
        if (!this.lesionsList.isEmpty())
            this.lesionsTable.setItems(this.lesionsList);
        else this.lesionsTable.setItems(FXCollections.observableArrayList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lesionSite.setCellValueFactory(cellData -> cellData.getValue().siteAnatomiqueProperty());
        this.lesionDiag.setCellValueFactory(cellData -> cellData.getValue().diagProperty());
        this.lesionAutreDiag.setCellValueFactory(cellData -> cellData.getValue().autreDiagProperty());
        this.lesionDaoImpl = new LesionDaoImpl(this.connection);
        this.lesionsList = this.lesionDaoImpl.selectAllByInclusion(Integer.parseInt(this.inclusion.getId()));
        this.populateLesions();

        this.lesionsTable.setOnMouseClicked((MouseEvent event) -> {
            int id = this.lesionsTable.getSelectionModel().getSelectedIndex();

            if ((event.getButton().equals(MouseButton.PRIMARY) || event.getButton().equals(MouseButton.SECONDARY)) && id >= 0) {
                this.selectedIdLesion = id;
                this.selectedLesion = this.lesionsList.get(this.selectedIdLesion);
                this.enableButtons(this.selectedLesion != null, false);
            }
        });
    }

    public void enableButtons(boolean enable, boolean all) {
        this.removeButton.setDisable(!enable);
        this.editButton.setDisable(!enable);

        if (selectedLesion != null) {
            if (!this.selectedLesion.getPhotoFixe().equals("Aucun") || !this.selectedLesion.getPhotoSur().equals("Aucun") || !this.selectedLesion.getPhotoHors().equals("Aucun"))
                this.photosButton.setDisable(!enable);

            if (!this.selectedLesion.getFileDiag().equals("Aucun"))
                this.fileDiagButton.setDisable(!enable);
        } else {
            this.photosButton.setDisable(!enable);
            this.fileDiagButton.setDisable(!enable);
        }

        this.siteCutaneButton.setDisable(!enable);
        this.histologicLamellaButton.setDisable(!enable);

        if (all) {
            this.addButton.setDisable(!enable);
            this.returnButton.setDisable(!enable);
        }
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<Lesion> lesions) {
        ArrayList<String> urls = new ArrayList<>();

        for (Lesion lesion : lesions) {
            String photoSur, photoHors, photoFixe, diagFile;

            if (!(photoSur = lesion.getPhotoSur()).equals("Aucun"))
                urls.add(photoSur);

            if (!(photoHors = lesion.getPhotoHors()).equals("Aucun"))
                urls.add(photoHors);

            if (!(photoFixe = lesion.getPhotoFixe()).equals("Aucun"))
                urls.add(photoFixe);

            if (!(diagFile = lesion.getFileDiag()).equals("Aucun"))
                urls.add(diagFile);
        }

        removeTask.addUrls(urls);
    }

    public void photosAction() {
        ArrayList<String> photos = new ArrayList<>();
        String photoSur, photoHors, photoFixe;

        if (!(photoSur = selectedLesion.getPhotoSur()).equals("Aucun"))
            photos.add(photoSur);

        if (!(photoHors = selectedLesion.getPhotoHors()).equals("Aucun"))
            photos.add(photoHors);

        if (!(photoFixe = selectedLesion.getPhotoFixe()).equals("Aucun"))
            photos.add(photoFixe);

        this.startDownload(photos, this.photosButton);
    }

    public void fileDiagAction() {
        this.startDownload(this.selectedLesion.getFileDiag(), this.fileDiagButton);
    }

    void refreshLesions() {
        this.lesionsList = this.lesionDaoImpl.selectAllByInclusion(Integer.parseInt(this.inclusion.getId()));

        if (!this.lesionsList.isEmpty())
            this.lesionsTable.setItems(this.lesionsList);
        else this.lesionsTable.setItems(FXCollections.observableArrayList());
    }

    public void addAction() {
        this.setStage(this.addButton);

        new AddLesionView(this.stage, this, null, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    public void editAction() {
        this.setStage(this.editButton);

        new AddLesionView(this.stage, this, this.selectedLesion, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    static void remove(RemoveTask task, ArrayList<Lesion> lesions) {
        ArrayList<HistologicLamella> lamesToRemove = new ArrayList<>();
        ArrayList<CutaneousSite> sitesToRemove = new ArrayList<>();

        for (Lesion lesion : lesions) {
            lamesToRemove.addAll(LameHistologiqueDaoImpl.removeLamellas(Integer.toString(lesion.getId())));
            sitesToRemove.addAll(SiteCutaneDaoImpl.removeSites(Integer.toString(lesion.getId())));
        }

        LesionsController.removeFTPSQL(task, lesions);

        if (lamesToRemove.size() != 0 && sitesToRemove.size() != 0) {
            LameController.remove(task, lamesToRemove, false);
            SiteController.remove(task, sitesToRemove);
        } else if (lamesToRemove.size() != 0)
            LameController.remove(task, lamesToRemove, true);
        else if (sitesToRemove.size() != 0)
            SiteController.remove(task, sitesToRemove);
        else new Thread(task).start();
    }

    private static void removeFTPSQL(RemoveTask removeTask, ArrayList<Lesion> lesions) {
        LesionsController.removeFTP(removeTask, lesions);
        LesionsController.removeSQL(lesions);
    }

    private static void removeSQL(ArrayList<Lesion> lesions) {
        for (Lesion lesion : lesions)
            LesionDaoImpl.delete(lesion.getId());
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }

    public void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer la lesion et les documents attachés");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            this.enableButtons(false, true);
            this.remove(new RemoveTask(this, this.fileManager).setParameters(this.removeButton, null, this.progressBar, this.progressLabel), this.selectedLesion);
            this.lesionsList.remove(this.selectedIdLesion);
            this.lesionsTable.getSelectionModel().clearSelection();
            this.selectedIdLesion = -1;
        } else alert.close();
    }

    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel) {
        this.removeButton.setDisable(true);
        this.editButton.setDisable(true);
        this.photosButton.setDisable(true);
        this.fileDiagButton.setDisable(true);
        this.siteCutaneButton.setDisable(true);
        this.histologicLamellaButton.setDisable(true);
        this.addButton.setDisable(false);
        this.returnButton.setDisable(false);

        if (button != null)
            button.setVisible(true);

        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    private void remove(RemoveTask removeTask, Lesion lesion) {
        LesionsController.remove(removeTask, new ArrayList<Lesion>() {{
            add(lesion);
        }});
    }

    public void siteCutaneAction() {
        this.setStage(this.siteCutaneButton);

        new SiteView(this.stage, this.selectedLesion, this.connection, this.fileManager);

        this.stage.close();
    }

    public void histologicLamellaAction() {
        this.setStage(this.histologicLamellaButton);

        new LameView(this.stage, this.connection, this.fileManager, this.selectedLesion);

        this.stage.close();
    }

    public void returnAction() {
        this.setStage(this.returnButton);
        this.fileManager.openFTPConnection();

        new InclusionsView(this.stage, this.connection, this.fileManager);

        this.stage.close();
    }
}
package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.LesionDaoImpl;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.Diag;
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
    Button fichierMoyButton;
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

    private static void remove(RemoveTask removeTask, Inclusion inclusion, Lesion lesion) {
        LesionsController.remove(removeTask, inclusion, new ArrayList<Lesion>() {{
            add(lesion);
        }});
    }

    static void remove(RemoveTask removeTask, Inclusion inclusion, ArrayList<Lesion> lesions) {
        LesionsController.removeFTP(removeTask, lesions);
        LesionsController.removeSQL(inclusion, lesions);
    }

    private static void removeSQL(Inclusion inclusion, ArrayList<Lesion> lesions) {
        for (Lesion lesion : lesions) {
            Diag diag = lesion.getDiag();

            if (!LesionDaoImpl.moreThanOneDiag(diag))
                InclusionDaoImpl.removeDiag(diag, Integer.parseInt(inclusion.getId()));

            LesionDaoImpl.delete(lesion.getId());
        }
    }

    private static void removeFTP(RemoveTask removeTask, ArrayList<Lesion> lesions) {
        ArrayList<String> urls = new ArrayList<>();

        for (Lesion lesion : lesions) {
            String photoSur, photoHors, photoFixe, diagFile, fichierMoy;

            if (!(photoSur = lesion.getPhotoSur()).equals("Aucun"))
                urls.add(photoSur);

            if (!(photoHors = lesion.getPhotoHors()).equals("Aucun"))
                urls.add(photoHors);

            if (!(photoFixe = lesion.getPhotoFixe()).equals("Aucun"))
                urls.add(photoFixe);

            if (!(diagFile = lesion.getFileDiag()).equals("Aucun"))
                urls.add(diagFile);

            if (!(fichierMoy = lesion.getFichierMoy()).equals("Aucun"))
                urls.add(fichierMoy);
        }

        removeTask.setUrls(urls);

        new Thread(removeTask).start();
    }

    void refreshLesions() {
        this.lesionsList = this.lesionDaoImpl.selectAll();

        if (!this.lesionsList.isEmpty())
            this.lesionsTable.setItems(this.lesionsList);
        else this.lesionsTable.setItems(FXCollections.observableArrayList());
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

            if (!this.selectedLesion.getFichierMoy().equals("Aucun"))
                this.fichierMoyButton.setDisable(!enable);
        } else {
            this.photosButton.setDisable(!enable);
            this.fileDiagButton.setDisable(!enable);
            this.fichierMoyButton.setDisable(!enable);
        }

        this.siteCutaneButton.setDisable(!enable);
        this.histologicLamellaButton.setDisable(!enable);

        if (all) {
            this.addButton.setDisable(!enable);
            this.returnButton.setDisable(!enable);
        }
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

    public void fichierMoyAction() {
        this.startDownload(this.selectedLesion.getFichierMoy(), this.fileDiagButton);
    }

    public void addAction() {
        this.setStage(this.addButton);

        new AddLesionView(this, null, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    public void editAction() {
        this.setStage(this.editButton);

        new AddLesionView(this, this.selectedLesion, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    public void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer la lesion et les documents attachés");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            this.enableButtons(false, true);
            LesionsController.remove(new RemoveTask(this, this.fileManager).setParameters(this.removeButton), this.inclusion, this.selectedLesion);
            this.lesionsList.remove(this.selectedIdLesion);
            this.lesionsTable.getSelectionModel().clearSelection();
            this.selectedIdLesion = -1;
            this.enableButtons(true, true);
        } else alert.close();
    }

    public void siteCutaneAction() {
        this.setStage(this.siteCutaneButton);

        new SiteView(this.selectedLesion, this.connection, this.fileManager);

        this.stage.close();
    }

    public void histologicLamellaAction() {
        this.setStage(this.histologicLamellaButton);

        new LameView(this.connection, this.fileManager, this.selectedLesion, this.inclusion.getNumAnaPat());

        this.stage.close();
    }

    public void returnAction() {
        this.setStage(this.returnButton);
        this.fileManager.openFTPConnection();

        new InclusionsView(this.connection, this.fileManager);

        this.stage.close();
    }

    private void startDownload(String url, Button button) {
        this.startDownload(new ArrayList<String>() {{
            add(url);
        }}, button);
    }

    protected void endDownload() {
        this.enableButtons(true, true);
        this.progressBar.setVisible(false);
    }
}
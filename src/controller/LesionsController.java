package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import src.daoImpl.LesionDaoImpl;
import src.table.Inclusion;
import src.table.Lesion;
import src.utils.FileManager;
import src.view.AddLesionView;
import src.view.InclusionsView;
import src.view.SiteView;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LesionsController implements Initializable {
    @FXML
    Button returnButton;
    @FXML
    Button photosButton;
    @FXML
    Button siteCutaneButton;
    @FXML
    Button histologicLamellaButton;
    @FXML
    Button addButton;
    @FXML
    Button removeButton;
    @FXML
    Button editButton;
    @FXML
    TableView<Lesion> lesionsTable;
    @FXML
    TableColumn<Lesion, String> lesionSite;
    @FXML
    TableColumn<Lesion, String> lesionDiag;

    private Connection connection;
    private FileManager fileManager;
    private Inclusion inclusion;
    private Lesion selectedLesion;
    private int selectedIdLesion;
    private ObservableList<Lesion> lesionsList;
    private LesionDaoImpl lesionDaoImpl;
    private Stage lesionsStage;

    public LesionsController(Connection connection, FileManager fileManager, Inclusion inclusion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.inclusion = inclusion;
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

                if (this.selectedLesion == null) {
                    this.removeButton.setDisable(true);
                    this.editButton.setDisable(true);
                    this.photosButton.setDisable(true);
                    this.siteCutaneButton.setDisable(true);
                    this.histologicLamellaButton.setDisable(true);
                } else {
                    this.removeButton.setDisable(false);
                    this.editButton.setDisable(false);
                    this.photosButton.setDisable(false);
                    this.siteCutaneButton.setDisable(false);
                    this.histologicLamellaButton.setDisable(false);
                }
            }
        });
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

    public void photosAction() {
        String photoHors = this.selectedLesion.getPhotoHors();
        String photoSur = this.selectedLesion.getPhotoSur();
        String photoFixe = this.selectedLesion.getPhotoFixe();
        File choosenDirectory = null;

        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.photosButton.getScene().getWindow();

        this.fileManager.openFTPConnection();

        if (photoHors != null)
            choosenDirectory = this.fileManager.downloadFromUrl(this.lesionsStage, photoHors, null, false, false);

        if (photoSur != null)
            choosenDirectory = this.fileManager.downloadFromUrl(this.lesionsStage, photoHors, choosenDirectory, false, false);

        if (photoFixe != null)
            this.fileManager.downloadFromUrl(this.lesionsStage, photoFixe, choosenDirectory, false, false);

        this.fileManager.closeFTPConnection();
    }

    public void addAction() {
        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.addButton.getScene().getWindow();

        new AddLesionView(this, null, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    public void editAction() {
        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.addButton.getScene().getWindow();

        new AddLesionView(this, this.selectedLesion, Integer.parseInt(this.inclusion.getId()), this.lesionDaoImpl, this.fileManager);
    }

    public void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer la lesion et les documents attachés");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            this.removeFiles();
            this.lesionDaoImpl.delete(this.selectedLesion.getId());
            this.lesionsList.remove(this.selectedIdLesion);
            this.lesionsTable.getSelectionModel().clearSelection();
        } else alert.close();
    }

    private void removeFiles() {
        String photoSurUrl = this.selectedLesion.getPhotoSur();
        String photoHorsUrl = this.selectedLesion.getPhotoHors();
        String photoFixeUrl = this.selectedLesion.getPhotoFixe();
        String diagFileUrl = this.selectedLesion.getAutreDiag();
        String fichierMoyUrl = this.selectedLesion.getFichierMoy();

        if (!photoSurUrl.equals("Aucun"))
            this.fileManager.removeFile(photoSurUrl);
        if (!photoHorsUrl.equals("Aucun"))
            this.fileManager.removeFile(photoHorsUrl);
        if (!photoFixeUrl.equals("Aucun"))
            this.fileManager.removeFile(photoFixeUrl);
        if (!diagFileUrl.equals("Aucun"))
            this.fileManager.removeFile(diagFileUrl);
        if (!fichierMoyUrl.equals("Aucun"))
            this.fileManager.removeFile(fichierMoyUrl);
    }

    public void siteCutaneAction() {
        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.histologicLamellaButton.getScene().getWindow();

        new SiteView(this.selectedLesion, this.connection, this.fileManager);

        this.lesionsStage.close();
    }

    public void histologicLamellaAction() {
        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.histologicLamellaButton.getScene().getWindow();

        //new LameView(this.selectedLesion, this.connection, this.fileManager);

        this.lesionsStage.close();
    }

    public void returnAction() {
        if (this.lesionsStage == null)
            this.lesionsStage = (Stage) this.returnButton.getScene().getWindow();

        this.fileManager.openFTPConnection();

        new InclusionsView(this.connection, this.fileManager);

        this.lesionsStage.close();
    }
}
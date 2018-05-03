package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.daoImpl.InclusionDaoImpl;
import src.daoImpl.LesionDaoImpl;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;
import src.view.AddDiagView;

import java.net.URL;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class AddLesionController implements Initializable, Observer {
    @FXML
    TextField siteAnatomiqueField;
    @FXML
    ChoiceBox<Diag> diagBox;
    @FXML
    Button addMoyFileButton;
    @FXML
    Button addPhotoSurButton;
    @FXML
    Button addPhotoHorsButton;
    @FXML
    Button addPhotoFixeButton;
    @FXML
    Button addDiagFileButton;
    @FXML
    Button otherDiagButton;
    @FXML
    Button cancelButton;
    @FXML
    Button addButton;
    @FXML
    Label diagFileLabel;
    @FXML
    Label addMoyFileLabel;
    @FXML
    Label photoSurLabel;
    @FXML
    Label photoHorsLabel;
    @FXML
    Label photoFixeLabel;
    @FXML
    Label progressLabel;
    @FXML
    ProgressBar progressBar;

    private LesionsController lesionsController;
    private Lesion lesion;
    private int idInclusion;
    private Stage addLesionStage;
    private InclusionDaoImpl inclusionDaoImpl;
    private LesionDaoImpl lesionDaoImpl;
    private FileManager fileManager;
    private String idLesion;
    private boolean[] uploadedBeforeEdit = {true, true, true, true, true};

    public AddLesionController(LesionsController lesionsController, Lesion lesion, int idInclusion, InclusionDaoImpl inclusionDaoImpl, LesionDaoImpl lesionDaoImpl, FileManager fileManager) {
        this.lesionsController = lesionsController;
        this.lesion = lesion;
        this.idInclusion = idInclusion;
        this.inclusionDaoImpl = inclusionDaoImpl;
        this.lesionDaoImpl = lesionDaoImpl;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Diag> diags = FXCollections.observableArrayList();
        Collections.addAll(diags, Diag.values());
        this.diagBox.setItems(diags);
        fileManager.addObserver(this);

        if (this.lesion != null)
            this.setLesionInformations();
        else {
            this.idLesion = Integer.toString(lesionDaoImpl.getLastId() + 1);
            this.lesion = new Lesion();
            this.lesion.setId(-1);
        }

        this.siteAnatomiqueField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                if (this.siteAnatomiqueField.getText().length() >= 1)
                    this.enableButtons();
                else this.disableButtons();
            }
        });

        this.siteAnatomiqueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z]*"))
                this.siteAnatomiqueField.setText(newValue.replaceAll("[^A-Za-z]", ""));
        });
    }

    private void enableButtons() {
        this.addButton.setDisable(false);
        this.addPhotoSurButton.setDisable(false);
        this.addPhotoHorsButton.setDisable(false);
        this.addPhotoFixeButton.setDisable(false);
        this.addDiagFileButton.setDisable(false);
        this.addMoyFileButton.setDisable(false);
    }

    private void disableButtons() {
        this.addButton.setDisable(true);
        this.addPhotoSurButton.setDisable(true);
        this.addPhotoHorsButton.setDisable(true);
        this.addPhotoFixeButton.setDisable(true);
        this.addDiagFileButton.setDisable(true);
        this.addMoyFileButton.setDisable(true);
    }

    private void setLesionInformations() {
        Diag diag = this.lesion.getDiag();
        this.addButton.setText("Modifier");
        this.siteAnatomiqueField.setText(this.lesion.getSiteAnatomique());
        this.diagBox.getSelectionModel().select(this.lesion.getDiag());
        this.idLesion = Integer.toString(this.lesion.getId());
        this.enableButtons();

        if (diag != null)
            if (diag.equals(Diag.FICHIER) || diag.equals(Diag.AUTRE))
                this.diagBox.setDisable(true);

        if (!this.lesion.getPhotoSur().equals("Aucun")) {
            this.photoSurLabel.setText(FileManager.getFileName(this.lesion.getPhotoSur(), false));
            this.addPhotoSurButton.setText("Supprimer");
        }

        if (!this.lesion.getPhotoHors().equals("Aucun")) {
            this.photoHorsLabel.setText(FileManager.getFileName(this.lesion.getPhotoHors(), false));
            this.addPhotoHorsButton.setText("Supprimer");
        }

        if (!this.lesion.getPhotoFixe().equals("Aucun")) {
            this.photoFixeLabel.setText(FileManager.getFileName(this.lesion.getPhotoFixe(), false));
            this.addPhotoFixeButton.setText("Supprimer");
        }

        if (!this.lesion.getFileDiag().equals("Aucun")) {
            this.diagFileLabel.setText(FileManager.getFileName(this.lesion.getFileDiag(), false));
            this.addDiagFileButton.setText("Supprimer");
        }

        if (!this.lesion.getFichierMoy().equals("Aucun")) {
            this.addMoyFileLabel.setText(FileManager.getFileName(this.lesion.getFichierMoy(), false));
            this.addMoyFileButton.setText("Supprimer");
        }
    }

    public void addAction() {
        /*TODO Faire que les fichiers s'add et se suppr au moment de quitter la fenetre*/
        this.lesion.setSiteAnatomique(this.siteAnatomiqueField.getText());
        Diag diagValue = this.diagBox.getValue();

        if (diagValue != null) {
            this.inclusionDaoImpl.updateDiag(diagValue.toString(), idInclusion);
            this.lesion.setDiag(diagValue.toString());
        }

        this.lesion.setIdInclusion(this.idInclusion);

        if (!this.photoSurLabel.getText().equals("Aucun"))
            this.lesion.setPhotoSur(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoSurLabel.getText());

        if (!this.photoHorsLabel.getText().equals("Aucun"))
            this.lesion.setPhotoHors(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoHorsLabel.getText());

        if (!this.photoFixeLabel.getText().equals("Aucun"))
            this.lesion.setPhotoFixe(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoFixeLabel.getText());

        if (!this.diagFileLabel.getText().equals("Aucun"))
            this.lesion.setFileDiag(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.diagFileLabel.getText());

        if (!this.addMoyFileLabel.getText().equals("Aucun"))
            this.lesion.setFichierMoy(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.addMoyFileLabel.getText());

        if (this.lesion.getId() >= 0) {
            this.lesionDaoImpl.update(this.lesion, this.lesion.getId());
            this.lesionsController.refreshLesions();
        } else {
            this.lesionDaoImpl.insert(this.lesion);
            this.lesionsController.populateLesions(this.lesion);
        }

        if (this.addLesionStage == null)
            this.addLesionStage = (Stage) this.addButton.getScene().getWindow();

        this.addLesionStage.close();
    }

    public void cancelAction() {
        if (!this.photoSurLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[0])
            this.fileManager.removeFile(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoSurLabel.getText());

        if (!this.photoHorsLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[1])
            this.fileManager.removeFile(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoHorsLabel.getText());

        if (!this.photoFixeLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[2])
            this.fileManager.removeFile(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoFixeLabel.getText());

        if (!this.otherDiagButton.getText().equals("Aucun") && !this.uploadedBeforeEdit[3])
            this.fileManager.removeFile(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.diagFileLabel.getText());

        if (!this.addMoyFileLabel.getText().equals("Aucun") && !this.uploadedBeforeEdit[4])
            this.fileManager.removeFile(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.addMoyFileLabel.getText());

        if (this.addLesionStage == null)
            this.addLesionStage = (Stage) this.cancelButton.getScene().getWindow();

        this.addLesionStage.close();
    }

    public void otherDiagAction() {
        if (this.addLesionStage == null)
            this.addLesionStage = (Stage) this.otherDiagButton.getScene().getWindow();

        new AddDiagView(this, this.lesion);
    }

    public void addPhotoSurAction() {
        if (this.photoSurLabel.getText().equals("Aucun")) {
            if (this.addLesionStage == null)
                this.addLesionStage = (Stage) this.addPhotoSurButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addLesionStage, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

            if (addedFileName != null) {
                this.addPhotoSurButton.setText("Supprimer");
                this.photoSurLabel.setText(addedFileName);
                this.lesion.setPhotoSur(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName);
            }
        } else {
            this.fileManager.removeFile(this.lesion.getPhotoSur());
            this.addPhotoSurButton.setText("Ajouter");
            this.lesion.setPhotoSur("Aucun");
            this.photoSurLabel.setText("Aucun");
        }
    }

    public void addPhotoHorsAction() {
        if (this.photoHorsLabel.getText().equals("Aucun")) {
            if (this.addLesionStage == null)
                this.addLesionStage = (Stage) this.addPhotoHorsButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addLesionStage, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

            if (addedFileName != null) {
                this.addPhotoHorsButton.setText("Supprimer");
                this.photoHorsLabel.setText(addedFileName);
                this.lesion.setPhotoHors(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName);
            }
        } else {
            this.fileManager.removeFile(this.lesion.getPhotoHors());
            this.addPhotoHorsButton.setText("Ajouter");
            this.photoHorsLabel.setText("Aucun");
            this.lesion.setPhotoHors("Aucun");
        }
    }

    public void addPhotoFixeAction() {
        if (this.photoFixeLabel.getText().equals("Aucun")) {
            if (this.addLesionStage == null)
                this.addLesionStage = (Stage) this.addPhotoFixeButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addLesionStage, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

            if (addedFileName != null) {
                this.addPhotoFixeButton.setText("Supprimer");
                this.photoFixeLabel.setText(addedFileName);
                this.lesion.setPhotoFixe(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName);
            }
        } else {
            this.fileManager.removeFile(this.lesion.getPhotoFixe());
            this.addPhotoFixeButton.setText("Ajouter");
            this.photoFixeLabel.setText("Aucun");
            this.lesion.setPhotoFixe("Aucun");
        }
    }

    public void addDiagFileAction() {
        if (this.diagFileLabel.getText().equals("Aucun")) {
            if (this.addLesionStage == null)
                this.addLesionStage = (Stage) this.addDiagFileButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addLesionStage, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

            if (addedFileName != null) {
                this.addDiagFileButton.setText("Supprimer");
                this.diagFileLabel.setText(addedFileName);
                this.lesion.setFileDiag(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName);
                this.diagBox.setValue(Diag.FICHIER);
                this.diagBox.setDisable(true);
            }
        } else {
            this.fileManager.removeFile(this.lesion.getFileDiag());
            this.addDiagFileButton.setText("Ajouter");
            this.diagFileLabel.setText("Aucun");
            this.diagBox.setValue(null);
            this.diagBox.setDisable(false);
            this.lesion.setFileDiag("Aucun");
        }
    }

    public void addMoyFileAction() {
        if (this.addMoyFileLabel.getText().equals("Aucun")) {
            if (this.addLesionStage == null)
                this.addLesionStage = (Stage) this.addMoyFileButton.getScene().getWindow();

            String addedFileName = this.fileManager.uploadToURL(this.addLesionStage, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

            if (addedFileName != null) {
                this.addMoyFileButton.setText("Supprimer");
                this.addMoyFileLabel.setText(addedFileName);
                this.lesion.setFichierMoy(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName);
            }
        } else {
            this.fileManager.removeFile(this.lesion.getFichierMoy());
            this.addMoyFileButton.setText("Ajouter");
            this.addMoyFileLabel.setText("Aucun");
            this.lesion.setFichierMoy("Aucun");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        double size = Double.longBitsToDouble(((FileManager) o).getSize());
        double bytes = Double.longBitsToDouble(((FileManager) o).getBytes());

        System.out.println(((FileManager) o).getSize());

        if (size >= 0) {
            double progress = bytes / size;

            this.disableButtons();
            this.addButton.setDisable(true);
            this.cancelButton.setDisable(true);
            this.progressBar.setProgress(bytes / size);
            this.progressLabel.setText(progress * 100 + " %");

            System.out.println("Uploaded " + progress + " bytes");
        } else {
            this.progressBar.setProgress(0);
            this.progressLabel.setText("");
            this.enableButtons();
            this.addButton.setDisable(false);
            this.cancelButton.setDisable(false);
        }
    }
}

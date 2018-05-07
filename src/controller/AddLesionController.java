package src.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.daoImpl.LesionDaoImpl;
import src.table.Lesion;
import src.utils.Diag;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.utils.UploadTask;
import src.view.AddDiagView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class AddLesionController extends Controller implements Initializable {
    @FXML
    TextField siteAnatomiqueField;
    @FXML
    ChoiceBox<Diag> diagBox;
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
    Label photoSurLabel;
    @FXML
    Label photoHorsLabel;
    @FXML
    Label photoFixeLabel;

    private LesionsController lesionsController;
    private Lesion lesion;
    private int idInclusion;
    private LesionDaoImpl lesionDaoImpl;
    private String idLesion;

    public AddLesionController(LesionsController lesionsController, Lesion lesion, int idInclusion, LesionDaoImpl lesionDaoImpl, FileManager fileManager) {
        this.lesionsController = lesionsController;
        this.lesion = lesion;
        this.idInclusion = idInclusion;
        this.lesionDaoImpl = lesionDaoImpl;
        this.fileManager = fileManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Diag> diags = FXCollections.observableArrayList();
        Collections.addAll(diags, Diag.values());
        this.diagBox.setItems(diags);

        if (this.lesion != null)
            this.setLesionInformations();
        else {
            this.idLesion = Integer.toString(lesionDaoImpl.getLastId() + 1);
            this.lesion = new Lesion();
            this.lesion.setId(-1);
        }

        this.siteAnatomiqueField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                this.enableButtons(this.siteAnatomiqueField.getText().length() >= 1, false);
            }
        });

        this.siteAnatomiqueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z]*"))
                this.siteAnatomiqueField.setText(newValue.replaceAll("[^A-Za-z]", ""));
        });
    }

    public void enableButtons(boolean enable, boolean all) {
        this.addButton.setDisable(!enable);
        this.addPhotoSurButton.setDisable(!enable);
        this.addPhotoHorsButton.setDisable(!enable);
        this.addPhotoFixeButton.setDisable(!enable);
        this.addDiagFileButton.setDisable(!enable);

        if (all) {
            this.addButton.setDisable(!enable);
            this.cancelButton.setDisable(!enable);
        }
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }

    private void setLesionInformations() {
        Diag diag = this.lesion.getDiag();
        this.addButton.setText("Modifier");
        this.siteAnatomiqueField.setText(this.lesion.getSiteAnatomique());
        this.diagBox.getSelectionModel().select(diag);
        this.idLesion = Integer.toString(this.lesion.getId());
        this.enableButtons(true, false);

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
    }

    public void addAction() {
        this.lesion.setSiteAnatomique(this.siteAnatomiqueField.getText());
        Diag diagValue = this.diagBox.getValue();

        if (diagValue != null)
            this.lesion.setDiag(diagValue.toString());

        this.lesion.setIdInclusion(this.idInclusion);

        if (!this.photoSurLabel.getText().equals("Aucun"))
            this.lesion.setPhotoSur(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoSurLabel.getText());

        if (!this.photoHorsLabel.getText().equals("Aucun"))
            this.lesion.setPhotoHors(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoHorsLabel.getText());

        if (!this.photoFixeLabel.getText().equals("Aucun"))
            this.lesion.setPhotoFixe(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.photoFixeLabel.getText());

        if (!this.diagFileLabel.getText().equals("Aucun"))
            this.lesion.setFileDiag(FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + this.diagFileLabel.getText());

        if (this.lesion.getId() >= 0) {
            this.lesionDaoImpl.update(this.lesion, this.lesion.getId());
            this.lesionsController.refreshLesions();
        } else {
            this.lesionDaoImpl.insert(this.lesion);
            this.lesionsController.populateLesions(this.lesion);
        }

        this.setStage(this.addButton);
        this.stage.close();
    }

    public void cancelAction() {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.cancelButton, null, this.progressBar, this.progressLabel);
        ArrayList<String> files = new ArrayList<>();
        String directory = FileManager.getLesionFilesDirectoryName(this.idLesion) + "//";
        String photoSur, photoHors, photoFixe, otherDiag;

        if (!this.addButton.getText().equals("Modifier")) {
            if (!(photoSur = this.photoSurLabel.getText()).equals("Aucun"))
                files.add(directory + photoSur);

            if (!(photoHors = this.photoHorsLabel.getText()).equals("Aucun"))
                files.add(directory + photoHors);

            if (!(photoFixe = this.photoFixeLabel.getText()).equals("Aucun"))
                files.add(directory + photoFixe);

            if (!(otherDiag = this.diagFileLabel.getText()).equals("Aucun"))
                files.add(directory + otherDiag);
        }

        removeTask.addUrls(files);

        new Thread(removeTask).start();
        this.stage.close();
    }

    public void otherDiagAction() {
        this.setStage(this.otherDiagButton);
        new AddDiagView(this.stage, this, this.lesion);
    }

    public void addPhotoSurAction() {
        if (this.photoSurLabel.getText().equals("Aucun"))
            this.startUpload("sur", this.addPhotoSurButton, this.photoSurLabel);
        else this.removeFileFromFTP("sur", this.addPhotoSurButton, this.photoSurLabel);
    }

    public void addPhotoHorsAction() {
        if (this.photoHorsLabel.getText().equals("Aucun"))
            this.startUpload("hors", this.addPhotoHorsButton, this.photoHorsLabel);
        else this.removeFileFromFTP("hors", this.addPhotoHorsButton, this.photoHorsLabel);
    }

    public void addPhotoFixeAction() {
        if (this.photoFixeLabel.getText().equals("Aucun"))
            this.startUpload("fixe", this.addPhotoFixeButton, this.photoFixeLabel);
        else this.removeFileFromFTP("fixe", this.addPhotoFixeButton, this.photoFixeLabel);
    }

    public void addDiagFileAction() {
        if (this.diagFileLabel.getText().equals("Aucun"))
            this.startUpload("diag", this.addDiagFileButton, this.diagFileLabel);
        else this.removeFileFromFTP("diag", this.addDiagFileButton, this.diagFileLabel);
    }

    private void removeFileFromFTP(String buttonName, Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        switch (buttonName) {
            case "sur":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(lesion.getPhotoSur());
                }});

                this.photoSurLabel.setText("Aucun");
                break;
            case "hors":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(lesion.getPhotoHors());
                }});

                this.lesion.setPhotoHors("Aucun");
                break;
            case "fixe":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(lesion.getPhotoFixe());
                }});

                this.lesion.setPhotoFixe("Aucun");
                break;
            case "diag":
                removeTask.addUrls(new ArrayList<String>() {{
                    add(lesion.getFileDiag());
                }});

                this.diagBox.setValue(null);
                this.diagBox.setDisable(false);
                this.lesion.setFileDiag("Aucun");
                break;
        }

        removeTask.setOnSucceeded(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        removeTask.setOnFailed(e -> {
            button.setText("Ajouter");
            label.setText("Aucun");
            this.enableButtons(true, true);
        });

        this.lesionDaoImpl.update(lesion, lesion.getId());
        new Thread(removeTask).start();
    }

    private void startUpload(String buttonName, Button button, Label label) {
        UploadTask uploadTask = new UploadTask(this.fileManager, FileManager.getLesionFilesDirectoryName(this.idLesion), null);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        uploadTask.setOnSucceeded(e -> this.endUpload(buttonName, uploadTask.getAddedFileName(), button, label));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    private void endUpload(String buttonName, String addedFileName, Button button, Label label) {
        if (addedFileName != null) {
            button.setText("Supprimer");
            label.setText(addedFileName);
            String url = FileManager.getLesionFilesDirectoryName(this.idLesion) + "//" + addedFileName;

            switch (buttonName) {
                case "sur":
                    this.lesion.setPhotoSur(url);
                    break;
                case "hors":
                    this.lesion.setPhotoHors(url);
                    break;
                case "fixe":
                    this.lesion.setPhotoFixe(url);
                    break;
                case "diag":
                    this.lesion.setFileDiag(url);
                    this.diagBox.setValue(Diag.FICHIER);
                    this.diagBox.setDisable(true);
                    break;
            }
        }

        this.enableButtons(true, true);
    }
}

package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import src.daoImpl.LameHistologiqueDaoImpl;
import src.table.HistologicLamella;
import src.table.Lesion;
import src.utils.FileManager;
import src.utils.RemoveTask;
import src.utils.UploadTask;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddLameController extends Controller implements Initializable {
    @FXML
    TextField lamellaNum;

    @FXML
    TextField cutArea;

    @FXML
    TextField blackOrientation;

    @FXML
    TextField greenOrientation;

    @FXML
    TextField coloration;

    @FXML
    Button addPictureButton;

    @FXML
    Button cancelButton;

    @FXML
    Button addButton;

    @FXML
    Label photoLabel;

    private LameHistologiqueDaoImpl lameHistologiqueDaoImpl;
    private Lesion lesion;
    private HistologicLamella lame;
    private LameController lameController;
    private int idLame;

    public AddLameController(LameController lameController, Connection connection, FileManager fileManager, Lesion lesion, HistologicLamella lame) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.lesion = lesion;
        this.lame = lame;
        this.lameController = lameController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);

        if (this.lame != null) {
            this.setLameInformations();
        } else {
            this.idLame = this.getIdLast();
            this.lame = new HistologicLamella();
            this.lame.setId(-1);
        }

        this.greenOrientation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.greenOrientation.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.greenOrientation.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.greenOrientation.getText().length() >= 3)
                    this.greenOrientation.setText(this.greenOrientation.getText().substring(0, 3));
        });

        this.blackOrientation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                this.blackOrientation.setText(newValue.replaceAll("[^\\d]", ""));
        });

        this.blackOrientation.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue())
                if (this.blackOrientation.getText().length() >= 3)
                    this.blackOrientation.setText(this.blackOrientation.getText().substring(0, 3));
        });
    }

    private void setLameInformations() {
        this.addButton.setText("Modifier");
        this.enableButtons(true, false);
        this.idLame = this.lame.getId();

        if (this.lame.getPhoto() != null) {
            this.photoLabel.setText(FileManager.getFileName(this.lame.getPhoto(), false));
            this.addPictureButton.setText("Supprimer");

        }

        this.lamellaNum.setText(this.lame.getNumLame());
        this.cutArea.setText(this.lame.getSiteCoupe());
        this.blackOrientation.setText(Integer.toString(this.lame.getOrientationNoir()));
        this.greenOrientation.setText(Integer.toString(this.lame.getOrientationVert()));
        this.coloration.setText(this.lame.getColoration());
    }

    @FXML
    private void cancelButtonAction() {
        this.setStage(this.cancelButton);

        if (!this.addButton.getText().equals("Modifier")) {
            RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(this.cancelButton, null, this.progressBar, this.progressLabel);
            ArrayList<String> files = new ArrayList<>();

            if (!(this.photoLabel.getText()).equals("Aucun"))
                files.add(FileManager.getLameFilesDirectoryName(Integer.toString(this.idLame)) + "//" + this.photoLabel.getText());

            removeTask.addUrls(files);

            new Thread(removeTask).start();
        }

        this.stage.close();
    }

    @FXML
    private void accepteButtonAction() {
        this.lame.setIdLesion(this.lesion.getId());
        this.lame.setNumLame(this.lamellaNum.getText());
        this.lame.setSiteCoupe(this.cutArea.getText());
        this.lame.setOrientationNoir(blackOrientation.getText().length() > 0 ? Integer.parseInt(this.blackOrientation.getText()) : 0);
        this.lame.setOrientationVert(greenOrientation.getText().length() > 0 ? Integer.parseInt(this.greenOrientation.getText()) : 0);
        this.lame.setColoration(this.coloration.getText());

        if (!this.photoLabel.getText().equals("Aucun"))
            this.lame.setPhoto(FileManager.getLameFilesDirectoryName(Integer.toString(this.idLame)) + "//" + this.photoLabel.getText());

        if (this.lame.getId() != -1) {
            this.lameHistologiqueDaoImpl.update(this.lame, this.lame.getId());
            this.lameController.refreshLesions();
        } else {
            this.lame.setId(this.idLame);
            this.lameHistologiqueDaoImpl.insert(this.lame);
            this.lameController.populateSingleLame(this.lame);
        }

        this.lameController.tab.getSelectionModel().clearSelection();
        this.lameController.enableButtons(false, false);

        this.setStage(this.addButton);
        this.stage.close();
    }

    @FXML
    public void photoButtonAction() {
        if (this.photoLabel.getText().equals("Aucun"))
            this.startUpload(this.addPictureButton, photoLabel);
        else this.removeFileFromFTP(this.addPictureButton, this.photoLabel);
    }

    private void removeFileFromFTP(Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        removeTask.addUrls(new ArrayList<String>() {{
            add(lame.getPhoto());
        }});

        this.photoLabel.setText("Aucun");
        this.lame.setPhoto(null);

        removeTask.setOnSucceeded(e -> {
            super.endRemove(null, this.progressBar, this.progressLabel);
            button.setText("Ajouter");
            label.setText("Aucun");
        });

        removeTask.setOnFailed(e -> removeTask.getOnSucceeded());

        this.lameHistologiqueDaoImpl.update(lame, lame.getId());
        new Thread(removeTask).start();
    }

    private void startUpload(Button button, Label label) {
        String directory = FileManager.getLameFilesDirectoryName(Integer.toString(this.idLame));
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, null);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        this.progressBar.setVisible(true);
        this.progressLabel.setVisible(true);
        uploadTask.setOnSucceeded(e -> this.endUpload(directory, uploadTask.getAddedFileName(), this.addPictureButton, label));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel) {
        this.enableButtons(false, true);

        if (button != null)
            button.setVisible(true);

        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        this.addButton.setDisable(!enable);

        if (all) {
            this.addPictureButton.setDisable(!enable);
            this.cancelButton.setDisable(!enable);
        }
    }

    void endUpload(String directory, String addedFileName, Button button, Label label) {
        if (addedFileName != null) {
            button.setText("Supprimer");
            label.setText(addedFileName);
            this.lame.setPhoto(directory + "//" + addedFileName);
        }

        this.progressBar.setVisible(false);
        this.progressLabel.setVisible(false);
        this.enableButtons(true, true);
    }


    private int getIdLast() {
        ArrayList<Integer> ints = lameHistologiqueDaoImpl.idList();
        int i = 0;

        while (ints.contains(i))
            i++;

        return i;
    }
}
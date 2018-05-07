package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private String numAnapat;
    private HistologicLamella histologicLamella;
    private String photoPath;
    private LameController lameController;

    public AddLameController(LameController lameController, Connection connection, FileManager fileManager, Lesion lesion, HistologicLamella histologicLamella, String numAnapat) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.lesion = lesion;
        this.histologicLamella = histologicLamella;
        this.numAnapat = numAnapat;
        this.lameController = lameController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (histologicLamella == null)
            lamellaNum.lengthProperty().addListener((observable, oldValue, newValue) -> this.enableButtons(lamellaNum.getText().length() > 0, true));

        if (this.histologicLamella != null) {
            this.addButton.setText("Modifier");

            if (this.histologicLamella.getPhoto() != null)
                this.addPictureButton.setText("Supprimer");
            else {
                String[] s0 = this.histologicLamella.getPhoto().split("//");
                this.photoLabel.setText(s0[3]);
            }

            this.lamellaNum.setText(this.histologicLamella.getNumLame());
            this.cutArea.setText(this.histologicLamella.getSiteCoupe());
            this.blackOrientation.setText(Integer.toString(this.histologicLamella.getOrientationNoir()));
            this.greenOrientation.setText(Integer.toString(this.histologicLamella.getOrientationVert()));
            this.coloration.setText(this.histologicLamella.getColoration());


        }

        this.photoLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Aucun")) {
                addPictureButton.setText("Supprimer");
            }
        });

        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);
    }

    @FXML
    private void cancelButtonAction() {
        this.setStage(this.cancelButton);
        this.stage.close();
    }

    @FXML
    private void accepteButtonAction() {
        Integer vert, noir;
        String numLame, cut, coloration;

        if (this.histologicLamella == null) {

            numLame = this.lamellaNum.getText();
            cut = this.cutArea.getText();

            if (greenOrientation.getText().length() > 0)
                vert = Integer.parseInt(this.greenOrientation.getText());
            else vert = 0;

            if (blackOrientation.getText().length() > 0)
                noir = Integer.parseInt(this.blackOrientation.getText());
            else noir = 0;

            coloration = this.coloration.getText();

            if (photoLabel.getText().equals("Aucun"))
                photoPath = null;

            HistologicLamella newLame = new HistologicLamella(lesion.getId(), numLame, cut, noir, vert, coloration, photoPath);

            this.lameHistologiqueDaoImpl.insert(newLame);
            this.lameController.populateSingleLame(newLame);
        } else {
            numLame = this.lamellaNum.getText().length() > 0 ? this.lamellaNum.getText() : this.histologicLamella.getNumLame();
            cut = this.cutArea.getText().length() > 0 ? this.cutArea.getText() : this.histologicLamella.getSiteCoupe();
            vert = this.greenOrientation.getText().length() > 0 ? Integer.parseInt(this.greenOrientation.getText()) : this.histologicLamella.getOrientationVert();
            noir = this.blackOrientation.getText().length() > 0 ? Integer.parseInt(this.blackOrientation.getText()) : this.histologicLamella.getOrientationNoir();
            coloration = this.coloration.getText().length() > 0 ? this.coloration.getText() : this.histologicLamella.getColoration();

            if (photoPath == null) {
                if (this.histologicLamella.getPhoto() != null)
                    photoPath = this.histologicLamella.getPhoto();
                else
                    photoPath = null;
            }

            if (photoLabel.getText().equals("Aucun"))
                photoPath = null;

            HistologicLamella newLame = new HistologicLamella(lesion.getId(), numLame, cut, noir, vert, coloration, photoPath);
            this.lameHistologiqueDaoImpl.update(newLame, this.histologicLamella.getId());
            this.lameController.refreshLesions();
        }


        this.setStage(this.addButton);
        this.stage.close();
    }

    @FXML
    public void photoButtonAction() {


        if (this.histologicLamella != null) {
            if (this.photoLabel.getText().equals("Aucun"))
                this.startUpload(this.addPictureButton, photoLabel, "//lame_histologique//" + Integer.toString(this.histologicLamella.getId()) + "//");
            else {
                this.removeFileFromFTP(this.addPictureButton, this.photoLabel);
            }

        } else if (this.photoLabel.getText().equals("Aucun"))
            this.startUpload(this.addPictureButton, photoLabel, "//lame_histologique//" + Integer.toString(lameHistologiqueDaoImpl.getLastid()) + "//");
        else this.removeFileFromFTP(this.addPictureButton, this.photoLabel);
    }

    private void removeFileFromFTP(Button button, Label label) {
        RemoveTask removeTask = new RemoveTask(this, this.fileManager).setParameters(button, null, this.progressBar, this.progressLabel);

        removeTask.addUrls(new ArrayList<String>() {{
            add(histologicLamella.getPhoto());
        }});

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

        this.lameHistologiqueDaoImpl.update(histologicLamella, histologicLamella.getId());
        new Thread(removeTask).start();

    }

    private void startUpload(Button button, Label label, String directory) {
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, null);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        this.progressBar.setVisible(true);
        uploadTask.setOnSucceeded(e -> this.endUpload(uploadTask.getAddedFileName(), directory, label, 0));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        addButton.setDisable(!enable);

        if (all) {
            addPictureButton.setDisable(!enable);
            cancelButton.setDisable(!enable);
        }
    }

    @Override
    void endUpload(String addedFileName, String directory, Label label, int num) {
        if (addedFileName != null) {

            this.photoPath = directory + addedFileName;
            label.setText(addedFileName);
        }

        this.enableButtons(true, true);
    }
}
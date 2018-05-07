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
import src.utils.UploadTask;

import java.net.URL;
import java.sql.Connection;
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
        this.lameController=lameController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (histologicLamella == null)
            lamellaNum.lengthProperty().addListener((observable, oldValue, newValue) -> this.enableButtons(lamellaNum.getText().length() > 0, true));

        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);
    }

    @FXML
    private void cancelButtonAction() {
        this.setStage(this.cancelButton);
        this.stage.close();
    }

    @FXML
    private void accepteButtonAction() {
        Integer id, vert, noir;
        String cut, coloration;

        if (this.histologicLamella == null) {
            id = Integer.parseInt(numAnapat + lamellaNum.getText());
            cut = this.cutArea.getText();

            if(greenOrientation.getText().length()>0)
            vert = Integer.parseInt(this.greenOrientation.getText());
            else vert=0;

            if(blackOrientation.getText().length()>0)
            noir = Integer.parseInt(this.blackOrientation.getText());
            else noir=0;

            coloration = this.coloration.getText();

            HistologicLamella newLame = new HistologicLamella(id, lesion.getId(), cut, noir, vert, coloration, photoPath);

            this.lameHistologiqueDaoImpl.insert(newLame);
            this.lameController.populateSingleLame(newLame);
        } else {
            id = this.lamellaNum.getText().length() > 0 ? Integer.parseInt(numAnapat + lamellaNum.getText()) : this.histologicLamella.getId();
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

            HistologicLamella newLame = new HistologicLamella(id, lesion.getId(), cut, noir, vert, coloration, photoPath);
            this.lameHistologiqueDaoImpl.update(newLame, this.histologicLamella.getId());
            this.lameController.refreshLesions();
        }

        this.setStage(this.addButton);
        this.stage.close();
    }

    @FXML
    public void photoButtonAction() {
        if(this.histologicLamella!=null)
        this.startUpload(this.addPictureButton, photoLabel,"//lame_histologique//"+Integer.toString(this.histologicLamella.getId())+"//");
        else
            this.startUpload(this.addPictureButton, photoLabel,"//lame_histologique//"+numAnapat + lamellaNum.getText()+"//");


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
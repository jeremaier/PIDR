package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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


    private Connection connection;
    private Stage addLameStage;
    private LameHistologiqueDaoImpl lameHistologiqueDaoImpl;
    private FileManager fileManager;
    private Lesion lesion;
    private int numAnapat;
    private HistologicLamella histologicLamella;
    private String photoPath;

    public AddLameController(Connection connection, FileManager fileManager, Lesion lesion, HistologicLamella histologicLamella, int numAnapat) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.lesion = lesion;
        this.histologicLamella = histologicLamella;
        this.numAnapat = numAnapat;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (histologicLamella == null)
            lamellaNum.lengthProperty().addListener((observable, oldValue, newValue) -> this.enableButtons(lamellaNum.getText().length() > 0, false));

        this.lameHistologiqueDaoImpl = new LameHistologiqueDaoImpl(connection);
    }

    @FXML
    private void cancelButtonAction() {
        this.addLameStage = (Stage) cancelButton.getScene().getWindow();
        this.addLameStage.close();
    }

    @FXML
    private void accepteButtonAction() {
            int Id;
            String cut;
            int vert;
            int noir;
            String coloration;


        if(this.histologicLamella!=null){
            Id=Integer.parseInt(Integer.toString(numAnapat)+lamellaNum.getText());
            cut=this.cutArea.getText();
            vert=Integer.parseInt(this.greenOrientation.getText());
            noir=Integer.parseInt(this.blackOrientation.getText());
            coloration=this.coloration.getText();

            HistologicLamella newLame = new HistologicLamella(Id, lesion.getId(), cut,vert,noir, coloration, photoPath);
            this.lameHistologiqueDaoImpl.insert(newLame);

            if(this.addLameStage==null)
                this.addLameStage = (Stage) addButton.getScene().getWindow();
            this.addLameStage.close();
        }else{

            if(this.lamellaNum.getText().length()>0){
                Id=Integer.parseInt(Integer.toString(numAnapat)+lamellaNum.getText());
            }else{
                Id=this.histologicLamella.getId();
            }

            if(this.cutArea.getText().length()>0){
                cut=this.cutArea.getText();
            }else{
                cut=this.histologicLamella.getSiteCoupe();
            }

            if(this.greenOrientation.getText().length()>0){
                vert=Integer.parseInt(this.greenOrientation.getText());
            } else {
                vert=this.histologicLamella.getOrientationVert();
            }

            if(this.blackOrientation.getText().length()>0){
                noir=Integer.parseInt(this.blackOrientation.getText());
            }else {
                noir=this.histologicLamella.getOrientationNoir();
            }

            if(this.coloration.getText().length()>0){
                coloration=this.coloration.getText();
            }else{
                coloration=this.histologicLamella.getColoration();
            }

            if(photoPath==null)
                photoPath=this.histologicLamella.getPhoto();

            HistologicLamella newLame = new HistologicLamella(Id, lesion.getId(), cut,vert,noir, coloration, photoPath);
            this.lameHistologiqueDaoImpl.update(newLame, this.histologicLamella.getId());

            if(this.addLameStage==null)
                this.addLameStage = (Stage) addButton.getScene().getWindow();
            this.addLameStage.close();

        }
    }

    @FXML
    public void photoButtonAction() {
        this.startUpload(this.addPictureButton, photoLabel, histologicLamella != null ? "//lame_histologique//" : "//trancriptomie//", null);
    }

    private void startUpload(Button button, Label label, String directory, String mesure) {
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, mesure);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        uploadTask.setOnSucceeded(e -> this.endUpload(uploadTask.getAddedFileName(), directory, button, label));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    private void endUpload(String addedFileName, String directory, Button button, Label label) {
        if (addedFileName != null) {
            String path = histologicLamella != null ? String.valueOf(this.histologicLamella.getId()) : Integer.toString(numAnapat) + lamellaNum.getText();

            this.photoPath = directory + path + addedFileName;
            label.setText(addedFileName);
        }

        this.enableButtons(true, true);
    }

    @Override
    public void enableButtons(boolean enable, boolean all) {
        addButton.setDisable(!enable);

        if (all) {
            addPictureButton.setDisable(!enable);
            cancelButton.setDisable(!enable);
        }
    }
}
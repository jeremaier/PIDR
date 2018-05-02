package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.dao.LameHistologiqueDao;
import src.daoImpl.LameHistologiqueDaompl;
import src.table.HistologicLamella;
import src.table.Lesion;
import src.utils.FileManager;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddLameController implements Initializable {
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
    private LameHistologiqueDaompl lameHistologiqueDaompl;
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

        if (histologicLamella == null) {
            lamellaNum.lengthProperty().addListener((observable, oldValue, newValue) -> {
                if (lamellaNum.getText().length() > 0) {
                    addButton.setDisable(true);
                } else {
                    addButton.setDisable(false);
                }
            });
        }
        this.lameHistologiqueDaompl = new LameHistologiqueDaompl(connection);


    }

    @FXML
    private void cancelButtonAction(ActionEvent actionEvent) {
        this.addLameStage = (Stage) cancelButton.getScene().getWindow();
        this.addLameStage.close();
    }

    @FXML
    private void accepteButtonAction(ActionEvent actionEvent) {
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
            this.lameHistologiqueDaompl.insert(newLame);

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
            this.lameHistologiqueDaompl.update(newLame, this.histologicLamella.getId());

            if(this.addLameStage==null)
                this.addLameStage = (Stage) addButton.getScene().getWindow();
            this.addLameStage.close();

        }
    }

    @FXML
    public void photoButtonAction(ActionEvent actionEvent){
        String fileName;
        if (histologicLamella!= null) {
            fileName=fileManager.uploadToURL(addLameStage, "//lame_histologique//" + String.valueOf(this.histologicLamella.getId()), null);
            photoPath = "//lame_histologie//" + String.valueOf(this.histologicLamella.getId()) + "//" + fileName ;
            photoLabel.setText(fileName);

        }else {
            fileName=fileManager.uploadToURL(addLameStage, "trancriptomie//" + (Integer.toString(numAnapat)+lamellaNum.getText()), null);
            photoPath = "//trancriptomie//" + (Integer.toString(numAnapat)+lamellaNum.getText())+"//" + fileName;
            photoLabel.setText(fileName);
        }
    }
}
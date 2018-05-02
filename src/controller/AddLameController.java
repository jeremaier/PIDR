package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.dao.LameHistologiqueDao;
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
    Button addPictureButton;

    @FXML
    Button cancelButton;

    @FXML
    Button addButton;

    @FXML
    Label photoLabel;

    private Connection connection;
    private Stage stage;
    private LameHistologiqueDao lameHistologiqueDao;
    private FileManager fileManager;
    private Lesion lesion;
    private int numAnapat;
    private HistologicLamella histologicLamella;

    public AddLameController(Connection connection, FileManager fileManager, Lesion lesion, HistologicLamella histologicLamella, int numAnapat){
        this.connection=connection;
        this.fileManager= fileManager;
        this.lesion=lesion;
        this.histologicLamella =histologicLamella;
        this.numAnapat = numAnapat;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

    }
}
package src.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import src.table.Inclusion;
import src.utils.FileManager;

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

    private Connection connection;
    private FileManager fileManager;
    private Inclusion inclusion;

    public LesionsController(Connection connection, FileManager fileManager, Inclusion inclusion) {
        this.connection = connection;
        this.fileManager = fileManager;
        this.inclusion = inclusion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void siteCutaneAction(ActionEvent actionEvent) {
    }

    public void photosAction(ActionEvent actionEvent) {
    }

    public void addAction(ActionEvent actionEvent) {
    }

    public void editAction(ActionEvent actionEvent) {
    }

    public void removeAction(ActionEvent actionEvent) {
    }

    public void histologicLamellaAction(ActionEvent actionEvent) {
    }

    public void returnAction(ActionEvent actionEvent) {
    }
}
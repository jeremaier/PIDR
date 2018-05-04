package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import src.utils.DownloadTask;
import src.utils.FileManager;

import java.sql.Connection;
import java.util.ArrayList;

public abstract class Controller {
    @FXML
    public
    Button removeButton;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label progressLabel;

    protected Stage stage;
    protected Connection connection;
    protected FileManager fileManager;

    public void setStage(Button button) {
        if (this.stage == null)
            this.stage = (Stage) button.getScene().getWindow();
    }

    public abstract void enableButtons(boolean enable, boolean all);

    void startDownload(ArrayList<String> urls, Button button) {
        DownloadTask downloadTask = new DownloadTask(this.fileManager, urls);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.setVisible(true);
        this.progressBar.progressProperty().bind(downloadTask.progressProperty());
        this.progressLabel.textProperty().bind(downloadTask.messageProperty());
        downloadTask.setOnSucceeded(e -> this.endDownload());
        downloadTask.setOnFailed(e -> this.endDownload());
        downloadTask.setSelectedDirectory(FileManager.openDirectoryChooser(this.stage));

        new Thread(downloadTask).start();
    }

    protected void endDownload() {
        this.enableButtons(true, true);
        this.progressBar.setVisible(false);
    }

    public void endRemove() {
        this.endDownload();
    }
}

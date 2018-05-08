package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import src.utils.DownloadTask;
import src.utils.FileManager;
import src.utils.UploadTask;

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

    void startDownload(String url, Button button) {
        this.startDownload(new ArrayList<String>() {{
            add(url);
        }}, button);
    }

    void startUpload(Button button, Label label, String directory, String mesure, int num) {
        UploadTask uploadTask = new UploadTask(this.fileManager, directory, mesure);

        this.setStage(button);
        this.enableButtons(false, true);
        this.progressBar.progressProperty().bind(uploadTask.progressProperty());
        this.progressBar.setVisible(true);
        uploadTask.setOnSucceeded(e -> this.endUpload(uploadTask.getAddedFileName(), directory, label, num));

        FileManager.openFileChooser(this.stage, uploadTask);

        new Thread(uploadTask).start();
    }

    private void startFileDownload(DownloadTask downloadTask, Button button, ProgressBar progressBar, Label progressLabel) {
        if (button != null)
            button.setVisible(false);

        progressBar.setVisible(true);
        progressBar.progressProperty().bind(downloadTask.progressProperty());
        progressLabel.textProperty().bind(downloadTask.messageProperty());
        downloadTask.setOnSucceeded(e -> this.endDownload(button, progressBar, progressLabel));
        downloadTask.setOnFailed(e -> this.endDownload(button, progressBar, progressLabel));
        downloadTask.setSelectedDirectory(FileManager.openDirectoryChooser(this.stage));

        new Thread(downloadTask).start();
    }

    void startDownload(ArrayList<String> urls, Button stageButton) {
        this.startFileDownload(this.initDownload(urls, stageButton), null, this.progressBar, this.progressLabel);
    }

    void startDownload(ArrayList<String> urls, Button stageButton, Button downloadButton) {
        this.startFileDownload(this.initDownload(urls, stageButton), downloadButton, this.progressBar, this.progressLabel);
    }

    void startDocDownload(String url, Button button, ProgressBar progressBar, Label label) {
        this.startFileDownload(this.initDownload(new ArrayList<String>() {{
            add(url);
        }}, button), button, progressBar, label);
    }

    private DownloadTask initDownload(ArrayList<String> urls, Button button) {
        DownloadTask downloadTask = new DownloadTask(this.fileManager, urls);

        this.setStage(button);
        this.enableButtons(false, true);

        return downloadTask;
    }

    private void endDownload(Button button, ProgressBar progressBar, Label progressLabel) {
        this.enableButtons(true, true);

        if (button != null)
            button.setVisible(true);

        progressBar.setVisible(false);
        progressLabel.setVisible(false);
    }

    public void endRemove(Button button, ProgressBar progressBar, Label progressLabel) {
        this.endDownload(button, progressBar, progressLabel);
    }

    abstract void endUpload(String addedFileName, String directory, Label label, int num);
}

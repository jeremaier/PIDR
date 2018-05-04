package src.utils;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import src.controller.Controller;

import java.util.ArrayList;

public class RemoveTask extends Task<Void> {
    private Controller controller;
    private FileManager fileManager;
    private ArrayList<String> urls;

    public RemoveTask(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public RemoveTask(Controller controller, FileManager fileManager) {
        this.controller = controller;
        this.fileManager = fileManager;
    }

    @Override
    protected Void call() {
        this.fileManager.removeFiles(this, urls);

        return null;
    }

    void updateProgressBar() {
        this.updateProgress(-1, -1);
        this.updateMessage("Suppression...");
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public RemoveTask setParameters(Button button) {
        this.controller.setStage(button);
        this.controller.enableButtons(false, true);
        this.controller.progressBar.setVisible(true);
        this.controller.progressBar.progressProperty().bind(this.progressProperty());
        this.controller.progressLabel.textProperty().bind(this.messageProperty());

        this.setOnSucceeded(e -> this.controller.endRemove());
        this.setOnFailed(e -> this.controller.endRemove());

        return this;
    }
}

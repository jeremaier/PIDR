package src.utils;

import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;

public class DownloadTask extends Task<Void> {
    private FileManager fileManager;
    private File selectedDirectory;
    private ArrayList<String> urls;

    public DownloadTask(FileManager fileManager, ArrayList<String> urls) {
        this.fileManager = fileManager;
        this.urls = urls;
    }

    @Override
    protected Void call() {
        this.fileManager.downloadFromUrl(this, this.selectedDirectory, urls);

        return null;
    }

    void updateProgressBar(double bytes, double size) {
        this.updateProgress(bytes, size);
        this.updateMessage(Double.toString(Math.round(bytes / size * 100.0)) + " %");
    }

    public void setSelectedDirectory(File selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }
}

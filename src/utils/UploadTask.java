package src.utils;

import javafx.concurrent.Task;

import java.io.File;

public class UploadTask extends Task<Void> {
    private String addedFileName;
    private File selectedFile;
    private FileManager fileManager;
    private String directoy;
    private String mesure;

    public UploadTask(FileManager fileManager, String directoy, String mesure) {
        this.fileManager = fileManager;
        this.directoy = directoy;
        this.mesure = mesure;
    }

    @Override
    protected Void call() {
        this.addedFileName = this.fileManager.uploadToURL(this, this.selectedFile, directoy, mesure);

        return null;
    }

    void updateProgressBar(double value) {
        System.out.println("lol");

        this.updateProgress(value, value);
    }

    public String getAddedFileName() {
        return addedFileName;
    }

    void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}

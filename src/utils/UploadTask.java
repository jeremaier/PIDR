package src.utils;

import javafx.concurrent.Task;

import java.io.File;

public class UploadTask extends Task<Void> {
    private String addedFileName;
    private File selectedFile;
    private FileManager fileManager;
    private String directoy;

    public UploadTask(FileManager fileManager, String directoy) {
        this.fileManager = fileManager;
        this.directoy = directoy;
    }

    @Override
    protected Void call() {
        this.addedFileName = this.fileManager.uploadToURL(this, this.selectedFile, directoy, null);

        return null;
    }

    void updateProgressBar(double value) {
        this.updateProgress(value, value);
    }

    public String getAddedFileName() {
        return addedFileName;
    }

    void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}

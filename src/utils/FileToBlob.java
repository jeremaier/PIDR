package src.utils;

import java.io.File;
import java.sql.Blob;

public class FileToBlob {
    Blob blob;
    File file;

    public FileToBlob(File file) {
        this.file = file;
        this.transformToBlob(this.file);
    }

    private void transformToBlob(File file) {
    }

    public Blob getBlob() {
        return this.blob;
    }
}

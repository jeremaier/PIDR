package src.utils;

import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class FileManager {
    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if (!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + name;
    }

    public static void downloadFromUrl(String url) {
        String[] fileName = url.split(url);
        int fileNameLength = fileName.length;

        try {
            FileUtils.copyURLToFile(new URL(url), new File(System.getProperty("user.dir") + File.separator + "Telechargements" + File.separator + fileName[fileNameLength - 1]), 5000, 10000);
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur d'URL");
            alert.setHeaderText(null);
            alert.setContentText("URL de téléchargement invalide");
            alert.showAndWait();
        }
    }
}
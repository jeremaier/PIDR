package src.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.URL;

public class FileManager {
    private FTPClient ftpClient;

    public FileManager(String user, String password) {
        this.openFTPConnection(user, password);
    }

    private void openFTPConnection(String user, String password) {
        ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        try {
            ftpClient.connect("193.54.21.7");
            int reply = ftpClient.getReplyCode();

            if(!FTPReply.isPositiveCompletion(reply))
                ftpClient.disconnect();

            ftpClient.login(user, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalActiveMode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FTPClient getConnection() {
        return this.ftpClient;
    }

    public static String getFileName(String url) {
        String[] urlSplit = url.split(File.separator);

        return urlSplit[urlSplit.length - 1];
    }

    public ObservableList<String> listFiles(String directory) {
        ObservableList<String> fileList = FXCollections.observableArrayList();

        try {
            FTPFile[] files = ftpClient.listFiles(directory);

            for (FTPFile file : files)
                fileList.add(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de commnuniquer avec le serveur");
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileList;
    }

    public final ObservableList<String> getFileFromFtp(String name) {
        String directory = FilenameUtils.getPath(name);
        ObservableList<String> fileList = this.listFiles(directory);
        ObservableList<String> fileMatchList = FXCollections.observableArrayList();

        for(String file : fileList) {
            if(file.contains(name))
                fileMatchList.add(file);
        }

        return fileMatchList;
    }

    public final void removeFile(String url) {
        try {
            ftpClient.deleteFile(url);
        } catch (IOException e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de supprimer le fichier");
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final void downloadFromUrl(Stage stage, String url) {
        File selectedDirectory = FileManager.openDirectoryChooser(stage);

        if(selectedDirectory != null) {
            try {
                FileUtils.copyURLToFile(new URL(url), new File(selectedDirectory.getAbsolutePath() + File.separator + FileManager.getFileName(url)), 5000, 10000);
            } catch(IOException e) {
                e.printStackTrace();

                FileManager.openAlert("Téléchargement impossible / Erreur de connexion");
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public final void uploadToURL(Stage stage, String dossier) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez un fichier");
        fileChooser.setInitialDirectory(new File("user.dir"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        InputStream input = null;

        if(selectedFile != null) {
            try {
                input = new FileInputStream(new File(selectedFile.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                this.ftpClient.storeFile(dossier + File.separator + FileManager.getFileName(selectedFile.toString()), input);
            } catch(IOException e) {
                e.printStackTrace();
                FileManager.openAlert("Impossible d'upload le fichier sur le serveur");
            } finally {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void openAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur d'URL");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private static File openDirectoryChooser(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choisissez un dossier");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return directoryChooser.showDialog(stage);
    }

    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if (!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + name;
    }
}
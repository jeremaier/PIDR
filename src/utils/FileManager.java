package src.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FileManager {
    private final static String procDirectoryName = ".//procedures";
    private final static String resDirectoryName = ".//resultats";
    private final static String refDirectoryName = "//ref";
    private final static String lesionFilesDirectoryName = "//lesion";
    private SSLSessionReuseFTPSClient ftpClient;
    private String user;
    private String password;

    public FileManager(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public static String getFileName(String url, boolean upload) {
        String[] urlSplit;

        if (upload)
            urlSplit = url.split("\\\\");
        else urlSplit = url.split("//");

        return urlSplit[urlSplit.length - 1];
    }

    private static File openDirectoryChooser(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choisissez un dossier");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return directoryChooser.showDialog(stage);
    }

    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + "//saves";
        File folder = new File(directoryPath);

        if (!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + "//" + name;
    }

    public static String getRefDirectoryName(String inclusionId) {
        return FileManager.refDirectoryName + "//" + inclusionId;
    }

    public static String getLesionFilesDirectoryName(String lesionId) {
        return FileManager.lesionFilesDirectoryName + "//" + lesionId;
    }

    public static String getProcDirectoryName() {
        return FileManager.procDirectoryName;
    }

    public static String getResDirectoryName() {
        return FileManager.resDirectoryName;
    }

    public static void openAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(errorMessage);
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public ObservableList<String> listFiles(String directory, boolean open, boolean close) {
        ObservableList<String> fileList = FXCollections.observableArrayList();

        if (open)
            this.openFTPConnection();

        try {
            for (FTPFile file : ftpClient.listFiles(directory))
                fileList.add(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de commnuniquer avec le serveur");
        }

        if (close)
            this.closeFTPConnection();

        return fileList;
    }

    public FTPClient getConnection() {
        return this.ftpClient;
    }

    public void openFTPConnection() {
        System.setProperty("jdk.tls.useExtendedMasterSecret", "false");
        ftpClient = new SSLSessionReuseFTPSClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.setConnectTimeout(7200000);
        ftpClient.setDataTimeout(10000);

        try {
            ftpClient.connect("193.54.21.7");
            ftpClient.setKeepAlive(true);

            ftpClient.execPROT("P");

            int reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
                ftpClient.disconnect();

            ftpClient.login(this.user, this.password);
            ftpClient.setSoTimeout(5000);
            ftpClient.setControlKeepAliveTimeout(120);
            ftpClient.setControlKeepAliveReplyTimeout(120);
            ftpClient.setBufferSize(1024 * 1024);
            ftpClient.setAutodetectUTF8(true);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFTPConnection() {
        if (this.getConnection() != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final ObservableList<String> getFileFromFtp(String name, String directory, boolean open, boolean close) {
        ObservableList<String> fileList = this.listFiles(directory, open, close);
        ObservableList<String> fileMatchList = FXCollections.observableArrayList();

        for (String file : fileList)
            if(file.contains(name))
                fileMatchList.add(file);

        return fileMatchList;
    }

    public final boolean removeFile(String url) {
        this.openFTPConnection();

        try {
            ftpClient.deleteFile(url);
        } catch (IOException e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de supprimer le fichier");
            return false;
        }

        this.closeFTPConnection();

        return true;
    }

    public final File downloadFromUrl(Stage stage, String url, File choosenDirectory, boolean open, boolean close) {
        File selectedDirectory;

        if (choosenDirectory == null)
            selectedDirectory = FileManager.openDirectoryChooser(stage);
        else selectedDirectory = choosenDirectory;

        if(selectedDirectory != null) {
            if (open)
                this.openFTPConnection();

            try {
                File file = new File(selectedDirectory + "//" + FileManager.getFileName(url, false));
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                ftpClient.retrieveFile(url, outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                FileManager.openAlert("Téléchargement impossible / Erreur de connexion");
            }

            if (close)
                this.closeFTPConnection();
        }

        return selectedDirectory;
    }

    public final String uploadToURL(Stage stage, String dossier, String mesure) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez un fichier");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(stage);
        String fileName = null;

        if(selectedFile != null) {
            InputStream input = null;

            this.openFTPConnection();

            try {
                input = new FileInputStream(new File(selectedFile.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                FileManager.openAlert("Impossible d'upload le fichier sur le serveur");
            }

            fileName = FileManager.getFileName(selectedFile.toString(), true);

            try {
                this.makeDirectories(dossier);

                if (!this.ftpClient.storeFile(dossier + "//" + (mesure == null ? "" : mesure + "=") + fileName, input))
                    fileName = null;
            } catch(IOException e) {
                e.printStackTrace();
                FileManager.openAlert("Impossible d'upload le fichier sur le serveur");
            }

            this.closeFTPConnection();
        }

        return fileName;
    }

    private void makeDirectories(String directories) {
        String[] directoriesPath = directories.split("//");

        if (directoriesPath.length > 0) {
            for (String dir : directoriesPath) {
                try {
                    if (!this.ftpClient.changeWorkingDirectory(dir))
                        if (this.ftpClient.makeDirectory(dir))
                            this.ftpClient.changeWorkingDirectory(dir);
                } catch (IOException e) {
                    FileManager.openAlert("Problème de communication avec le serveur");
                    e.printStackTrace();
                }
            }
        }
    }
}
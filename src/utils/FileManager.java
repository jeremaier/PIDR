package src.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

public class FileManager extends Observable {
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

    public static File openDirectoryChooser(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choisissez un dossier");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        return directoryChooser.showDialog(stage);
    }

    public static void openFileChooser(Stage stage, UploadTask uploadTask) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez un fichier");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        uploadTask.setSelectedFile(fileChooser.showOpenDialog(stage));
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
            fileList = null;
            this.closeFTPConnection();
            FileManager.openAlert("Impossible de commnuniquer avec le serveur");
        }

        if (close)
            this.closeFTPConnection();

        return fileList;
    }

    public FTPClient getConnection() {
        return this.ftpClient;
    }

    public boolean openFTPConnection() {
        System.setProperty("jdk.tls.useExtendedMasterSecret", "false");
        ftpClient = new SSLSessionReuseFTPSClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftpClient.setConnectTimeout(2000);
        ftpClient.setDataTimeout(500);

        try {
            ftpClient.connect("193.54.21.7");
            ftpClient.setKeepAlive(true);

            ftpClient.execPROT("P");

            int reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }

            if (!ftpClient.login(this.user, this.password)) {
                ftpClient.logout();
                ftpClient.disconnect();
                return false;
            }

            ftpClient.setSoTimeout(5000);
            ftpClient.setControlKeepAliveTimeout(120);
            ftpClient.setControlKeepAliveReplyTimeout(120);
            ftpClient.setBufferSize(1024 * 1024);
            ftpClient.setAutodetectUTF8(true);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void closeFTPConnection() {
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

    final void removeFiles(RemoveTask task, ArrayList<String> urls) {
        this.openFTPConnection();

        try {
            task.updateProgressBar();

            for (String url : urls)
                ftpClient.deleteFile(url);
        } catch (IOException e) {
            e.printStackTrace();
            FileManager.openAlert("Impossible de supprimer le fichier");
        }

        this.closeFTPConnection();
    }

    void downloadFromUrl(DownloadTask task, File selectedDirectory, ArrayList<String> urls) {
        if(selectedDirectory != null) {
            this.openFTPConnection();

            try {
                for (String url : urls) {
                    String filePath = selectedDirectory + "//" + FileManager.getFileName(url, false);
                    FTPFile file = ftpClient.mlistFile(url);
                    long fileSize = file.getSize();

                    OutputStream outputStream = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filePath)))) {
                        @Override
                        public void beforeWrite(int count) {
                            super.beforeWrite(count);
                            task.updateProgressBar(this.getCount(), fileSize);
                        }
                    };

                    ftpClient.retrieveFile(url, outputStream);
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                FileManager.openAlert("Téléchargement impossible / Erreur de connexion");
            }

            task.updateProgressBar(0, 0);
            this.closeFTPConnection();
        }
    }

    /*public final String uploadToURL(Stage stage, String dossier, String mesure) {
        String fileName = null;

        if(selectedFile != null) {
            InputStream input = null;

            this.openFTPConnection();
            this.setSize(selectedFile.length());

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

            this.setSize(-1);
            this.closeFTPConnection();
        }

        return fileName;
    }*/

    final String uploadToURL(UploadTask task, File selectedFile, String dossier, String mesure) {
        String fileName = null;

        if (selectedFile != null) {
            InputStream input = null;

            this.openFTPConnection();

            try {
                input = new FileInputStream(new File(selectedFile.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                FileManager.openAlert("Impossible d'upload le fichier sur le serveur");
            }

            fileName = FileManager.getFileName(selectedFile.toString(), true);
            task.updateProgressBar(-1);

            try {
                this.makeDirectories(dossier);

                if (!this.ftpClient.storeFile(dossier + "//" + (mesure == null ? "" : mesure + "=") + fileName, input))
                    fileName = null;
            } catch (IOException e) {
                e.printStackTrace();
                FileManager.openAlert("Impossible d'upload le fichier sur le serveur");
            }

            this.closeFTPConnection();
        }

        task.updateProgressBar(0);

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
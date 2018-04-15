package src.utils;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileManager {
    public static String getFilePath(String name) {
        String directoryPath = System.getProperty("user.dir") + File.separator + "saves";
        File folder = new File(directoryPath);

        if (!folder.exists())
            folder.mkdirs();

        return folder.getAbsoluteFile() + File.separator + name;
    }

    public static final String getFileName(String url) {
        String[] urlSplit = url.split(File.separator);

        return urlSplit[urlSplit.length - 1];
    }

    /*public static final LocalDate sqlDateToLocalDate(Date date) {
        LocalDate locald = LocalDate.of(1967, 06, 22);
        Date date = Date.valueOf(locald);
        r.setDateOfBirth(date);

        return localDate;
    }*/

    public static void downloadFromUrl(Stage stage, String url) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choisissez un dossier");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(stage);

        if(selectedDirectory != null) {
            try {
                FileUtils.copyURLToFile(new URL(url), new File(selectedDirectory.getAbsolutePath() + File.separator + FileManager.getFileName(url)), 5000, 10000);
            } catch(IOException e) {
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur d'URL");
                alert.setHeaderText(null);
                alert.setContentText("URL de téléchargement invalide");
                alert.showAndWait();
            }
        }
    }

    public static void uploadToURL() {
        String url = "http://example.com/upload";
        String charset = "UTF-8";
        String param = "value";
        File textFile = new File("/path/to/file.txt");
        File binaryFile = new File("/path/to/file.bin");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.
        URLConnection connection;

        try {
            connection = new URL(url).openConnection();
            assert connection != null;
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (
                    OutputStream output = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)
            ) {
                // Send normal param.
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF);
                writer.append(CRLF).append(param).append(CRLF).flush();

                // Send text file.
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"").append(textFile.getName()).append("\"").append(CRLF);
                writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF); // Text file itself must be saved in this charset!
                writer.append(CRLF).flush();
                Files.copy(textFile.toPath(), output);
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                // Send binary file.
                writer.append("--").append(boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"").append(binaryFile.getName()).append("\"").append(CRLF);
                writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();
                Files.copy(binaryFile.toPath(), output);
                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                // End of multipart/form-data.
                writer.append("--").append(boundary).append("--").append(CRLF).flush();
            }

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println(responseCode);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void uploadToURL2() {
        HttpEntity entity = MultipartEntityBuilder.create().addTextBody("field1", "value1").addBinaryBody("myfile", new File("/path/file1.txt"), ContentType.create("application/octet-stream"), "file1.txt").build();
        entity.addPart("file", new FileBody(file));

        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
    }*/
}
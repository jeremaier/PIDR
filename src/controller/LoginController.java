package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import src.utils.FileManager;
import src.utils.SQLConnection;
import src.view.InclusionsView;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField user;
    @FXML
    TextField password;
    @FXML
    Button connectButton;
    @FXML
    ProgressIndicator pi;
    @FXML
    Label load;
    @FXML
    CheckBox saveLogin;
    @FXML
    GridPane grid;

    private String loginFileName = "Log";
    private static String logDirectoryName;

    public void NewConnection() {
        this.checkLog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if ((System.getProperty("os.name")).toUpperCase().contains("WIN"))
            logDirectoryName = System.getenv("AppData") + "//SpectroLive//";
        else logDirectoryName = System.getProperty("user.home") + "/Library/Application Support/SpectroLive/";

        this.loadTokenFromFile();
        this.pi.setProgress(-1);
        this.pi.setVisible(false);
        this.load.setVisible(false);

        if(!this.user.getText().equals(""))
            this.saveLogin.setSelected(true);

        this.password.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                this.NewConnection();
        });
    }

    private void checkLog() {
        if (!this.user.getText().equals("") && !this.password.getText().equals("")) {
            this.connectButton.setDisable(true);

            SQLConnection sqlConnection = new SQLConnection(this.user.getText(), this.password.getText(), this.connectButton);
            FileManager fileManager = new FileManager(this.user.getText(), this.password.getText());
            Connection connection = SQLConnection.getConnection();

            try {
                fileManager.openFTPConnection();

                if (connection != null) {
                    if (this.saveLogin.isSelected())
                        this.saveLoginInFile();
                    else this.deleteLoginFile();

                    Stage stage = (Stage) this.connectButton.getScene().getWindow();
                    stage.close();
                    new InclusionsView(null, connection, fileManager);
                } else {
                    FileManager.openAlert("Identifiant et/ou mot de passe incorrect(s) ou impossible de se connecter");
                    this.password.clear();
                    this.connectButton.setDisable(false);

                    try {
                        sqlConnection.closeConnection();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.connectButton.setDisable(false);
                FileManager.openAlert("Impossible de se connecter au serveur FTP");
            }
        }
    }

    private void loadTokenFromFile() {
        File file = new File(logDirectoryName + loginFileName);
        FileInputStream fIn;
        ObjectInputStream oIn;

        if (file.exists()) {
            if (file.length() > 0) {
                try {
                    fIn = new FileInputStream(file);
                    oIn = new ObjectInputStream(fIn);

                    this.user.setText((String) oIn.readObject());

                    oIn.close();
                    fIn.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveLoginInFile() {
        File folder = new File(logDirectoryName);
        File file = new File(logDirectoryName + loginFileName);

        if (!folder.exists())
            folder.mkdirs();

        try {
            if (!this.user.getText().equals("") && file.createNewFile()) {
                FileOutputStream fOut;
                ObjectOutputStream oOut;

                try {
                    fOut = new FileOutputStream(file.getAbsoluteFile());
                    oOut = new ObjectOutputStream(fOut);

                    oOut.writeObject(this.user.getText());

                    oOut.close();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLoginFile() {
        File loginFile = new File(logDirectoryName + loginFileName);

        if (loginFile.exists()) {
            File folder = loginFile.getParentFile();

            try {
                java.nio.file.Files.delete(loginFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!folder.delete())
                System.out.println("Cannot delete file: " + folder);
        }
    }
}
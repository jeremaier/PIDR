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

    public void NewConnection() {
        this.checkLog();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        Alert alert = new Alert(Alert.AlertType.WARNING);

        if (!this.user.getText().equals("") && !this.password.getText().equals("")) {
            this.connectButton.setDisable(true);

            SQLConnection sqlConnection = new SQLConnection(user.getText(), password.getText());
            FileManager fileManager = new FileManager(user.getText(), password.getText());
            Connection connection = SQLConnection.getConnection();
            
            /*TODO empecher co quand ftp marche pas*/
            if (connection != null && fileManager.openFTPConnection()) {
                if (this.saveLogin.isSelected())
                    this.saveLoginInFile();
                else this.deleteLoginFile();

                System.out.println("Connection etablie");

                Stage stage = (Stage) connectButton.getScene().getWindow();
                stage.close();
                new InclusionsView(connection, fileManager);
            } else {
                alert.setTitle("Erreur d'identification");
                alert.setHeaderText(null);
                alert.setContentText("Identifiant et/ou mot de passe incorrect(s)");
                alert.showAndWait();
                this.password.clear();
                this.connectButton.setDisable(false);

                if (connection != null) {
                    try {
                        sqlConnection.closeConnection();
                    } catch(SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void loadTokenFromFile() {
        File file = new File(FileManager.getFilePath(loginFileName));
        FileInputStream fIn;
        ObjectInputStream oIn;

        try {
            if (!file.createNewFile())
                System.out.println("Cannot create file: " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void saveLoginInFile() {
        if(!this.user.getText().equals("")) {
            FileOutputStream fOut;
            ObjectOutputStream oOut;

            try {
                fOut = new FileOutputStream(FileManager.getFilePath(loginFileName));
                oOut = new ObjectOutputStream(fOut);

                oOut.writeObject(this.user.getText());

                oOut.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteLoginFile() {
        File loginFile = new File(FileManager.getFilePath(loginFileName));

        if (loginFile.exists())
            if (!loginFile.delete())
                System.out.println("Cannot delete file: " + loginFile);
    }
}
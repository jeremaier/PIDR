package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.utils.ConnectionConfiguration;
import src.utils.FileManager;
import src.view.InclusionsView;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    TextField password;
    @FXML
    TextField user;
    @FXML
    Button connectButton;
    @FXML
    ProgressIndicator pi;
    @FXML
    Label load;
    @FXML
    CheckBox saveLogin;

    private String loginFileName = "Log";
    private ConnectionConfiguration connection = null;
    private boolean saveSelected = false;

    public void NewConnection() {
        this.checkLog();
        //new Thread(this.connection).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadTokenFromFile();
        this.saveLogin.setSelected(this.saveSelected);
        this.pi.setProgress(-1);
        this.pi.setVisible(false);
        this.load.setVisible(false);

        if(!this.user.getText().equals(""))
            this.password.requestFocus();
        //pi.setStyle("-fx-progress-color: #676768;");
    }

    private void checkLog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        if (!this.user.getText().equals("") && !this.password.getText().equals("")) {
            this.connectButton.setDisable(true);

            this.connection = new ConnectionConfiguration(user.getText(), password.getText());
            this.connection.createConnnection();

            if (this.connection.getConnection() != null) {
                if (this.saveLogin.isSelected())
                    this.saveLoginInFile();
                else this.deleteLoginFile();

                System.out.println("Connection established");

                Stage stage = (Stage) connectButton.getScene().getWindow();
                stage.close();
                new InclusionsView(this.connection.getConnection());
            } else {
                alert.setTitle("Erreur d'identification");
                alert.setHeaderText(null);
                alert.setContentText("Identifiant et/ou mot de passe incorrect(s)");
                alert.showAndWait();
                this.password.clear();
                this.connectButton.setDisable(false);

                if(this.connection.getConnection() != null) {
                    try {
                        this.connection.closeConnection();
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
                this.saveSelected = (boolean) oIn.readObject();

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
                oOut.writeObject(this.saveSelected);

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
package src.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import src.util.ConnectionConfiguration;
import src.util.FileManager;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogViewController implements Initializable {
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

    public void NewConnection() {
        this.checkLog();
        new Thread(this.connection).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadTokenFromFile();
        this.saveLogin.setSelected(true);
        this.pi.setProgress(-1);
        this.pi.setVisible(false);
        this.load.setVisible(false);
        //pi.setStyle("-fx-progress-color: #676768;");
    }

    private void checkLog() {
        if(!this.user.getText().equals("") && !this.password.getText().equals("")) {
            this.connectButton.setDisable(true);

            try {
                this.connection = new ConnectionConfiguration(user.getText(), password.getText());

                if (this.connection.getConnection() != null) {
                    if (this.saveLogin.isSelected())
                        this.saveLoginInFile();
                    else this.deleteLoginFile();

                    /*Stage stage = (Stage) connectButton.getScene().getWindow();
                    stage.close();*/

                    System.out.println("Connection established");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);

                e.printStackTrace();
                alert.setTitle("Erreur d'identification");
                alert.setHeaderText(null);
                alert.setContentText("Identifiant et/ou mot de passe incorrect(s)");
                alert.showAndWait();
                this.user.clear();
                this.connectButton.setDisable(false);
            } finally {
                if(this.connection.getConnection() != null) {
                    try {
                        this.connection.closeConnection();
                    } catch (SQLException e) {
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

        if(file.length() > 0) {
            try {
                fIn = new FileInputStream(file);
                oIn = new ObjectInputStream(fIn);

                this.user.setText((String) oIn.readObject());
                this.password.setText((String) oIn.readObject());

                oIn.close();
                fIn.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLoginInFile() {
        if(this.user != null && this.password != null) {
            FileOutputStream fOut;
            ObjectOutputStream oOut;

            try {
                fOut = new FileOutputStream(FileManager.getFilePath(loginFileName));
                oOut = new ObjectOutputStream(fOut);

                oOut.writeObject(user.getText());
                oOut.writeObject(password.getText());

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
package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import src.table.Lesion;

import java.net.URL;
import java.util.ResourceBundle;

public class AddDiagController implements Initializable {
    @FXML
    Button addButton;
    @FXML
    Button removeButton;
    @FXML
    TextArea diag;

    private Lesion lesion;
    private Stage addDiag;

    public AddDiagController(Lesion lesion) {
        this.lesion = lesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.lesion.getAutreDiag() != null) {
            this.diag.setText(this.lesion.getAutreDiag());
            this.addButton.setText("Modifier");
        }
    }

    public void ajoutAction() {
        if (this.addDiag == null)
            this.addDiag = (Stage) this.addButton.getScene().getWindow();

        if (!this.diag.getText().equals(""))
            this.lesion.setAutreDiag(this.diag.getText());
        else this.lesion.setAutreDiag(null);

        this.addDiag.close();
    }

    public void removeAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Vous allez supprimer le diagnostic");
        alert.setContentText("Confirmer?");

        if (alert.showAndWait().get() == ButtonType.OK)
            this.diag.setText("");
        else alert.close();
    }
}

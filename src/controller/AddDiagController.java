package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import src.table.Lesion;
import src.utils.Diag;

import java.net.URL;
import java.util.ResourceBundle;

public class AddDiagController implements Initializable {
    @FXML
    Button addButton;
    @FXML
    Button removeButton;
    @FXML
    TextArea diag;

    private AddLesionController addLesionController;
    private Lesion lesion;

    public AddDiagController(AddLesionController addLesionController, Lesion lesion) {
        this.addLesionController = addLesionController;
        this.lesion = lesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!this.lesion.getAutreDiag().equals("")) {
            this.diag.setText(this.lesion.getAutreDiag());
            this.addButton.setText("Modifier");
        }
    }

    public void ajoutAction() {
        if (!this.diag.getText().equals("")) {
            this.addLesionController.diagBox.setValue(Diag.AUTRE);
            this.addLesionController.diagBox.setDisable(true);
            this.lesion.setAutreDiag(this.diag.getText());
        } else {
            this.addLesionController.diagBox.setValue(null);
            this.addLesionController.diagBox.setDisable(false);
            this.lesion.setAutreDiag("");
        }
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

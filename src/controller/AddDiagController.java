package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import src.table.Lesion;

public class AddDiagController {
    @FXML
    Button ajoutButton;
    @FXML
    TextArea diag;

    private Lesion lesion;

    public AddDiagController(Lesion lesion) {
        this.lesion = lesion;
    }

    public void ajoutAction() {
        if (!this.diag.getText().equals(""))
            this.lesion.setAutreDiag(this.diag.getText());
    }
}

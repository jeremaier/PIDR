package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import src.utils.FileManager;

import java.sql.Connection;

public abstract class Controller {
    @FXML
    public
    Button removeButton;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label progressLabel;

    protected Stage stage;
    protected Connection connection;
    protected FileManager fileManager;

    public void setStage(Button button) {
        if (this.stage == null)
            this.stage = (Stage) button.getScene().getWindow();
    }

    public abstract void enableButtons(boolean enable, boolean all);

    public void endRemove() {
        this.enableButtons(true, true);
        this.progressBar.setVisible(false);
    }
}

package src;

import javafx.application.Application;
import javafx.stage.Stage;
import src.view.LogView;

public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new LogView(primaryStage);
    }
}
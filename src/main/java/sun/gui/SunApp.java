package sun.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SunApp extends Application {
    @Override
    public void start(Stage stage) {
        Label welcome = new Label("Welcome to Sun Chatbot!");

        StackPane root = new StackPane();
        root.getChildren().add(welcome);

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);;
        stage.setTitle("Sun ChatBot");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

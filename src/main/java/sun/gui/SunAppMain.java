package sun.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SunAppMain extends Application {

    private SunGui sunGui = new SunGui();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SunAppMain.class.getResource("/sun/view/SunAppView.fxml"));

            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root);

            // Inject sunGui into controller
            SunAppController controller = fxmlLoader.getController();
            controller.setSunGui(sunGui);
            controller.setStage(stage);

            stage.setScene(scene);
            stage.setTitle("Sun ChatBot");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




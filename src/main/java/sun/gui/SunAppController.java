package sun.gui;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SunAppController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private SunBackend sunBackend;

    private Stage stage;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/User.png"));
    private final Image sunImage = new Image(getClass().getResourceAsStream("/images/Sun.png"));

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        dialogContainer.getChildren().add(
                DialogBox.getSunDialog("Hello! I'm Sun.\nWhat can I do for you?", sunImage)
        );
    }

    /** Inject SunBackend from SunApp */
    public void setSunBackend(SunBackend backend) {
        this.sunBackend = backend;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input == null || input.isBlank()) {
            return;
        }

        String response = sunBackend.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSunDialog(response.equals("BYE_SIGNAL")
                        ? "Bye. Hope to see you again soon!"
                        : response,
                        sunImage)
        );

        if ("BYE_SIGNAL".equals(response)) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        }

        userInput.clear();
    }
}

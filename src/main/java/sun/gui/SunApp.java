package sun.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.SunCli;

public class SunApp extends Application {
    private Stage stage;

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    // Images
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image sunImage = new Image(this.getClass().getResourceAsStream("/images/Sun.png"));

    // SunBackend instance
    private SunBackend sunBackend = new SunBackend();

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // Initialise Components
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        // Welcome Message
        dialogContainer.getChildren().add(
                DialogBox.getSunDialog("Hello! I'm Sun.\nWhat can I do for you?", sunImage)
        );

        // Scene the stage
        Scene scene = new Scene(mainLayout, 400, 600);
        stage.setScene(scene);;
        stage.show();

        // Window settings
        stage.setTitle("Sun ChatBot");
        stage.setResizable(false);
        stage.setMinWidth(400);
        stage.setMinHeight(600);

        // Component settings
        mainLayout.setPrefSize(400, 600);
        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        userInput.setPrefWidth(325);;
        sendButton.setPrefWidth(55);;

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);


        // Define Interactions
        sendButton.setOnMouseClicked((event) -> handleUserInput());
        userInput.setOnAction((event) -> handleUserInput());

    }

    private void handleUserInput() {
        String userInput = this.userInput.getText();

        if (userInput.isEmpty()) {
            return;
        }

        String sunOutput = sunBackend.getResponse(userInput);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog("User:\n" + userInput, userImage),
                DialogBox.getSunDialog("Sun:\n" +
                        (sunOutput.equals("BYE_SIGNAL") ? "Bye. Hope to see you again soon!" : sunOutput), sunImage)
        );

        this.userInput.clear();

        if (sunOutput.equals("BYE_SIGNAL")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> stage.close());;
            delay.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
//        try {
//            launch(args);
//        } catch (Throwable e) {
//            System.err.println("GUI failed to start. Falling back to CLI...");
//            System.err.println(e.getMessage());
//
//            new SunCli("./data/sun.txt").run();
//        }
    }
}

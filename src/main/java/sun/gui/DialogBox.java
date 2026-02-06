package sun.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/sun/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getSunDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}

// JavaFX ONLY
//package sun.gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//
//public class DialogBox extends HBox {
//
//    private Label text;
//    private ImageView displayPicture;
//
//    public DialogBox(String string, Image image) {
//        text = new Label(string);
//        displayPicture = new ImageView(image);
//
//        // Styling
//        text.setWrapText(true);
//        displayPicture.setFitWidth(100.0);
//        displayPicture.setFitHeight(100.0);
//        this.setAlignment(Pos.TOP_RIGHT);
//
//        this.getChildren().addAll(text, displayPicture);
//    }
//
//    /**
//     * Flips the dialog box: image left, text right
//     */
//    private void flip() {
//        this.setAlignment(Pos.TOP_LEFT);
//        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
//        FXCollections.reverse(tmp);
//        this.getChildren().setAll(tmp);
//    }
//
//    /**
//     * Factory method for user dialog
//     */
//    public static DialogBox getUserDialog(String userInput, Image userImage) {
//        return new DialogBox(userInput, userImage);
//    }
//
//    /**
//     * Factory method for Duke dialog
//     */
//    public static DialogBox getSunDialog(String sunOutput, Image sunImage) {
//        DialogBox dialogBox = new DialogBox(sunOutput, sunImage);
//        dialogBox.flip();
//        return dialogBox;
//    }
//}

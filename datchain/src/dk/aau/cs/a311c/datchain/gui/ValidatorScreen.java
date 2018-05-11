package dk.aau.cs.a311c.datchain.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ValidatorScreen {
    public static void validatorScreen(Stage primaryStage) {

        VBox leftPanel = new VBox(20);
        leftPanel.setPadding(new Insets(10, 10, 10, 10));
        Button addBlock = new Button("Add a block");

        //Label addBlockLabel = new Label("Add a block");
        leftPanel.getChildren().addAll(addBlock);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftPanel);
        Scene scene = new Scene(borderPane, 700, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

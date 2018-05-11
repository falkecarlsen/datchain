package dk.aau.cs.a311c.datchain.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class CloseProgram {

    static boolean answer;

    public static boolean display(String message) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setResizable(false);
        Label label = new Label();
        label.setText(message);

        //buttons
        Button yesButton = new Button("yes");
        Button noButton = new Button("no");

        yesButton.setOnAction(e -> {
            answer = true;
            popUp.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            popUp.close();
        });

        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label);
        root.setPadding(new Insets(10, 0, 0, 0));

        HBox center = new HBox(25);
        center.setAlignment(Pos.CENTER);
        center.getChildren().addAll(yesButton, noButton);
        center.setPadding(new Insets(0, 0, 10, 0));

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(root);
        borderPane.setCenter(center);

        Scene scene = new Scene(borderPane, 200, 100);

        popUp.setScene(scene);
        popUp.showAndWait();

        return answer;
    }
}

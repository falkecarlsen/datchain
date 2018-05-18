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


class CloseProgram {

    private static boolean answer;

    public static boolean display() {

        //setting up the popup window
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setResizable(false);
        Label exitLabel = new Label();
        exitLabel.setText("Are you sure you want to exit?");

        //buttons
        Button yesButton = new Button("yes");
        Button noButton = new Button("no");

        //if yes button is clicked, return true
        yesButton.setOnAction(e -> {
            answer = true;
            popUp.close();
        });

        //if no button is clicked, return false
        noButton.setOnAction(e -> {
            answer = false;
            popUp.close();
        });


        //label on top
        HBox topPane = new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.getChildren().addAll(exitLabel);
        topPane.setPadding(new Insets(10, 0, 0, 0));

        //buttons below the label, next to each other
        HBox centerPanel = new HBox(25);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.getChildren().addAll(yesButton, noButton);
        centerPanel.setPadding(new Insets(0, 0, 10, 0));

        //setting the scene and stage
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topPane);
        borderPane.setCenter(centerPanel);
        Scene scene = new Scene(borderPane, 200, 100);
        popUp.setScene(scene);
        popUp.showAndWait();

        return answer;
    }
}

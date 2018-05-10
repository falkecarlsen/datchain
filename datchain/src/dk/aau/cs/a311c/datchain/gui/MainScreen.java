package dk.aau.cs.a311c.datchain.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MainScreen {
    public static void screen(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setVgap(5);
        root.setHgap(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(root, 560, 180);
        primaryStage.setResizable(false);


        Button Search_button = new Button("Search");
        root.setConstraints(Search_button,3,0);
        Search_button.setMinWidth(100);
        root.getChildren().add(Search_button);
        Search_button.setOnMouseClicked(event -> Search.Search());

        Button logout_button = new Button("Logout");
        root.setConstraints(logout_button,0,0);
        root.getChildren().add(logout_button);
        logout_button.setOnMouseClicked(event -> Login.login(primaryStage));

        Label firstname_label = new Label("Fornavn:");
        root.setConstraints(firstname_label,1,1);
        root.getChildren().add(firstname_label);

        Label lastname_label = new Label("Efternavn:");
        root.setConstraints(lastname_label,1,2);
        root.getChildren().add(lastname_label);

        Label ID_label = new Label("ID-nummer:");
        root.setConstraints(ID_label,1,3);
        root.getChildren().add(ID_label);

        Label birthday_label = new Label("Fødselsdag:");
        root.setConstraints(birthday_label,1,4);
        root.getChildren().add(birthday_label);

        Label public_key_label = new Label("Offentlig ID:");
        root.setConstraints(public_key_label,1,5);
        root.getChildren().add(public_key_label);

        Label firstname = new Label("");
        root.setConstraints(firstname,2,1);
        root.getChildren().add(firstname);

        Label lastname = new Label("");
        root.setConstraints(lastname,2,2);
        root.getChildren().add(lastname);

        Label ID = new Label("");
        root.setConstraints(ID,2,3);
        root.getChildren().add(ID);

        Label birthday = new Label("");
        root.setConstraints(birthday,2,4);
        root.getChildren().add(birthday);

        Label public_key = new Label("");
        root.setConstraints(public_key,2,5);
        public_key.setMinWidth(300);
        root.getChildren().add(public_key);

        primaryStage.setScene(scene);
        primaryStage.show();


    }
}

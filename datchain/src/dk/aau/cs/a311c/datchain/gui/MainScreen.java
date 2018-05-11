package dk.aau.cs.a311c.datchain.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;


public class MainScreen {
    public static void screen(Stage primaryStage) {
        GridPane gridLeft = new GridPane();
        gridLeft.setVgap(10);
        gridLeft.setHgap(8);
        gridLeft.setPadding(new Insets(10, 20, 10, 20));
        primaryStage.setResizable(false);


        //top panel
        HBox topPanel = new HBox();
        topPanel.setPadding(new Insets(5, 0, 10, 0));

        Button login_button = new Button("Login as validator");
        login_button.setOnMouseClicked(event -> Login.login(primaryStage));
        login_button.setMinWidth(125);

        topPanel.getChildren().add(login_button);
        topPanel.setAlignment(CENTER);


        //Left panel
        Text text1 = new Text("            Chosen person");
        text1.setStyle("-fx-font-weight: bold");
        gridLeft.setConstraints(text1, 2, 0);
        gridLeft.getChildren().add(text1);

        /*Label nullLabel = new Label("");
        nullLabel.setMinWidth(75);
        gridLeft.setConstraints(nullLabel,1,0);
        gridLeft.getChildren().add(nullLabel);*/

        Label firstname_label = new Label("First name:");
        firstname_label.setMinWidth(75);
        gridLeft.setConstraints(firstname_label, 1, 1);
        gridLeft.getChildren().add(firstname_label);

        Label lastname_label = new Label("Last name:");
        lastname_label.setMinWidth(75);
        gridLeft.setConstraints(lastname_label, 1, 2);
        gridLeft.getChildren().add(lastname_label);

        Label birthdateLabel = new Label("Birthdate:");
        birthdateLabel.setMinWidth(75);
        gridLeft.setConstraints(birthdateLabel, 1, 3);
        gridLeft.getChildren().add(birthdateLabel);

        Label pbkLabel = new Label("Public key:");
        pbkLabel.setMinWidth(75);
        gridLeft.setConstraints(pbkLabel, 1, 4);
        gridLeft.getChildren().add(pbkLabel);

        Label identityLabel = new Label("Identity:");
        identityLabel.setMinWidth(75);
        gridLeft.setConstraints(identityLabel, 1, 5);
        gridLeft.getChildren().add(identityLabel);


        //right panel
        GridPane gridRight = new GridPane();
        gridRight.setVgap(10);
        gridRight.setHgap(8);
        gridRight.setPadding(new Insets(10, 20, 10, 100));

        /*search*/
        /*Label term_label = new Label("Input:");
        term_label.setMinWidth(150);
        gridRight.setConstraints(term_label,0,0);
        gridRight.getChildren().add(term_label);*/
        TextField term_text = new TextField();
        term_text.setPromptText("Search for name, date of birth, public key");
        gridRight.setConstraints(term_text, 0, 0);
        gridRight.getChildren().add(term_text);

        ListView listView = new ListView<>();
        listView.setMinWidth(200);
        listView.setMaxHeight(120);

        listView.getItems().addAll("eks. 1", "eks. 2", "eks. 3");
        gridRight.setConstraints(listView, 0, 1, 1, 1);
        gridRight.getChildren().add(listView);


        //bottompanel
        HBox bottomPanel = new HBox();
        bottomPanel.setSpacing(150);
        bottomPanel.setAlignment(CENTER);
        bottomPanel.setPadding(new Insets(10, 0, 5, 0));

        String status = "Online";
        Text onlineLabel = new Text(status);
        if (status.equals("Online")) {
            onlineLabel.setFill(Color.GREEN);
        } else onlineLabel.setFill(Color.RED);


        int numberOfNodes = 10;
        Text nodesLabel = new Text("Nodes:  " + numberOfNodes);

        int numberOfBlocks = 497;
        Text blocksLabel = new Text("Blocks:  " + numberOfBlocks);

        bottomPanel.getChildren().addAll(nodesLabel, onlineLabel, blocksLabel);


        //close action
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(primaryStage);
        });

        //setting scene
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topPanel);
        borderPane.setLeft(gridLeft);
        borderPane.setRight(gridRight);
        borderPane.setBottom(bottomPanel);

        Scene scene = new Scene(borderPane, 620, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void closeProgram(Stage primaryStage) {
        boolean answer = CloseProgram.display("Sure you want to exit?");
        if (answer) {
            primaryStage.close();
        }
    }
}

/*var firstname_label = new Label("Fornavn:");
        firstname_label.setMinWidth(150);
        root.setConstraints(firstname_label,0,0);
        root.getChildren().add(firstname_label);
        var firstname_text = new TextField();
        root.setConstraints(firstname_text,0,1);
        root.getChildren().add(firstname_text);

        var lastname_label = new Label("Efternavn:");
        root.setConstraints(lastname_label,0,2);
        root.getChildren().add(lastname_label);
        var lastname_text = new TextField();
        root.setConstraints(lastname_text,0,3);
        root.getChildren().add(lastname_text);

        var ID_label = new Label("ID-nummer:");
        root.setConstraints(ID_label,0,4);
        root.getChildren().add(ID_label);
        var ID_text = new TextField();
        root.setConstraints(ID_text,0,5);
        root.getChildren().add(ID_text);
        */
        /*Button Search_button = new Button("CloseProgramBox");
        root.setConstraints(Search_button,0,7);
        root.getChildren().add(Search_button);*/

        /*Label public_key_label = new Label("Public Key:");
        root.setConstraints(public_key_label,1,6);
        root.getChildren().add(public_key_label);*/
        /*
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
        public_key.setMinWidth(75);
        root.getChildren().add(public_key);
*/

        /*
        Stage Search_stage = new Stage();
        Search_stage.setTitle("CloseProgramBox");
        GridPane root = new GridPane();
        root.setVgap(7);
        root.setHgap(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(root,600, 230);*/
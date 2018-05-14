package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Block;
import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.Search;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

import static javafx.geometry.Pos.CENTER;


public class MainScreen {

    static ArrayList<Block> searchResults = new ArrayList<>();
    static Text nameText = new Text();
    static Text birthdateText = new Text();
    static Text publicKeyText = new Text();
    static TableView<Block> table = new TableView<>();

    public static void screen(Stage primaryStage, Blockchain chain) {

        //top panel
        HBox topPanel = new HBox();
        topPanel.setStyle("-fx-background-color: #FFFFFF;");
        topPanel.setPadding(new Insets(5, 0, 10, 0));

        Button login_button = new Button("Login as validator");
        login_button.setOnMouseClicked(event -> Login.login(primaryStage, chain));

        topPanel.getChildren().add(login_button);
        topPanel.setAlignment(CENTER);


        //Center panel
        GridPane gridRight = new GridPane();
        gridRight.setAlignment(Pos.CENTER);
        gridRight.setVgap(10);
        gridRight.setHgap(8);
        gridRight.setPadding(new Insets(10, 20, 10, 10));
        gridRight.setStyle("-fx-background-color: #FFFFFF;");


        Label firstname_label = new Label("Name:");
        firstname_label.setMinWidth(75);
        gridRight.setConstraints(firstname_label, 0, 3);
        gridRight.getChildren().add(firstname_label);

        Button button = new Button("Goto validator");
        button.setMinWidth(100);
        gridRight.setConstraints(button, 0, 0);
        gridRight.getChildren().add(button);//TODO genesis
        button.setOnAction(e -> ValidatorScreen.validatorScreen(primaryStage, chain, (GenesisBlock) chain.getBlock(0)));


        Label birthdateLabel = new Label("Birthdate:");
        birthdateLabel.setMinWidth(75);
        gridRight.setConstraints(birthdateLabel, 0, 4);
        gridRight.getChildren().add(birthdateLabel);

        Label pbkLabel = new Label("Public key:");
        pbkLabel.setMinWidth(75);
        gridRight.setConstraints(pbkLabel, 0, 5);
        gridRight.getChildren().add(pbkLabel);


        Text text1 = new Text("Chosen person");
        text1.setStyle("-fx-font-weight: bold");
        GridPane.setHalignment(text1, HPos.CENTER);
        gridRight.setConstraints(text1, 0, 2);
        gridRight.getChildren().add(text1);


        GridPane.setHalignment(nameText, HPos.CENTER);
        gridRight.setConstraints(nameText, 0, 3);
        gridRight.getChildren().add(nameText);

        GridPane.setHalignment(birthdateText, HPos.CENTER);
        gridRight.setConstraints(birthdateText, 0, 4);
        gridRight.getChildren().add(birthdateText);


        publicKeyText.setOnMouseClicked(e -> System.out.println("Placeholder, skal eksportere key"));
        GridPane.setHalignment(publicKeyText, HPos.CENTER);
        gridRight.setConstraints(publicKeyText, 0, 5);
        gridRight.getChildren().add(publicKeyText);


        //name column
        TableColumn<Block, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("identity"));

        //DOB column
        TableColumn<Block, String> DOBColumn = new TableColumn<>("Date of Birth");
        DOBColumn.setMinWidth(200);
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("identityDOB"));

        //Pubkey column
        TableColumn<Block, String> pubKeyColumn = new TableColumn<>("Public Key");
        pubKeyColumn.setMinWidth(200);
        pubKeyColumn.setCellValueFactory(new PropertyValueFactory<>("identityPublicKey"));

        table = new TableView<>();
        table.getColumns().addAll(nameColumn, DOBColumn, pubKeyColumn);
        table.setMaxHeight(150);


        gridRight.setConstraints(table, 0, 10, 1, 1);
        gridRight.getChildren().add(table);


        TextField term_text = new TextField();
        term_text.setPromptText("Search for name, date of birth or public key");
        term_text.setOnKeyReleased(e -> {
            searchResults = getSearchResults(term_text.getText(), chain);
            table.getItems().clear();

            ObservableList<Block> blocks = FXCollections.observableArrayList();

            blocks.addAll(searchResults);

            table.setItems(blocks);
        });

        gridRight.setConstraints(term_text, 0, 9);
        gridRight.getChildren().add(term_text);


        Button selectBlockButton = new Button("Select");
        selectBlockButton.setOnMouseClicked(e -> setChosenBlockDetails());
        GridPane.setHalignment(selectBlockButton, HPos.CENTER);
        gridRight.getChildren().add(selectBlockButton);
        gridRight.setConstraints(selectBlockButton, 0, 11);


        //bottompanel
        HBox bottomPanel = new HBox();
        bottomPanel.setSpacing(150);
        bottomPanel.setStyle("-fx-background-color: #D3D3D3;");
        bottomPanel.setAlignment(CENTER);
        bottomPanel.setPadding(new Insets(10, 0, 5, 0));

        String status = "Online";
        Text onlineLabel = new Text(status);
        if (status.equals("Online")) {
            onlineLabel.setFill(Color.GREEN);
        } else onlineLabel.setFill(Color.RED);


        int numberOfNodes = 10;
        Text nodesLabel = new Text("Nodes:  " + numberOfNodes);

        int numberOfBlocks = chain.size();
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
        borderPane.setCenter(gridRight);
        borderPane.setBottom(bottomPanel);

        Scene scene = new Scene(borderPane, 800, 520);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    static void closeProgram(Stage primaryStage) {
        boolean answer = CloseProgram.display("Sure you want to exit?");
        if (answer) {
            primaryStage.close();
        }
    }

    private static ArrayList<Block> getSearchResults(String searchTerm, Blockchain chain) {
        Search search = new Search();
        ArrayList<Block> results = new ArrayList<>();

        if (searchTerm.matches("[0-9-]+")) {
            System.out.println("searching birthdate");
            //search birthdate
            //results = search.FuzzySearchIdentityPublicKey((searchTerm), chain, 5);
            return results;
        } else if (searchTerm.matches("[a-zA-Z]+")) {
            results = search.FuzzySearchIdentity((searchTerm), chain, 5);
            System.out.println("searching identity");
            return results;
        } else {
            System.out.println("earching pub key");
            return (results = search.FuzzySearchIdentityPublicKey((searchTerm), chain, 5));
        }
    }

    private static void setChosenBlockDetails() {
        //need exception catch here
        int index = table.getSelectionModel().getSelectedIndex();

        nameText.setText(searchResults.get(index).getIdentity());
        birthdateText.setText(searchResults.get(index).getIdentityDOB());
        publicKeyText.setText(searchResults.get(index).getIdentityPublicKey());
    }
}

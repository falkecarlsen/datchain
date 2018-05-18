package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.*;
import dk.aau.cs.a311c.datchain.cryptography.RSA;
import dk.aau.cs.a311c.datchain.utility.Search;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static javafx.geometry.Pos.CENTER;


class MainScreen {

    private static ArrayList<Block> searchResults = new ArrayList<>();
    private static Text identityText = new Text();
    private static Text birthdateText = new Text();
    private static Text publicKeyText = new Text();
    private static Text blockTypeText = new Text();
    private static TableView<Block> table = new TableView<>();
    private static String publicKey;

    public static void screen(Stage primaryStage, Blockchain chain) {

        //this stage uses a borderpane, which contains a top, center and bottom panel

        //top panel, which just contains the login button
        HBox topPanel = new HBox();
        topPanel.setStyle("-fx-background-color: #FFFFFF;");
        topPanel.setPadding(new Insets(5, 0, 10, 0));

        Button login_button = new Button("Login as validator");
        login_button.setOnMouseClicked(event -> Login.login(primaryStage, chain));

        topPanel.getChildren().add(login_button);
        topPanel.setAlignment(CENTER);


        //Center panel contains the search functionality
        GridPane gridRight = new GridPane();
        gridRight.setAlignment(CENTER);
        gridRight.setVgap(10);
        gridRight.setHgap(8);
        gridRight.setPadding(new Insets(10, 20, 10, 10));
        gridRight.setStyle("-fx-background-color: #FFFFFF;");

        //text for guidance, tells the user that the chosen information will be displayed under this text
        Text chosenPersonText = new Text("Chosen person");
        chosenPersonText.setStyle("-fx-font-weight: bold");
        GridPane.setHalignment(chosenPersonText, HPos.CENTER);
        GridPane.setConstraints(chosenPersonText, 0, 2);
        gridRight.getChildren().add(chosenPersonText);

        //label for displaying the identity in chosen the block
        Label firstname_label = new Label("Name:");
        firstname_label.setMinWidth(75);
        GridPane.setConstraints(firstname_label, 0, 3);
        gridRight.getChildren().add(firstname_label);

        //label for displaying the birthdate in the chosen block
        Label birthdateLabel = new Label("Birthdate:");
        birthdateLabel.setMinWidth(75);
        GridPane.setConstraints(birthdateLabel, 0, 4);
        gridRight.getChildren().add(birthdateLabel);

        //label for displaying the public key in the chosen block
        Label publicKeyLabel = new Label("Public key:");
        publicKeyLabel.setMinWidth(75);
        GridPane.setConstraints(publicKeyLabel, 0, 5);
        gridRight.getChildren().add(publicKeyLabel);

        //label for displaying the public key in the chosen block
        Label blockTypeLabel = new Label("Blocktype:");
        blockTypeLabel.setMinWidth(75);
        GridPane.setConstraints(blockTypeLabel, 0, 6);
        gridRight.getChildren().add(blockTypeLabel);

        //adding the texts for displaying the information in the chosen block
        GridPane.setHalignment(identityText, HPos.CENTER);
        GridPane.setConstraints(identityText, 0, 3);
        gridRight.getChildren().add(identityText);

        GridPane.setHalignment(birthdateText, HPos.CENTER);
        GridPane.setConstraints(birthdateText, 0, 4);
        gridRight.getChildren().add(birthdateText);

        GridPane.setHalignment(publicKeyText, HPos.CENTER);
        GridPane.setConstraints(publicKeyText, 0, 5);
        gridRight.getChildren().add(publicKeyText);

        GridPane.setHalignment(blockTypeText, HPos.CENTER);
        GridPane.setConstraints(blockTypeText, 0, 6);
        gridRight.getChildren().add(blockTypeText);

        Button savePublicKeyButton = new Button("Save public key in datchain folder");
        savePublicKeyButton.setOnMouseClicked(e -> savePublicKey());
        GridPane.setConstraints(savePublicKeyButton, 0, 0);
        gridRight.getChildren().add(savePublicKeyButton);


        //name column for the tableview
        TableColumn<Block, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("identity"));

        //DOB column for the tableview
        TableColumn<Block, String> DOBColumn = new TableColumn<>("Date of Birth");
        DOBColumn.setMinWidth(200);
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("identityDOB"));

        //Pubkey column for the tableview
        TableColumn<Block, String> pubKeyColumn = new TableColumn<>("Public Key");
        pubKeyColumn.setMinWidth(200);
        pubKeyColumn.setCellValueFactory(new PropertyValueFactory<>("identityPublicKey"));

        //sets up the tableview, with the columns
        table = new TableView<>();
        table.setPlaceholder(new Label(""));
        table.getColumns().addAll(nameColumn, DOBColumn, pubKeyColumn);
        table.setMaxHeight(150);

        //adds the tableview to the grid
        GridPane.setConstraints(table, 0, 11, 1, 1);
        gridRight.getChildren().add(table);

        //textfield for searching in the chain, can search for name, birthdate or public key, by inputting either
        //alphabetical, digits, or both
        TextField searchField = new TextField();
        searchField.setPromptText("Search for name, date of birth or public key");
        //calls function to search for the term for each keystroke
        searchField.setOnKeyReleased(e -> {
            searchResults = getSearchResults(searchField.getText(), chain);
            //clear previous showed items
            table.getItems().clear();

            //creates an observable list and adds the search results to it
            ObservableList<Block> blocks = FXCollections.observableArrayList();
            blocks.addAll(searchResults);

            //displays the search results
            table.setItems(blocks);
        });
        GridPane.setConstraints(searchField, 0, 10);
        gridRight.getChildren().add(searchField);

        //select button for selecting which block to show properties of
        Button selectBlockButton = new Button("Select");
        selectBlockButton.setOnMouseClicked(e -> setChosenBlockDetails());
        GridPane.setHalignment(selectBlockButton, HPos.CENTER);
        gridRight.getChildren().add(selectBlockButton);
        GridPane.setConstraints(selectBlockButton, 0, 12);


        //TODO REMOVE NODES AND ONLINE STATUS?
        //bottompanel with various information about the chain, online status, number of blocks and nodes
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


        //setting the scene with all the above
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topPanel);
        borderPane.setCenter(gridRight);
        borderPane.setBottom(bottomPanel);

        Scene scene = new Scene(borderPane, 800, 580);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //if the user chooses to close the program, open the popup to prompt the user if he is sure he want to close it
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            runPopUp(primaryStage);
        });
    }

    private static void savePublicKey() {
        //opens a window for the user to select a directory
        DirectoryChooser directory = new DirectoryChooser();
        File selectedDirectory = directory.showDialog(null);

        RSA.publicKeyWriter(publicKey, selectedDirectory);
    }

    private static void runPopUp(Stage primaryStage) {
        //calls the popUp to verify the user wants to close the program
        boolean answer = CloseProgram.display();
        if (answer) {
            primaryStage.close();
        }
    }

    //method to search in the chain, based on the input in GUI
    private static ArrayList<Block> getSearchResults(String searchTerm, Blockchain chain) {
        //create class to search and array for search results
        Search search = new Search();
        ArrayList<Block> results = new ArrayList<>();

        //if the user input just numbers and hyphen, search for date of birth in the chain
        if (searchTerm.matches("[0-9-]+")) {
            System.out.println("searching birthdate");
            results = search.FuzzySearchIdentityDOB((searchTerm), chain, 5);
            return results;
            //if the user input just alphabetical characters, search for identity in the chain
        } else if (searchTerm.matches("[a-zA-Z]+")) {
            results = search.FuzzySearchIdentity((searchTerm), chain, 5);
            System.out.println("searching identity");
            return results;
        } else {
            //if none of the above is true, search for pub key, which can have many different characters
            System.out.println("earching pub key");
            return (results = search.FuzzySearchIdentityPublicKey((searchTerm), chain, 5));
        }
    }

    //Sets the properties of the selected block, when the select button is pushed
    private static void setChosenBlockDetails() {
        //gets the selected index of the table, and returns value of the same index from the search results
        int index = table.getSelectionModel().getSelectedIndex();
        if (0 <= index && index <= 4) {
            identityText.setText(searchResults.get(index).getIdentity());
            birthdateText.setText(searchResults.get(index).getIdentityDOB());
            //the public key is made into a substring, because of the length of the public key
            publicKeyText.setText(searchResults.get(index).getIdentityPublicKey().substring(0, 40) + "...");
            publicKey = searchResults.get(index).getIdentityPublicKey();
            if (searchResults.get(index) instanceof GenesisBlock) {
                blockTypeText.setText("Genesis");
            } else if (searchResults.get(index) instanceof ValidatorBlock) {
                blockTypeText.setText("Validator");
            } else if (searchResults.get(index) instanceof CitizenBlock) {
                blockTypeText.setText("Citizen");
            }
        }
    }
}

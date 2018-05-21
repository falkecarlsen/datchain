package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.security.PrivateKey;
import java.util.ArrayList;

public class VoidScreen {

    private static ArrayList<Block> searchResults = new ArrayList<>();
    private static TableView<Block> table = new TableView<>();

    public static void voidingBlock(Stage primaryStage, Blockchain chain, Block block, PrivateKey validatorPrivateKey) {

        //setting up the gridpane to be used in the scene
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(8);
        grid.setPadding(new Insets(10, 20, 10, 10));
        grid.setStyle("-fx-background-color: #FFFFFF;");

        //back button to return to mainscreen, returns the chain
        Button logoutButton = new Button("Logout");
        logoutButton.setMinWidth(70);
        logoutButton.setOnAction(e -> MainScreen.screen(primaryStage, chain));
        GridPane.setHalignment(logoutButton, HPos.CENTER);
        GridPane.setConstraints(logoutButton, 0, 0);
        grid.getChildren().add(logoutButton);

        Label label = new Label("Select the block to void");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setConstraints(label, 1, 2);
        grid.getChildren().add(label);

        Button switchButton = new Button("Switch");
        switchButton.setMinWidth(70);
        switchButton.setOnAction(e -> ValidatorScreen.validatorScreen(primaryStage, chain, block, validatorPrivateKey));
        GridPane.setHalignment(switchButton, HPos.CENTER);
        GridPane.setConstraints(switchButton, 2, 0);
        grid.getChildren().add(switchButton);

        //textfield for searching in the chain, can search for name, birthdate or public key, by inputting either
        //alphabetical, digits, or both
        TextField searchField = new TextField();
        searchField.setPromptText("Search for name, date of birth or public key");
        //calls function to search for the term for each keystroke
        searchField.setOnKeyReleased(e -> {
            searchResults = MainScreen.getSearchResults(searchField.getText(), chain);
            //clear previous showed items
            table.getItems().clear();

            //creates an observable list and adds the search results to it
            ObservableList<Block> blocks = FXCollections.observableArrayList();
            blocks.addAll(searchResults);

            //displays the search results
            table.setItems(blocks);
        });
        GridPane.setConstraints(searchField, 1, 3);
        grid.getChildren().add(searchField);


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
        GridPane.setConstraints(table, 1, 4, 1, 1);
        grid.getChildren().add(table);
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                addVoidedBlock(chain, block, validatorPrivateKey);
            }
        });


        //select button for selecting which block to show properties of
        Button selectBlockButton = new Button("Void this block");
        selectBlockButton.setOnMouseClicked(e -> addVoidedBlock(chain, block, validatorPrivateKey));
        GridPane.setHalignment(selectBlockButton, HPos.CENTER);
        grid.getChildren().add(selectBlockButton);
        GridPane.setConstraints(selectBlockButton, 1, 5);

        Scene scene = new Scene(grid, 800, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void addVoidedBlock(Blockchain chain, Block block, PrivateKey validatorPrivateKey) {

        //TODO USE VALIDATE CHAIN, CHECK IF BLOCK IS ALREADY VOIDED
        //TODO add error labels and such?
        //gets the selected index of the table, and returns value of the same index from the search results
        int index = table.getSelectionModel().getSelectedIndex();
        if ((0 <= index && index <= 4)) {
            String birthdate = searchResults.get(index).getIdentityDOB();
            String publicKey = searchResults.get(index).getIdentityPublicKey();

            //gets the hash of the last block
            String prevHash = chain.getHead().getHash();

            //if the user that logged in (block) is a genesis, the user can only void validator blocks
            //the new block is added to the chain, with the required information
            if ((block instanceof GenesisBlock) && (searchResults.get(index) instanceof ValidatorBlock)) {
                chain.addValidatedBlock(new ValidatorBlock("Revoked", birthdate, publicKey, prevHash, validatorPrivateKey), block);
                //setLabelsAfterSubmittedBlock(chain);
                //else the user is a validator, and can add citizen blocks
            } else if ((block instanceof ValidatorBlock) && (searchResults.get(index) instanceof CitizenBlock)) {
                chain.addValidatedBlock(new CitizenBlock("Revoked", birthdate, publicKey, prevHash, block.getIdentity(), block.getIdentityPublicKey(), validatorPrivateKey), block);
                //setLabelsAfterSubmittedBlock(chain);
            }
        }
    }
}



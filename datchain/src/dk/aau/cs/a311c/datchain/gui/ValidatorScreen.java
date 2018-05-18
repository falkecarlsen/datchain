package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.*;
import dk.aau.cs.a311c.datchain.cryptography.RSA;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


class ValidatorScreen {
    //input data
    private static String identity;
    private static String identityDOB;

    //the label, buttons and textfields to be used on the stage
    private static Button addBlockButton = new Button("Check if data is correct and submit block");
    private static TextField DOBText = new TextField();
    private static TextField identityText = new TextField();
    private static Label succesLabel = new Label();
    private static Label errorLabel = new Label("");
    private static Label userPromptLabel = new Label();

    public static void validatorScreen(Stage primaryStage, Blockchain chain, Block block, PrivateKey validatorPrivateKey) {
        //if somehow a citizen got logged in, return to mainscreen
        if (!((block instanceof GenesisBlock) || (block instanceof ValidatorBlock))) {
            MainScreen.screen(primaryStage, chain);
        }

        //setting up the gridpane to be used in the scene
        GridPane gridCenter = new GridPane();
        gridCenter.setVgap(10);
        gridCenter.setHgap(8);
        gridCenter.setPadding(new Insets(10, 20, 10, 10));
        gridCenter.setStyle("-fx-background-color: #FFFFFF;");

        //back button to return to mainscreen, returns the chain
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainScreen.screen(primaryStage, chain));
        GridPane.setHalignment(logoutButton, HPos.CENTER);
        GridPane.setConstraints(logoutButton, 0, 0);
        gridCenter.getChildren().add(logoutButton);

        //direction label, which text changes depending on who logged in
        if (block instanceof GenesisBlock) {
            userPromptLabel.setText("Enter the details of the validator you wish to add");
        } else if (block instanceof ValidatorBlock) {
            userPromptLabel.setText("Enter the details of the citizen you wish to add");
        }

        //adds the directionlabel to the gridpane
        GridPane.setHalignment(userPromptLabel, HPos.CENTER);
        GridPane.setConstraints(userPromptLabel, 1, 4);
        gridCenter.getChildren().add(userPromptLabel);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);

        //error label for wrong DOB format, positioned under the date of birth textfield
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorLabel, 1, 7);
        gridCenter.getChildren().add(errorLabel);

        //button to be used to save the input data, saves both identity and DOB
        Button saveDataButton = new Button("Save data");
        GridPane.setHalignment(saveDataButton, HPos.CENTER);
        saveDataButton.setOnAction(e -> saveInput(identityText.getText(), DOBText.getText()));
        GridPane.setConstraints(saveDataButton, 1, 8);
        gridCenter.getChildren().add(saveDataButton);

        //button to submit the block to the chain, is invisible until the user input data in the correct format
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setVisible(false);
        addBlockButton.setOnAction(e -> submitBlock(chain, block, validatorPrivateKey));
        GridPane.setConstraints(addBlockButton, 1, 9);
        gridCenter.getChildren().add(addBlockButton);

        //label to appear after block is submitted, is invisible until block is submitted
        GridPane.setHalignment(succesLabel, HPos.CENTER);
        succesLabel.setTextFill(Color.GREEN);
        GridPane.setConstraints(succesLabel, 1, 9);
        gridCenter.getChildren().add(succesLabel);
        succesLabel.setVisible(false);

        //setting up the scene in a borderpane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridCenter);
        Scene scene = new Scene(borderPane, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    //saves the input from user to variables
    private static void saveInput(String identityInput, String DOBInput) {
        succesLabel.setVisible(false);
        //íf the identity input consists of just alphabetical characters and spaces, save the input
        if (identityInput.matches("[a-zA-ZæøåÆØÅ ]+")) {
            identity = identityInput;
            errorLabel.setText("");
            //if the date of birth input is of the format xx-xx-xxxx, where x are integers, save the input
            if (DOBInput.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                identityDOB = DOBInput;
                errorLabel.setText("");
                //at this point, set the submit button visible
                addBlockButton.setVisible(true);
            } else errorLabel.setText("Wrong date of birth format, try again");
        } else errorLabel.setText("Wrong identity format, supports only alphabetical");
    }

    private static void submitBlock(Blockchain chain, Block block, PrivateKey validatorPrivateKey) {
        //generates keys
        KeyPair keyPair = RSA.keyPairInit();

        //saves the keys on a file
        String destination = "data/gui/createdBlock/" + identityText.getText() + "/";
        RSA.keyPairWriter(keyPair, destination);

        //RSA.keyPairWriter(genesisKeypair, "data/gui/genesis/");

        //encodes public key to add to new block
        String encodedPublicKey = RSA.getEncodedPublicKey(keyPair);

        //gets the hash of the last block
        String prevHash = chain.getHead().getHash();

        //if the user that logged in (block) is a genesis, the user can only add validator blocks
        //the new block is added to the chain, with the required information
        if (block instanceof GenesisBlock) {
            chain.addValidatedBlock(new ValidatorBlock(identity, identityDOB, encodedPublicKey, prevHash, validatorPrivateKey), block);
            setLabelsAfterSubmittedBlock(chain);
            //else the user is a validator, and can add citizen blocks
        } else if (block instanceof ValidatorBlock) {
            chain.addValidatedBlock(new CitizenBlock(identity, identityDOB, encodedPublicKey, prevHash, block.getIdentity(), block.getIdentityPublicKey(), validatorPrivateKey), block);
            setLabelsAfterSubmittedBlock(chain);
        }
    }

    private static void setLabelsAfterSubmittedBlock(Blockchain chain) {
        succesLabel.setVisible(true);
        if (chain.validateChain()) {
            addBlockButton.setVisible(false);
            succesLabel.setText("Success! Keys written to data folder");
            //clears the saved data
            identityText.clear();
            DOBText.clear();
        } else succesLabel.setText("Something went wrong");
    }

    private static void submitGenesis(Stage primaryStage) {
        //generates key for the genesis and stores them
        KeyPair genesisKeypair = RSA.keyPairInit();
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/gui/genesis/");

        //creates the genesis and adds it to a new chain
        GenesisBlock genesis01 = new GenesisBlock(identity, identityDOB, RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        //opens mainscreen
        MainScreen.screen(primaryStage, chain);
    }


    public static void initialStartup(Stage primaryStage) {

        //setting up the gridpane to be used in the scene
        GridPane gridCenter = new GridPane();
        gridCenter.setVgap(10);
        gridCenter.setHgap(8);
        gridCenter.setPadding(new Insets(10, 20, 10, 10));
        gridCenter.setStyle("-fx-background-color: #FFFFFF;");


        //direction label
        userPromptLabel.setText("Enter the details of the genesis");

        //adds the directionlabel to the gridpane
        GridPane.setHalignment(userPromptLabel, HPos.CENTER);
        GridPane.setConstraints(userPromptLabel, 1, 4);
        gridCenter.getChildren().add(userPromptLabel);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);

        //error label for wrong DOB format, positioned under the date of birth textfield
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorLabel, 1, 7);
        gridCenter.getChildren().add(errorLabel);

        //button to be used to save the input data, saves both identity and DOB
        Button saveDataButton = new Button("Save data");
        GridPane.setHalignment(saveDataButton, HPos.CENTER);
        saveDataButton.setOnAction(e -> saveInput(identityText.getText(), DOBText.getText()));
        GridPane.setConstraints(saveDataButton, 1, 8);
        gridCenter.getChildren().add(saveDataButton);

        //button to submit the block to the chain, is invisible until the user input data in the correct format
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setVisible(false);
        addBlockButton.setOnAction(e -> ValidatorScreen.submitGenesis(primaryStage));
        GridPane.setConstraints(addBlockButton, 1, 9);
        gridCenter.getChildren().add(addBlockButton);

        //label to appear after block is submitted, is invisible until block is submitted
        GridPane.setHalignment(succesLabel, HPos.CENTER);
        succesLabel.setTextFill(Color.GREEN);
        GridPane.setConstraints(succesLabel, 1, 9);
        gridCenter.getChildren().add(succesLabel);
        succesLabel.setVisible(false);

        //setting up the scene in a borderpane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridCenter);
        Scene scene = new Scene(borderPane, 260, 275);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
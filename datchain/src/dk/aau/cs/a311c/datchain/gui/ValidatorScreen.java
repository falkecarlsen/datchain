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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


class ValidatorScreen {
    //the label, buttons and textfields to be used on the stage
    private static final TextField DOBText = new TextField();
    private static final TextField identityText = new TextField();
    private static final Label succesLabel = new Label();
    private static final Label errorLabel = new Label("");

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
        logoutButton.setMinWidth(100);
        logoutButton.setOnAction(e -> MainScreen.screen(primaryStage, chain));
        GridPane.setHalignment(logoutButton, HPos.CENTER);
        GridPane.setConstraints(logoutButton, 0, 0);
        gridCenter.getChildren().add(logoutButton);

        Button switchButton = new Button("Revoke block");
        switchButton.setMinWidth(100);
        switchButton.setOnAction(e -> VoidScreen.voidingBlock(primaryStage, chain, block, validatorPrivateKey));
        GridPane.setHalignment(switchButton, HPos.CENTER);
        GridPane.setConstraints(switchButton, 2, 0);
        gridCenter.getChildren().add(switchButton);

        //adds the directionlabel to the gridpane
        Label userPromptLabel = new Label();
        userPromptLabel.setMinWidth(300);
        GridPane.setHalignment(userPromptLabel, HPos.CENTER);
        GridPane.setConstraints(userPromptLabel, 1, 4);
        gridCenter.getChildren().add(userPromptLabel);

        //direction label, which text changes depending on who logged in
        if (block instanceof GenesisBlock) {
            userPromptLabel.setText("Enter the details of the validator you wish to add");
        } else if (block instanceof ValidatorBlock) {
            userPromptLabel.setText("Enter the details of the citizen you wish to add");
        }

        //identity textfield
        identityText.setPromptText("Enter the identity");
        identityText.setMinWidth(300);
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setMinWidth(300);
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);

        //error label for wrong DOB format, positioned under the date of birth textfield
        errorLabel.setMinWidth(300);
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorLabel, 1, 7);
        gridCenter.getChildren().add(errorLabel);

        //button to submit the block to the chain, is invisible until the user input data in the correct format
        Button addBlockButton = new Button("Check data is correct and submit");
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setOnAction(e -> submitBlock(chain, block, validatorPrivateKey,
                identityText.getText(), DOBText.getText()));
        GridPane.setConstraints(addBlockButton, 1, 8);
        gridCenter.getChildren().add(addBlockButton);

        //label to appear after block is submitted, is invisible until block is submitted
        GridPane.setHalignment(succesLabel, HPos.CENTER);
        GridPane.setConstraints(succesLabel, 1, 9);
        gridCenter.getChildren().add(succesLabel);
        succesLabel.setVisible(false);

        //setting up the scene in a borderpane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridCenter);
        Scene addBlockScene = new Scene(borderPane, 550, 280);

        primaryStage.setScene(addBlockScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void submitBlock(Blockchain chain, Block block, PrivateKey validatorPrivateKey,
                                    String identityInput, String DOBInput) {
        Boolean correctInput = checkInput(identityInput, DOBInput);

        if (correctInput) {
            //generates keys
            KeyPair keyPair = RSA.keyPairInit();

            //if createdBlockDirectory doesn't exist, create the directory
            String createdBlockDirectory = "data/gui/createdBlocks/";
            if (!Files.exists(Paths.get(createdBlockDirectory))) {
                try {
                    Files.createDirectory(Paths.get(createdBlockDirectory));
                } catch (IOException e) {
                    System.out.println("ERROR: When creating directory " + createdBlockDirectory +
                            " following error occured: " + e.getMessage());
                }
            }

            //saves the keys onto files, by directory of formatted identity-name
            String destination = createdBlockDirectory + identityText.getText().replaceAll(" ", "_").toLowerCase() + "/";
            RSA.keyPairWriter(keyPair, destination);


            //encodes public key to add to new block
            String encodedPublicKey = RSA.getEncodedPublicKey(keyPair);

            //gets the hash of the last block
            String prevHash = chain.getHead().getHash();

            //if the user that logged in (block) is a genesis, the user can only add validator blocks
            //the new block is added to the chain, with the required information
            if (block instanceof GenesisBlock) {
                chain.addValidatedBlock(new ValidatorBlock(identityInput, DOBInput, encodedPublicKey,
                        prevHash, validatorPrivateKey), block);
                setLabelsAfterSubmittedBlock(chain);
                //else the user is a validator, and can add citizen blocks
            } else if (block instanceof ValidatorBlock) {
                chain.addValidatedBlock(new CitizenBlock(identityInput, DOBInput, encodedPublicKey,
                        prevHash, block.getIdentity(), block.getIdentityPublicKey(), validatorPrivateKey), block);
                setLabelsAfterSubmittedBlock(chain);
            }
        }
    }

    private static void setLabelsAfterSubmittedBlock(Blockchain chain) {
        succesLabel.setVisible(true);
        if (chain.validateChain()) {
            succesLabel.setTextFill(Color.GREEN);
            succesLabel.setText("Success! Keys written to data folder");
            //clears the saved data
            identityText.clear();
            DOBText.clear();
        } else {
            succesLabel.setTextFill(Color.RED);
            succesLabel.setText("Something went wrong");
        }
    }

    private static void submitGenesis(Stage primaryStage, String identityInput, String DOBInput) {
        boolean correctInput = checkInput(identityInput, DOBInput);

        if (correctInput) {
            //generates key for the genesis and stores them
            KeyPair genesisKeypair = RSA.keyPairInit();
            PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
            RSA.keyPairWriter(genesisKeypair, "data/gui/genesis/");

            //creates the genesis
            GenesisBlock genesis = new GenesisBlock(identityInput, DOBInput,
                    RSA.getEncodedPublicKey(genesisPublicKey), "0000");

            //adds the genesis to the new chain and clears the textfields
            identityText.clear();
            DOBText.clear();

            //opens mainscreen
            MainScreen.screen(primaryStage, new Blockchain(genesis));
        }
    }

    private static boolean checkInput(String identityInput, String DOBInput) {
        //First check input
        succesLabel.setVisible(false);
        //íf the identity input consists of just alphabetical characters and spaces, save the input
        if (identityInput.matches("[a-zA-ZæøåÆØÅ\\- ]+")) {
            errorLabel.setText("");
            //if the date of birth input is of the format xx-xx-xxxx, where x are integers, save the input
            if (DOBInput.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                errorLabel.setText("");
                return true;
            } else errorLabel.setText("Wrong date of birth format, try again");
            return false;
        } else errorLabel.setText("Wrong identity format, supports only alphabetical");
        return false;
    }


    public static void initialStartup(Stage primaryStage) {

        //setting up the gridpane to be used in the scene
        GridPane gridCenter = new GridPane();
        gridCenter.setVgap(10);
        gridCenter.setHgap(8);
        gridCenter.setPadding(new Insets(10, 20, 10, 10));
        gridCenter.setStyle("-fx-background-color: #FFFFFF;");


        //direction label
        Label userPromptLabel = new Label("Enter the details of the genesis");
        GridPane.setHalignment(userPromptLabel, HPos.CENTER);
        GridPane.setConstraints(userPromptLabel, 1, 4);
        gridCenter.getChildren().add(userPromptLabel);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        identityText.setMinWidth(300);
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        DOBText.setMinWidth(300);
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);

        //error label for wrong DOB format, positioned under the date of birth textfield
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorLabel, 1, 7);
        gridCenter.getChildren().add(errorLabel);

        //button to submit the block to the chain, is invisible until the user input data in the correct format
        Button addBlockButton = new Button("Check data is correct and submit");
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setOnAction(e -> ValidatorScreen.submitGenesis(primaryStage, identityText.getText(), DOBText.getText()));
        GridPane.setConstraints(addBlockButton, 1, 8);
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
        Scene scene = new Scene(borderPane, 350, 275);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
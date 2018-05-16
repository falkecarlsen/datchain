package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.*;
import dk.aau.cs.a311c.datchain.utility.RSA;
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


public class ValidatorScreen {

    //input data
    private static String identity;
    private static String identityDOB;
    private static String prevHash;
    private static String genesisSignature;
    private static String validatorSignature;


    //choose pubkey, pub key should be generated here
    private static TextField pubKeyText = new TextField();

    //choose prev hash, could be automatic?
    private static TextField prevHashText = new TextField();

    //choose signature
    private static TextField signatureText = new TextField();

    //addblockbutton
    private static Button addBlockButton = new Button("");

    private static TextField DOBText = new TextField();

    //succes label
    private static Label succesLabel = new Label("Success!");

    private static Label errorLabel = new Label("");

    private static TextField identityText = new TextField();

    private static Label label = new Label();

    public static void validatorScreen(Stage primaryStage, Blockchain chain, Block block) {

        //if somehow a citizen got logged in, return to mainscreen
        if (!((block instanceof GenesisBlock) || (block instanceof ValidatorBlock)))  {
            MainScreen.screen(primaryStage,chain);
        }

        GridPane gridCenter = new GridPane();
        gridCenter.setVgap(10);
        gridCenter.setHgap(8);
        gridCenter.setPadding(new Insets(10, 20, 10, 10));
        gridCenter.setStyle("-fx-background-color: #FFFFFF;");

        //back button
        Button backButton = new Button("Return");
        backButton.setOnAction(e -> MainScreen.screen(primaryStage, chain));
        GridPane.setHalignment(backButton, HPos.CENTER);
        GridPane.setConstraints(backButton, 0, 0);
        gridCenter.getChildren().add(backButton);

        //direction label
        if ( block instanceof GenesisBlock) {
            label.setText("Enter the details of the validator you wish to add");
        } else if ( block instanceof ValidatorBlock) {
            label.setText("Enter the details of the citizen you wish to add");
        }
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setConstraints(label, 1, 4);
        gridCenter.getChildren().add(label);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);


        //error label for wrong DOB format
        GridPane.setHalignment(errorLabel, HPos.CENTER);
        errorLabel.setTextFill(Color.RED);
        GridPane.setConstraints(errorLabel, 1, 7);
        gridCenter.getChildren().add(errorLabel);

        //choose pubkey, pub key should be generated here, not be an input. could also choose file
        /*pubKeyText.setPromptText("Enter the public Key");
        pubKeyText.setOnAction(e -> System.out.println("Added public key"));
        gridCenter.setConstraints(pubKeyText, 1, 7);
        gridCenter.getChildren().add(pubKeyText);

        //choose prev hash, should be automatic, not text field
        prevHashText.setPromptText("Enter the previous hash");
        prevHashText.setOnAction(e -> System.out.println("Added prev hash"));
        gridCenter.setConstraints(prevHashText, 1, 8);
        gridCenter.getChildren().add(prevHashText);*/

        //choose signature, should be automatic, not text field
        /*signatureText.setPromptText("Enter the genesis signature");
        signatureText.setOnAction(e -> System.out.println("Added signature"));
        gridCenter.setConstraints(signatureText, 1, 7);
        gridCenter.getChildren().add(signatureText);*/

        //save input data
        Button saveDataButton = new Button("Save data");
        GridPane.setHalignment(saveDataButton, HPos.CENTER);
        saveDataButton.setOnAction(e -> saveInput(chain, identityText.getText(), DOBText.getText()));
        GridPane.setConstraints(saveDataButton, 1, 8);
        gridCenter.getChildren().add(saveDataButton);


        addBlockButton.setText("Check if data is correct and submit block");
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setOnAction(e -> submitBlock(chain, block));
        GridPane.setConstraints(addBlockButton, 1, 9);
        gridCenter.getChildren().add(addBlockButton);
        addBlockButton.setVisible(false);

        GridPane.setHalignment(succesLabel, HPos.CENTER);
        succesLabel.setTextFill(Color.GREEN);
        GridPane.setConstraints(succesLabel, 1, 9);
        gridCenter.getChildren().add(succesLabel);
        succesLabel.setVisible(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridCenter);
        Scene scene = new Scene(borderPane, 400, 300);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }



    private static void saveInput(Blockchain chain, String identityInput, String DOBInput) {
        succesLabel.setVisible(false);
        if (identityInput.matches("[a-z A-Z]")) {
            identity = identityInput;
            if (DOBInput.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
                identityDOB = DOBInput;
                addBlockButton.setVisible(true);
            } else errorLabel.setText("Wrong date of birth format, try again");
        } else errorLabel.setText("Wrong identity format, supports only alphabetical");
    }

    private static void submitBlock(Blockchain chain, Block block) {

        //generates keys
        KeyPair keyPair = RSA.keyPairInit();

        //saves the keys on a file
        RSA.keyPairWriter(keyPair, "data/gui/");

        //encodes public key to add to new block
        String encodedPublicKey = RSA.getEncodedPublicKey(keyPair);
        //TODO SIGNATURE
        //gets the hash of the last block
        prevHash = chain.getHead().getHash();
        if (block instanceof GenesisBlock) {
            chain.addValidatedBlock(new ValidatorBlock(identity, identityDOB, encodedPublicKey, prevHash, genesisSignature), block);
        } else if (block instanceof ValidatorBlock) {
            chain.addValidatedBlock(new CitizenBlock(identity,identityDOB,encodedPublicKey,prevHash,block.getIdentity(),block.getIdentityPublicKey(),validatorSignature), block);
        }
        addBlockButton.setVisible(false);
        succesLabel.setVisible(true);

        identityText.clear();
        DOBText.clear();
    }
}
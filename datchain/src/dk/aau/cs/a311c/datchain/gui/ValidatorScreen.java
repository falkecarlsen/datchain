package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Block;
import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
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

    private static Label birthdateLabel = new Label("Wrong format, try again");

    private static TextField identityText = new TextField();

    public static void validatorScreen(Stage primaryStage, Blockchain chain, Block block) {

        if (block instanceof GenesisBlock) {
            System.out.println("logged in as genesis");
        } else if (block instanceof ValidatorBlock) {
            System.out.println("logged in as validator");
        } else System.out.println("oh shit, you shouldnt be here, citizen");

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
        Label label = new Label("Enter the details of the validator you wish to add");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setConstraints(label, 1, 4);
        gridCenter.getChildren().add(label);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        identityText.setOnAction(e -> saveIdentityInput(identityText.getText()));
        GridPane.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield mangler logik her til at checke formatet
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        DOBText.setOnAction(e -> saveDOBInput(DOBText.getText()));
        GridPane.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);


        GridPane.setHalignment(birthdateLabel, HPos.CENTER);
        birthdateLabel.setVisible(false);
        birthdateLabel.setTextFill(Color.RED);
        GridPane.setConstraints(birthdateLabel, 1, 7);
        gridCenter.getChildren().add(birthdateLabel);

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

        //add validator button
        Button saveDataButton = new Button("Save data");
        GridPane.setHalignment(saveDataButton, HPos.CENTER);
        saveDataButton.setOnAction(e -> saveInput(chain));
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

    private static void saveIdentityInput(String input) {
        identity = input;
    }

    private static void saveDOBInput(String input) {
        System.out.println(input);
        if (input.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            identityDOB = input;
            System.out.println("succes");
        } else birthdateLabel.setVisible(true);
    }

    private static void saveInput(Blockchain chain) {
        prevHash = chain.getHead().getHash();
        genesisSignature = signatureText.getText();

        addBlockButton.setVisible(true);
    }

    private static void submitBlock(Blockchain chain, Block block) {
        //generates keys
        KeyPair keyPair = RSA.keyPairInit();

        //saves the keys on a file
        RSA.keyPairWriter(keyPair, "data/gui/");

        //encodes public key to add to new block
        String encodedPublicKey = RSA.getEncodedPublicKey(keyPair);

        chain.addValidatedBlock(new ValidatorBlock(identity, identityDOB, encodedPublicKey, prevHash, genesisSignature), block);
        addBlockButton.setVisible(false);
        succesLabel.setVisible(true);

        identityText.clear();
        DOBText.clear();
        pubKeyText.clear();
        prevHashText.clear();
        signatureText.clear();
    }

//TODO genesis signature
    //todo lagre data?
}
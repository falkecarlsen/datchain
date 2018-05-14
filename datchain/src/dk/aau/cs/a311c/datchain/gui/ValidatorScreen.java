package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ValidatorScreen {

    //input data
    static String identity = new String();
    static String identityDOB = new String();
    static String identityPublicKey = new String();
    static String prevHash = new String();
    static String genesisSignature = new String();

    //identity textfield
    static TextField identityText = new TextField();

    //birthdate textfield
    static TextField DOBText = new TextField();

    //choose pubkey, pub key should be generated here
    static TextField pubKeyText = new TextField();


    //choose prev hash, could be automatic?
    static TextField prevHashText = new TextField();

    //choose signature
    static TextField signatureText = new TextField();

    //addblockbutton
    static Button addBlockButton = new Button("");

    //succes label
    static Label succesLabel = new Label("Success!");


    public static void validatorScreen(Stage primaryStage, Blockchain chain, ValidatorBlock validator) {

        GridPane gridCenter = new GridPane();
        gridCenter.setVgap(10);
        gridCenter.setHgap(8);
        gridCenter.setPadding(new Insets(10, 20, 10, 10));
        gridCenter.setStyle("-fx-background-color: #FFFFFF;");

        //back button
        Button backButton = new Button("Return");
        backButton.setOnAction(e -> MainScreen.screen(primaryStage,chain));
        GridPane.setHalignment(backButton, HPos.CENTER);
        gridCenter.setConstraints(backButton, 0, 0);
        gridCenter.getChildren().add(backButton);

        //direction label
        Label label = new Label("Enter the details of the validator you wish to add");
        GridPane.setHalignment(label, HPos.CENTER);
        gridCenter.setConstraints(label, 1, 4);
        gridCenter.getChildren().add(label);

        //identity textfield
        identityText.setPromptText("Enter the identity");
        identityText.setOnAction(e -> System.out.println("Added identity"));
        gridCenter.setConstraints(identityText, 1, 5);
        gridCenter.getChildren().add(identityText);

        //birthdate textfield
        DOBText.setPromptText("Enter the date of birth, DD-MM-YYYY");
        DOBText.setOnAction(e -> System.out.println("Added Date of birth in the format DD-MM-YYYY"));
        gridCenter.setConstraints(DOBText, 1, 6);
        gridCenter.getChildren().add(DOBText);

        //choose pubkey, pub key should be generated here, not be an input. could also choose file
        pubKeyText.setPromptText("Enter the public Key");
        pubKeyText.setOnAction(e -> System.out.println("Added public key"));
        gridCenter.setConstraints(pubKeyText, 1, 7);
        gridCenter.getChildren().add(pubKeyText);

        //choose prev hash, should be automatic, not text field
        prevHashText.setPromptText("Enter the previous hash");
        prevHashText.setOnAction(e -> System.out.println("Added previous hash"));
        gridCenter.setConstraints(prevHashText, 1, 8);
        gridCenter.getChildren().add(prevHashText);

        //choose signature, should be automatic, not text field
        signatureText.setPromptText("Enter the genesis signature");
        signatureText.setOnAction(e -> System.out.println("Added signature"));
        gridCenter.setConstraints(signatureText, 1, 9);
        gridCenter.getChildren().add(signatureText);

        //add validator button
        Button saveDataButton = new Button("Save data");
        GridPane.setHalignment(saveDataButton, HPos.CENTER);
        saveDataButton.setOnAction( e -> saveInput());
        gridCenter.setConstraints(saveDataButton, 1, 10);
        gridCenter.getChildren().add(saveDataButton);


        addBlockButton.setText("Check if data is correct and submit block");
        GridPane.setHalignment(addBlockButton, HPos.CENTER);
        addBlockButton.setOnAction( e -> submitBlock(chain, validator));
        gridCenter.setConstraints(addBlockButton, 1, 11);
        gridCenter.getChildren().add(addBlockButton);
        addBlockButton.setVisible(false);

        GridPane.setHalignment(succesLabel, HPos.CENTER);
        succesLabel.setTextFill(Color.GREEN);
        gridCenter.setConstraints(succesLabel, 1, 11);
        gridCenter.getChildren().add(succesLabel);
        succesLabel.setVisible(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridCenter);
        Scene scene = new Scene(borderPane, 400, 350);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void saveInput() {
        identity = identityText.getText().toString();
        identityDOB = DOBText.getText().toString();
        identityPublicKey = pubKeyText.getText().toString();
        prevHash = prevHashText.getText().toString();
        genesisSignature = signatureText.getText().toString();
        addBlockButton.setVisible(true);
        succesLabel.setVisible(false);
    }

    private static void submitBlock(Blockchain chain, ValidatorBlock validator) {
        chain.addValidatedBlock(new ValidatorBlock(identity,identityDOB,identityPublicKey,prevHash,genesisSignature),validator);
        addBlockButton.setVisible(false);
        succesLabel.setVisible(true);

        identityText.clear();
        DOBText.clear();
        pubKeyText.clear();
        prevHashText.clear();
        signatureText.clear();
    }

}

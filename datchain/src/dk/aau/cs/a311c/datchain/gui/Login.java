package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Block;
import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
import dk.aau.cs.a311c.datchain.cryptography.CipherBlock;
import dk.aau.cs.a311c.datchain.cryptography.RandomChallenge;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import static dk.aau.cs.a311c.datchain.cryptography.RSA.*;

class Login {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private static Label labelLogin = new Label();
    private static Label labelPrivateKey = new Label();
    private static Label labelPublicKey = new Label();

    public static void login(Stage primaryStage, Blockchain chain) {

        //setting up the gridpane layout to be used
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #FFFFFF;");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);


        //labels for user guidance
        Label label = new Label("Choose your key files");
        label.setMaxWidth(140);
        label.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label, 1, 0);
        gridPane.getChildren().add(label);

        //label under the private key load button
        labelPrivateKey.setMaxWidth(140);
        labelPrivateKey.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelPrivateKey, 0, 2);
        gridPane.getChildren().add(labelPrivateKey);

        //label under the public key load button
        labelPublicKey.setMaxWidth(140);
        labelPublicKey.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelPublicKey, 1, 2);
        gridPane.getChildren().add(labelPublicKey);

        //label under the challenge button
        labelLogin.setMaxWidth(140);
        labelLogin.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelLogin, 2, 2);
        gridPane.getChildren().add(labelLogin);


        //setting up the 4 buttons
        //private key button
        Button privateKeyButton = new Button("Private key");
        privateKeyButton.setMinWidth(140);
        //when pressed, save the file the user chooses, as a private key
        privateKeyButton.setOnAction(e -> {
            privateKey = loadPrivateKey();
            if (privateKey != null) {
                //if the file gets loaded, set the text under the button to "chosen"
                labelPrivateKey.setText("Private key chosen");
            }
            //if both a public and private key is loaded, prompt the user to login by setting the label text
            if ((publicKey != null) && (privateKey != null)) {
                labelLogin.setText("Ready to login");
            }
        });
        //positioned to the left
        GridPane.setConstraints(privateKeyButton, 0, 1);
        gridPane.getChildren().add(privateKeyButton);

        //public key button
        Button publicKeyButton = new Button("Public key");
        publicKeyButton.setMinWidth(140);
        //when pressed, save the file the user chooses, as a public key
        publicKeyButton.setOnAction(e -> {
            publicKey = loadPublicKey();
            if (publicKey != null) {
                //if the file gets loaded, set the text under the button to "chosen"
                labelPublicKey.setText("Public key chosen");
            }
            //if both a public and private key is loaded, prompt the user to login by setting the label text
            if ((publicKey != null) && (privateKey != null)) {
                labelLogin.setText("Ready to login");
            }
        });
        //positioned in the middle
        GridPane.setConstraints(publicKeyButton, 1, 1);
        gridPane.getChildren().add(publicKeyButton);

        //challenge button issues an RSA challenge based on the two given keys when clicked,
        //if the challenge is passed, the user gets logged in
        Button challengeButton = new Button("Login");
        challengeButton.setMinWidth(140);
        challengeButton.setOnMouseClicked(e -> labelLogin.setText(issueChallenge(primaryStage, chain)));
        GridPane.setConstraints(challengeButton, 2, 1);
        gridPane.getChildren().add(challengeButton);

        //button which returns the user to the main screen
        Button backButton = new Button("Return");
        backButton.setOnAction(e -> MainScreen.screen(primaryStage, chain));
        GridPane.setConstraints(backButton, 0, 0);
        gridPane.getChildren().add(backButton);


        //setting the scene with above buttons and labels
        Scene scene = new Scene(gridPane, 500, 100);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //method that prompts the user to select a private key
    private static PrivateKey loadPrivateKey() {
        //opens a window for the user to select a file
        FileChooser fileChooserPrivate = new FileChooser();
        File selectedFilePrivate = fileChooserPrivate.showOpenDialog(null);

        if (selectedFilePrivate != null) {
            //checks if the loaded file contains a private key, if it does, return the private key, else tell the user
            //he did not select a private key file
            if (getPrivateKeyFromFile(selectedFilePrivate.getAbsolutePath()) != null) {
                return getPrivateKeyFromFile(selectedFilePrivate.getAbsolutePath());
            } else labelPrivateKey.setText("File is not a private key");
            return null;
        } else return null;
    }

    //method that prompts the user to select a private key
    private static PublicKey loadPublicKey() {
        //opens a window for the user to select a file
        FileChooser fileChooserPrivate = new FileChooser();
        File selectedFilePublic = fileChooserPrivate.showOpenDialog(null);

        if (selectedFilePublic != null) {
            //checks if the loaded file contains a private key, if it does, return the public key, else tell the user
            //he did not select a public key file
            if (getPublicKeyFromFile(selectedFilePublic.getAbsolutePath()) != null) {
                return getPublicKeyFromFile(selectedFilePublic.getAbsolutePath());
            } else labelPublicKey.setText("File is not a public key");
            return null;
        } else return null;
    }

    //issues an RSA challenge based on the two selected keys
    private static String issueChallenge(Stage primaryStage, Blockchain chain) {

        //first do a check to see if there exists two keys
        if (privateKey == null) {
            return "No private key chosen";
        } else if (publicKey == null) {
            return "No public key chosen";
        }

        //find the index of the block containing the pub key, count number of blocks with the pub key
        int index = -1;
        int numberOfBlocksContainingPubKey = 0;
        //checks every block in the chain, if it contains the public key provided by the user, save the index
        for (Block block : chain) {
            if (block.getIdentityPublicKey().equals(getEncodedPublicKey(publicKey))
                    && (block instanceof GenesisBlock || block instanceof ValidatorBlock)) {
                index = (chain.indexOf(block));
                numberOfBlocksContainingPubKey ++;
            }
        }

        //if index is still -1, no block contains the public key, and therefore cannot log in. Resets keys and labels
        if ((index == -1) || (1 < numberOfBlocksContainingPubKey)) {
            publicKey = null;
            privateKey = null;
            labelPublicKey.setText("");
            labelPrivateKey.setText("");
            return "Keypair is not validator";
        }

        //create cipherblock and build, based on random string
        CipherBlock cipherBlock = new CipherBlock(RandomChallenge.generateRandomChallenge());

        //do operations on block
        cipherBlock.encryptBlock(publicKey);
        cipherBlock.decryptBlock(privateKey);

        //do a check, and see if the challenge is passed by the decrypted text, being the same as the cleartext
        if (cipherBlock.getDecryptedText().equals(cipherBlock.getCleartext())) {
            labelPublicKey.setText("");
            labelPrivateKey.setText("");
            ValidatorScreen.validatorScreen(primaryStage, chain, chain.getBlock(index), privateKey);
            publicKey = null;
            privateKey = null;
        }
        return "";
    }
}


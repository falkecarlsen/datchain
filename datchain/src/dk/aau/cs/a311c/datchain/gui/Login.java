package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.utility.RSA;
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

import static dk.aau.cs.a311c.datchain.utility.RSA.getPrivateKeyFromFile;
import static dk.aau.cs.a311c.datchain.utility.RSA.getPublicKeyFromFile;

public class Login {
    static PrivateKey privateKey;
    static PublicKey publicKey;
    static String labelText = "Choose your key files";

    public static void login(Stage primaryStage, Blockchain chain) {
        //AtomicReference<PublicKey> publicKey = null;
        //AtomicReference<PrivateKey> privateKey = null;

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.CENTER);
        //vBox.setPadding(new Insets(10, 10, 10, 10));


        //labels for guidance
        Label label = new Label(labelText);
        label.setMaxWidth(140);
        label.setAlignment(Pos.CENTER);
        GridPane.setConstraints(label, 1, 0);
        gridPane.getChildren().add(label);

        Label labelPrivateKey = new Label();
        labelPrivateKey.setMaxWidth(140);
        labelPrivateKey.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelPrivateKey, 0, 2);
        gridPane.getChildren().add(labelPrivateKey);

        Label labelPublicKey = new Label();
        labelPublicKey.setMaxWidth(140);
        labelPublicKey.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelPublicKey, 1, 2);
        gridPane.getChildren().add(labelPublicKey);

        Label labelLogin = new Label();
        labelLogin.setMaxWidth(140);
        labelLogin.setAlignment(Pos.CENTER);
        GridPane.setConstraints(labelLogin, 2, 2);
        gridPane.getChildren().add(labelLogin);

        //private key button
        Button privateKeyButton = new Button("Private key");
        privateKeyButton.setMinWidth(140);
        privateKeyButton.setOnAction(e -> {
            privateKey = loadPrivateKey();
            labelPrivateKey.setText("Chosen");
            if (publicKey != null && privateKey != null) {
                labelLogin.setText("Ready to login");
            }
        });
        GridPane.setConstraints(privateKeyButton, 0, 1);
        gridPane.getChildren().add(privateKeyButton);

        //public key button
        Button publicKeyButton = new Button("Public key");
        publicKeyButton.setMinWidth(140);
        publicKeyButton.setOnAction(e -> {
            publicKey = loadPublicKey();
            labelPublicKey.setText("Chosen");
            if (publicKey != null && privateKey != null) {
                labelLogin.setText("Ready to login");
            }
        });
        GridPane.setConstraints(publicKeyButton, 1, 1);
        gridPane.getChildren().add(publicKeyButton);

        //challenge button
        Button challengeButton = new Button("Login");
        challengeButton.setMinWidth(140);
        challengeButton.setOnMouseClicked(e -> labelText = issueChallenge(primaryStage));
        GridPane.setConstraints(challengeButton, 2, 1);
        gridPane.getChildren().add(challengeButton);

        //go back button
        Button backButton = new Button("Return");
        backButton.setOnAction(e -> MainScreen.screen(primaryStage,chain));
        GridPane.setConstraints(backButton,0,0);
        gridPane.getChildren().add(backButton);


        //setting scene
        Scene scene = new Scene(gridPane, 500, 100);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static PrivateKey loadPrivateKey() {
        FileChooser fileChooserPrivate = new FileChooser();
        File selectedFilePrivate = fileChooserPrivate.showOpenDialog(null);

        if (selectedFilePrivate == null) {
            //.setText("No file chosen");
            return null;
        } else return (getPrivateKeyFromFile(selectedFilePrivate.getAbsolutePath()));
    }

    private static PublicKey loadPublicKey() {
        FileChooser fileChooserPrivate = new FileChooser();
        File selectedFilePrivate = fileChooserPrivate.showOpenDialog(null);

        if (selectedFilePrivate == null) {
            //.setText("No file chosen");
            return null;
        } else return (getPublicKeyFromFile(selectedFilePrivate.getAbsolutePath()));
    }

    private static String issueChallenge(Stage primaryStage) {
        byte[][] encryptedText;
        String decryptedText;
        //TODO should conform to new implementation of blockwise encryption/decryption
        /*
        encryptedText = RSA.blockCipherEncrypt("TEST", publicKey);
        decryptedText = RSA.blockCipherDecrypt(encryptedText, privateKey);
        if (decryptedText.equals("TEST")) {
            ValidatorScreen.validatorScreen(primaryStage);
            return "Succes!";
            //todo handle wrong keys
        } else return "Keys do not match";
    */
        return "bullshit"; //placeholder for compiling
    }
}

package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.CitizenBlock;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
import dk.aau.cs.a311c.datchain.cryptography.RSA;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wrapper extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO ALL KEYS START THE SAME
        //create keypairs for testing
        KeyPair genesisKeypair = RSA.keyPairInit();
        PrivateKey genesisPrivateKey = RSA.getPrivateKey(genesisKeypair);
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/gui/genesis/");

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);
        PublicKey validatorPublic01 = RSA.getPublicKey(validatorKeypair01);

        KeyPair validatorKeypair02 = RSA.keyPairInit();
        PrivateKey validatorPrivate02 = RSA.getPrivateKey(validatorKeypair02);
        PublicKey validatorPublic02 = RSA.getPublicKey(validatorKeypair02);

        KeyPair validatorKeypair03 = RSA.keyPairInit();
        PrivateKey validatorPrivate03 = RSA.getPrivateKey(validatorKeypair03);
        PublicKey validatorPublic03 = RSA.getPublicKey(validatorKeypair03);

        KeyPair citizenKeypair01 = RSA.keyPairInit();
        PrivateKey citizenPrivateKey01 = RSA.getPrivateKey(citizenKeypair01);
        PublicKey citizenPublicKey01 = RSA.getPublicKey(citizenKeypair01);

        KeyPair citizenKeypair02 = RSA.keyPairInit();
        PrivateKey citizenPrivateKey02 = RSA.getPrivateKey(citizenKeypair02);
        PublicKey citizenPublicKey02 = RSA.getPublicKey(citizenKeypair02);

        KeyPair citizenKeypair03 = RSA.keyPairInit();
        PrivateKey citizenPrivateKey03 = RSA.getPrivateKey(citizenKeypair03);
        PublicKey citizenPublicKey03 = RSA.getPublicKey(citizenKeypair03);


        GenesisBlock genesis01 = new GenesisBlock("Erik Lauridsen", "19-09-1980", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Kim Larsen", "19-05-1977", RSA.getEncodedPublicKey(validatorKeypair01), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator01, genesis01);

        ValidatorBlock validator02 = new ValidatorBlock("Mette Nielsen", "21-09-1992", RSA.getEncodedPublicKey(validatorKeypair02), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator02, genesis01);

        ValidatorBlock validator03 = new ValidatorBlock("Hans Andersen", "27-09-1953", RSA.getEncodedPublicKey(validatorKeypair03), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator03, genesis01);

        CitizenBlock citizen01 = new CitizenBlock("Natasja Christiansen", "07-09-2000", RSA.getEncodedPublicKey(citizenKeypair01), chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        chain.addValidatedBlock(citizen01, validator01);

        CitizenBlock citizen02 = new CitizenBlock("Sara Petersen", "12-09-1999", RSA.getEncodedPublicKey(citizenKeypair02), citizen01.getHash(), chain.getHead().getHash(), validator02.getIdentityPublicKey(), validatorPrivate02);
        chain.addValidatedBlock(citizen02, validator01);

        CitizenBlock citizen03 = new CitizenBlock("Annie Skriver Købke", "05-09-1986", RSA.getEncodedPublicKey(citizenKeypair03), citizen02.getHash(), chain.getHead().getHash(), validator03.getIdentityPublicKey(), validatorPrivate03);
        chain.addValidatedBlock(citizen03, validator01);

        primaryStage.setTitle("Datchain");
        //opens mainscreen
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("blockchainLogo.png")));
        MainScreen.screen(primaryStage, chain);
    }
}

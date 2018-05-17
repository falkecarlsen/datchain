package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.CitizenBlock;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Wrapper extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        /* TODO needs to conform to privatekey from validator when creating blocks
        //Blockchain for testing purposes
        GenesisBlock genesis01 = new GenesisBlock("Kamilla", "19-09-1980", "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAh6mCcaGkk8dzT+qIF97/cnXGPPFmcMZetpDAP7Ih0Uxf1v+gGOQUAvmMk6ua3upXm72leRmYB7WyBQy5Pp+Tg1bzg90wwk+pspmwRvbW7E0idrgAE7GFJ9Wncdaps+jZHgBVBLWODcRT/jVqRb6Nvwo7TKS8PNOoejAm8m1kN3qwJH185fnuhF1wWRELuxe2OrY20a4QcPYlrTYW3O0AhWN01IyXeefWo3E289jdEHHEMR9082zKfFm2CMCwu45qD04MLq1p/aJzcoryULNs4EAYmWOXwP1foHNqlinV1psG/+Tl9a+2/iJg1IqcXMKE+5wOkAdX0U9VF4LZLaFZVPN5ptG0PB0WUTCxwTPvqt1AD0MDlaEiAj9qkDbJdCMobVSW3eos7fYGnwWn9T7I9AKp0jTq0yEaaPY4RccBoA/6mkl7AmCZQ1k03Q/fcpA69dZH/THNxjJnUHDFrcIQQk9FbtlUNu2FcFnB+w5Yu2wMzFllysENIM+NhGex/yTCXJXjFAR7enoU/U7+4+c+JMaNFYUN/+Cn6kKm7mEVpgHzFLTQx+Q9UmQudh9mzPMm5U4PeoNWD9DxKE/Mo8DAaNWqCfCJqU26RIgFgmcuEwJZQRSGhFqVRcT9w6K6oOIRGdRQxPMVlbeewD1AQNWKjPH4DF2noDaK/hy60yHkGF0CAwEAAQ==", "0000");
        ValidatorBlock validator01 = new ValidatorBlock("Tobias", "19-09-1980", "ValidatorPublicKey", genesis01.getHash(), "GenesisSignature");
        ValidatorBlock validator02 = new ValidatorBlock("Jarl", "19-09-1980", "ValidatorPublicKey", validator01.getHash(), "GenesisSignature");
        ValidatorBlock validator03 = new ValidatorBlock("Hans", "19-09-1980", "ValidatorPublicKey", validator02.getHash(), "GenesisSignature");
        CitizenBlock citizen01 = new CitizenBlock("Fie", "19-09-1980", "CitizenPublicKey", validator03.getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen02 = new CitizenBlock("Christian", "19-09-1980", "CitizenPublicKey", citizen01.getHash(), validator02.getIdentity(), validator02.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen03 = new CitizenBlock("Karl", "19-09-1980", "CitizenPublicKey", citizen02.getHash(), validator03.getIdentity(), validator03.getIdentityPublicKey(), "ValidatorSignature");

        Blockchain chain = new Blockchain(genesis01);
        chain.addValidatedBlock(validator01, validator01);
        chain.addValidatedBlock(validator02, validator01);
        chain.addValidatedBlock(validator03, validator01);
        chain.addValidatedBlock(citizen01, validator01);
        chain.addValidatedBlock(citizen02, validator01);
        chain.addValidatedBlock(citizen03, validator01);

        primaryStage.setTitle("Datchain");
        //opens mainscreen
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("blockchainLogo.png")));
        MainScreen.screen(primaryStage, chain);
        */
    }
}

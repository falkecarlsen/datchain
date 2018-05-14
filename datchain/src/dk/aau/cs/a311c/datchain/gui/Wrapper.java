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

        //Blockchain for testing purposes
        GenesisBlock genesis01 = new GenesisBlock("Kamilla", "19-09-1980", "GenesisPublicKey", "0000");
        ValidatorBlock validator01 = new ValidatorBlock("Tobias", "19-09-1980", "ValidatorPublicKey", genesis01.getHash(), "GenesisSignature");
        ValidatorBlock validator02 = new ValidatorBlock("Jarl", "19-09-1980", "ValidatorPublicKey", validator01.getHash(), "GenesisSignature");
        ValidatorBlock validator03 = new ValidatorBlock("Hans", "19-09-1980", "ValidatorPublicKey", validator02.getHash(), "GenesisSignature");
        CitizenBlock citizen01 = new CitizenBlock("Fie", "19-09-1980", "CitizenPublicKey", validator03.getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen02 = new CitizenBlock("Christian", "19-09-1980", "CitizenPublicKey", citizen01.getHash(), validator02.getIdentity(), validator02.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen03 = new CitizenBlock("Karl", "19-09-1980", "CitizenPublicKey", citizen02.getHash(), validator03.getIdentity(), validator03.getIdentityPublicKey(), "ValidatorSignature");


        Blockchain chain = new Blockchain();

        chain.addValidatedBlock(genesis01, validator01);
        chain.addValidatedBlock(validator01, validator01);
        chain.addValidatedBlock(validator02, validator01);
        chain.addValidatedBlock(validator03, validator01);
        chain.addValidatedBlock(citizen01, validator01);
        chain.addValidatedBlock(citizen02, validator01);
        chain.addValidatedBlock(citizen03, validator01);


        primaryStage.setTitle("Datchain");
        //might need a new logo to not infringe on copyright :shrug
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("blockchainLogo.jpg")));
        MainScreen.screen(primaryStage, chain);
    }
}

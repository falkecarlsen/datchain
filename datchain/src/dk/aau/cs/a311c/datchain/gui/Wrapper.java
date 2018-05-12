package dk.aau.cs.a311c.datchain.gui;
import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.CitizenBlock;
import dk.aau.cs.a311c.datchain.GenesisBlock;
import dk.aau.cs.a311c.datchain.ValidatorBlock;
import javafx.application.Application;
import javafx.stage.Stage;

public class Wrapper extends Application {

    public static void main(String[] args) {launch(args);}


    @Override
    public void start(Stage primaryStage) throws Exception {

        //Blockchain for testing purposes
        GenesisBlock genesis01 = new GenesisBlock("Michael", "GenesisPublicKey", "0000");
        ValidatorBlock validator01 = new ValidatorBlock("Ingrid", "ValidatorPublicKey", genesis01.getHash(), "GenesisSignature");
        ValidatorBlock validator02 = new ValidatorBlock("Susanne", "ValidatorPublicKey", validator01.getHash(), "GenesisSignature");
        ValidatorBlock validator03 = new ValidatorBlock("Sanne", "ValidatorPublicKey", validator02.getHash(), "GenesisSignature");
        CitizenBlock citizen01 = new CitizenBlock("Knud", "CitizenPublicKey", validator03.getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen02 = new CitizenBlock("Fiskarl", "CitizenPublicKey", citizen01.getHash(), validator02.getIdentity(), validator02.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen03 = new CitizenBlock("Benjamin", "CitizenPublicKey", citizen02.getHash(), validator03.getIdentity(), validator03.getIdentityPublicKey(), "ValidatorSignature");

        Blockchain chain = new Blockchain();

        chain.addValidatedBlock(genesis01, validator01);
        chain.addValidatedBlock(validator01, validator01);
        chain.addValidatedBlock(validator02, validator01);
        chain.addValidatedBlock(validator03, validator01);
        chain.addValidatedBlock(citizen01, validator01);
        chain.addValidatedBlock(citizen02, validator01);
        chain.addValidatedBlock(citizen03, validator01);

        primaryStage.setTitle("Datchain");
        MainScreen.screen(primaryStage, chain);
    }
}

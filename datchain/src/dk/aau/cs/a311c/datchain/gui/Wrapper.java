package dk.aau.cs.a311c.datchain.gui;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.utility.StoreChain;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Wrapper extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Blockchain chain;
        primaryStage.setTitle("Datchain");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("blockchainLogo.png")));

        if (new File("data/blockchain.obj").exists()) {
            chain = StoreChain.readChainFromFilesystem("data/");
            MainScreen.screen(primaryStage, chain);
        } else {
            ValidatorScreen.initialStartup(primaryStage);
        }
    }
}

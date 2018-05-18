package dk.aau.cs.a311c.datchain.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.aau.cs.a311c.datchain.Block;
import dk.aau.cs.a311c.datchain.Blockchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StoreChain {

    private static final String chainFilename = "blockchain.obj";

    private static String serialiseChain(Blockchain chain) {
        //build serialised Blockchain with GSON, passing custom TypeAdapter, BlockClassAdapter, to GsonBuilder
        Gson gson = new GsonBuilder().registerTypeAdapter(Block.class, new BlockClassAdapter()).create();
        //return created json string
        return gson.toJson(chain);
    }

    public static boolean writeChainToFilesystem(String directory, Blockchain chain) {
        //get path for given directory and chainFilename specified
        Path path = Paths.get(directory + chainFilename);

        try {
            //call serialiseChain and getBytes from returned string, writing with no options to given path, thus
            //creating, writing and closing if no exception is caught or overwriting with same procedure if object exists
            Files.write(path, serialiseChain(chain).getBytes());
            //return true if no exception has been caught
            return true;
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when writing to " + directory + chainFilename + ". " + e.getMessage());
        }
        //else return false, as something must've gone wrong
        return false;
    }

    private static Blockchain deserialiseChain(String jsonChain) {
        //build deserialised Blockchain with GSON, passing custom TypeAdapter, BlockClassAdapter, to GsonBuilder
        Gson gson = new GsonBuilder().registerTypeAdapter(Block.class, new BlockClassAdapter()).create();
        //create new Blockchain object, utilising no-args constructor for this specific purpose
        Blockchain blockchain = new Blockchain();
        //assign reconstituted Blockchain from serialised json to newly created Blockchain
        blockchain = gson.fromJson(jsonChain, Blockchain.class);
        return blockchain;
    }

    public static Blockchain readChainFromFilesystem(String directory) {
        //get path for given directory and chainFilename specified
        Path path = Paths.get(directory + chainFilename);

        try {
            //read all bytes from path and create string from input,
            //passing to deserialiseChain() and returning created Blockchain
            return deserialiseChain(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            System.out.println("ERROR: Caught IOException when reading from " + directory + chainFilename + ". " + e.getMessage());
        }
        //else return null, as something must've gone wrong
        return null;
    }
}

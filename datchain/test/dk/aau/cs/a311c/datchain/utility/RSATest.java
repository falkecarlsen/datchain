package dk.aau.cs.a311c.datchain.utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RSATest {

    static private final String testDirectiory = "testData/";
    static private KeyPair keyPair;

    @BeforeAll
    static void keyPairInitBefore() {
        //create keypair for testing
        KeyPair keyPair = RSA.keyPairInit();

        //create testDirectory if it doesn't exist
        if (!Files.exists(Paths.get(testDirectiory))) {
            try {
                Files.createDirectory(Paths.get(testDirectiory));
            } catch (IOException e) {
                System.out.println("ERROR: when testing, could create test directory: " + testDirectiory + ". " + e.getMessage());
            }
        }
        //write keys to files in testDirectory
        RSA.keyPairWriter(keyPair, testDirectiory);
    }

    @Test
    void keyPairWriter() {
        //assert that keys have been written
        assertTrue(RSA.keyPairWriter(RSA.keyPairInit(), testDirectiory));
    }

    @Test
    void getPublicKey() {
        //assert that object returned is not null
        PublicKey publicKey = RSA.getPublicKey(RSA.keyPairInit());
        assertNotNull(publicKey);
    }

    @Test
    void getPrivateKey() {
        //assert that object returned is not null
        PrivateKey privateKey = RSA.getPrivateKey(RSA.keyPairInit());
        assertNotNull(privateKey);
    }

    @Test
    void keyPairInit() {
        //assert not null, as some keypair must've been generated
        assertNotNull(RSA.keyPairInit());
    }

    @Test
    void getPrivateKeyFromFile() {
        //assert that object returned is not null
        PrivateKey privateKeyFromFile = RSA.getPrivateKeyFromFile(testDirectiory + "private.key");
        assertNotNull(privateKeyFromFile);
    }

    @Test
    void getPublicKeyFromFile() {
        //assert that object returned is not null
        PublicKey publicKeyFromFile = RSA.getPublicKeyFromFile(testDirectiory + "public.key");
        assertNotNull(publicKeyFromFile);
    }

    @Test
    void keysPresent() {
        //assert that keys generated exists in filesystem
        assertTrue(RSA.keysPresent(testDirectiory));
    }

    @Test
    void encrypt() {
    }

    @Test
    void decrypt() {
    }
}
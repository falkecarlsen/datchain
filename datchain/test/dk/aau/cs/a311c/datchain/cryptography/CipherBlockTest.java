package dk.aau.cs.a311c.datchain.cryptography;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CipherBlockTest {

    @Test
    void encryptDecryptBlock() {
        //setup keys
        KeyPair keypair = RSA.keyPairInit();
        PrivateKey privateKey = RSA.getPrivateKey(keypair);
        PublicKey publicKey = RSA.getPublicKey(keypair);

        //execute RSA-operations
        String randomChallenge = RandomChallenge.generateRandomChallenge();
        CipherBlock cipherBlock = new CipherBlock(randomChallenge);
        cipherBlock.encryptBlock(publicKey);
        cipherBlock.decryptBlock(privateKey);

        //assert that received cleartext is actually cleartext
        assertEquals(randomChallenge, cipherBlock.getCleartext());
        //assert that encryption and decryption succeeded
        assertEquals(cipherBlock.getCleartext(), cipherBlock.getDecryptedText());
    }

    @Test
    void signVerifyBlock() {
        //setup keys
        KeyPair keypair = RSA.keyPairInit();
        PrivateKey privateKey = RSA.getPrivateKey(keypair);
        PublicKey publicKey = RSA.getPublicKey(keypair);

        //execute RSA-operations
        String randomChallenge = RandomChallenge.generateRandomChallenge();
        CipherBlock cipherBlock = new CipherBlock(randomChallenge);
        cipherBlock.signBlock(privateKey);

        //assert that block was signed by private key belonging to this public key
        assertTrue(cipherBlock.verifyBlock(publicKey));
    }
}
package dk.aau.cs.a311c.datchain.utility;

import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RSATest {

    private String testDirectiory = "data/";

    @Test
    void keyPairInit() {
        //assert not null, as some keypair must've been generated
        assertNotNull(RSA.keyPairInit());
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
    void encryptDecrypt() {
        KeyPair keyPair = RSA.keyPairInit();

        String test = "teststringisverytestyandhasweirdchars¡$}ªßđ@$£ŋþĸðßßðđĸ€þnªŋ";

        byte[] encryptedTest = RSA.encrypt(test.getBytes(), RSA.getPublicKey(keyPair));

        //assert encryption succeeded
        assertNotNull(encryptedTest);

        byte[] decryptedTest = RSA.decrypt(encryptedTest, RSA.getPrivateKey(keyPair));

        //trim string to avoid padding issues and assert equal with input
        assertEquals(test, new String(decryptedTest).trim());
    }

    @Test
    void getEncodedPrivateKey() {
        KeyPair keyPair = RSA.keyPairInit();
        //assert that keypair has been generated
        assertNotNull(keyPair);

        //assert that some string has been returned
        assertNotNull(RSA.getEncodedPrivateKey(keyPair));
    }

    @Test
    void getPrivateKeyFromEncoded() {
        KeyPair keyPair = RSA.keyPairInit();
        //assert that keypair has been generated
        assertNotNull(keyPair);

        String encodedPrivateKey = RSA.getEncodedPrivateKey(keyPair);

        assertNotNull(RSA.getPrivateKeyFromEncoded(encodedPrivateKey));
    }

    @Test
    void getEncodedPublicKey() {
        KeyPair keyPair = RSA.keyPairInit();
        //assert that keypair has been generated
        assertNotNull(keyPair);

        //assert that some string has been returned
        assertNotNull(RSA.getEncodedPublicKey(keyPair));
    }

    @Test
    void getPublicKeyFromEncoded() {
        KeyPair keyPair = RSA.keyPairInit();
        //assert that keypair has been generated
        assertNotNull(keyPair);

        String encodedPublicKey = RSA.getEncodedPublicKey(keyPair);

        assertNotNull(RSA.getPublicKeyFromEncoded(encodedPublicKey));
    }
}

package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    void testBlockDifferent() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock block01 = new CitizenBlock("Validator1", "19-09-1999", "ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        CitizenBlock block02 = new CitizenBlock("Validator2", "19-09-1980", "ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        assertFalse(block01.equals(block02));
    }

    @Test
    void testTimestampConstructor() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock block01 = new CitizenBlock("Validator", "19-09-1980", "ValidatorPubkey", "Citizen Name", "CitizenPubKey", "0000", validatorPrivate01);
        assertEquals(now().getEpochSecond(), block01.getTimestamp());
    }

    @Test
    void testNameCongruency() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1", "19-09-1980", "CitizenPublicKey", "0", "0", "0", validatorPrivate01);
        assertTrue(citizen01.getIdentity().equals("Citizen Name1"));
    }

    @Test
    void equalsTest() {
        KeyPair genesisKeypair = RSA.keyPairInit();
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);

        //create testblocks with differing fields
        GenesisBlock genesis01 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis02 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis03 = new GenesisBlock("Genesis  Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis04 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0001");

        //assert that blocks are equal or not equal
        assertTrue(genesis01.equals(genesis02));
        assertFalse(genesis01.equals(genesis03));
        assertFalse(genesis01.equals(genesis04));
    }

    @Test
    void hashCodeTest() {
        KeyPair genesisKeypair = RSA.keyPairInit();
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);

        //create testblocks with differing fields
        GenesisBlock genesis01 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis02 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis03 = new GenesisBlock("Genesis  Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        GenesisBlock genesis04 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0001");

        //assert that blocks are equal or not equal
        assertEquals(genesis01.hashCode(), genesis02.hashCode());
        assertNotEquals(genesis01.hashCode(), genesis03.hashCode());
        assertNotEquals(genesis01.hashCode(), genesis04.hashCode());
    }
}
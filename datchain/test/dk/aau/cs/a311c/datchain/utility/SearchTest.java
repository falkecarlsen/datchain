package dk.aau.cs.a311c.datchain.utility;

import dk.aau.cs.a311c.datchain.*;
import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchTest {

    @Test
    void fuzzySearchIdentityTest() {
        Blockchain chain = SetupChain.getDefaultChain();
        Search search = new Search();
        assertNotNull(search);

        //assert that first hit is "Mike Johnson"
        assertEquals("Mike Johnson", search.FuzzySearchIdentity("Mike", chain, 1).get(0).getIdentity());
    }

    @Test
    void fuzzySearchIdentityPublicKey() {
        Search search = new Search();

        //create keypairs for testing
        KeyPair genesisKeypair = RSA.keyPairInit();
        PrivateKey genesisPrivateKey = RSA.getPrivateKey(genesisKeypair);
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/");

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);
        PublicKey validatorPublic01 = RSA.getPublicKey(validatorKeypair01);

        GenesisBlock genesis01 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Mike Johnson", "30-05-1944", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator01, genesis01);

        //assert that when exact key is searched for, it's the top hit
        assertEquals(validator01.getIdentityPublicKey(), search.FuzzySearchIdentityPublicKey(validator01.getIdentityPublicKey(), chain, 1).get(0).getIdentityPublicKey());
    }

    @Test
    void fuzzySearchIdentityDOB() {
        Blockchain chain = SetupChain.getDefaultChain();
        Search search = new Search();

        //assert that first hit is "Mike Johnson"
        assertEquals("29-05-2018", search.FuzzySearchIdentityDOB("2018", chain, 1).get(0).getIdentityDOB());
    }
}
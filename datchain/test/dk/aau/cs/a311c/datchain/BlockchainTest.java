package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockchainTest {

    @Test
    void addValidatedBlock() {
        //setup elements for chain and assert true for add validator block
        KeyPair genesisKeypair = RSA.keyPairInit();
        PrivateKey genesisPrivateKey = RSA.getPrivateKey(genesisKeypair);
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/");

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PublicKey validatorPublic01 = RSA.getPublicKey(validatorKeypair01);

        GenesisBlock genesis01 = new GenesisBlock("Genesis", "19-09-1980", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Validator1", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        assertTrue(chain.addValidatedBlock(validator01, genesis01));
    }

    @Test
    void testDuplicate() {
        //assert true for expected and assert false for adding revoked block twice
        KeyPair genesisKeypair = RSA.keyPairInit();
        PrivateKey genesisPrivateKey = RSA.getPrivateKey(genesisKeypair);
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/");

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PublicKey validatorPublic01 = RSA.getPublicKey(validatorKeypair01);

        GenesisBlock genesis01 = new GenesisBlock("Genesis", "19-09-1980", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Validator1", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        assertTrue(chain.addValidatedBlock(validator01, genesis01));

        ValidatorBlock validator02 = new ValidatorBlock("Revoked", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        assertTrue(chain.addValidatedBlock(validator02, genesis01));

        ValidatorBlock validator03 = new ValidatorBlock("Revoked", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        assertFalse(chain.addValidatedBlock(validator03, genesis01));

    }

    @Test
    void validateChain() {
        Blockchain chain = SetupChain.getDefaultChain();

        //assert that chain is valid
        assertTrue(chain.validateChain());
    }

}
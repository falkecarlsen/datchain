package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

    @Test
    void addValidatedBlock() {
    }

    @Test
    void add() {
        KeyPair genesisKeypair01 = RSA.keyPairInit();

        GenesisBlock genesisBlock = new GenesisBlock("Genesis", "19-09-1980", RSA.getEncodedPublicKey(genesisKeypair01), "45678909876545678");
        Blockchain chain = new Blockchain(genesisBlock);

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);
        chain.add(new ValidatorBlock("validator", "19-09-1980", RSA.getEncodedPublicKey(validatorKeypair01), chain.getHead().getHash(), RSA.getPrivateKey(genesisKeypair01)));

        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        CitizenBlock block02 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        CitizenBlock block03 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);

        assertTrue(chain.add(block01));
        assertTrue(chain.add(block02));
        assertTrue(chain.add(block03));
    }

    @Test
    void validateChain() {
        KeyPair genesisKeypair01 = RSA.keyPairInit();

        GenesisBlock genesisBlock = new GenesisBlock("Genesis", "19-09-1980", RSA.getEncodedPublicKey(genesisKeypair01), "0000");
        Blockchain chain = new Blockchain(genesisBlock);

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);
        ValidatorBlock validator01 = new ValidatorBlock("validator", "19-09-1980", RSA.getEncodedPublicKey(validatorKeypair01), chain.getHead().getHash(), RSA.getPrivateKey(genesisKeypair01));
        assertTrue(chain.add(validator01));

        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1","19-09-1980", "CitizenPublicKey", chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        assertTrue(chain.add(citizen01));

        CitizenBlock citizen02 = new CitizenBlock("Citizen Name2","19-09-1980", "CitizenPublicKey", chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        assertTrue(chain.add(citizen02));

        CitizenBlock citizen03 = new CitizenBlock("Citizen Name3","19-09-1980", "CitizenPublicKey", chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        assertTrue(chain.add(citizen03));


        assertTrue(chain.validateChain());
    }

    @Test
    void searchSingleIdentity() {
    }

    @Test
    void searchSinglePublicKey() {
    }

    @Test
    void getHead() {

    }

    @Test
    void getBlock() {

    }

    @Test
    void getChain() {
    }
}
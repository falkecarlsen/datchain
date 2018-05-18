package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

class DatchainTest  {

    @Test
    void testBlockDifferent() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        CitizenBlock block02 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        assertFalse(block01.equals(block02));
    }

    @Test
    void testTimestampConstructor()  {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "0000", validatorPrivate01);
        assertEquals(now().getEpochSecond(),block01.getTimestamp());
    }

    @Test
    void testNameCongruency() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1", "19-09-1980","CitizenPublicKey", "0", "0", "0", validatorPrivate01);
        assertTrue(citizen01.getIdentity().equals("Citizen Name1"));

        //TODO Must also test validator info
    }

    @Test
    void testHashCongruency() {
        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);

        //naive by testing two identical objects, could be done more advanced
        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        CitizenBlock block02 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", validatorPrivate01);
        assertEquals(block01.getHash(),block02.getHash());
    }
    //TODO test validator and citizen RSA-congruency
}
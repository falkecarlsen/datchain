package dk.aau.cs.a311c.datchain;

import org.junit.jupiter.api.Test;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

class DatchainTest  {

    @Test
    void testBlockDifferent() {
        //mostly learning, but might be useful
        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", "ValidatorSignature");
        CitizenBlock block02 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", "ValidatorSignature");
        assertFalse(block01.equals(block02));
    }

    @Test
    void testBlockEqual() {
        //mostly learning, but might be useful
        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", "ValidatorSignature");
        assertEquals(block01, block01);
    }

    @Test
    void testTimestampConstructor()  {
        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "0000", "ValidatorSignature");
        assertEquals(now().getEpochSecond(),block01.getTimestamp());
    }

    @Test
    void testNameCongruency() {
        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1", "19-09-1980","CitizenPublicKey", "0", "0", "0", "ValidatorSignature");
        assertTrue(citizen01.getIdentity().equals("Citizen Name1"));

        //TODO Must also test validator info
    }

    @Test
    void testHashCongruency() {
        //naive by testing two identical objects, could be done more advanced
        CitizenBlock block01 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", "ValidatorSignature");
        CitizenBlock block02 = new CitizenBlock("Validator","19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "9817324939382", "ValidatorSignature");
        assertEquals(block01.getHash(),block02.getHash());
    }
    //TODO test validator and citizen RSA-congruency
}
package dk.aau.cs.a311c.datchain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorBlockTest {

    @Test
    void testGetGenesisSignature() {
        ValidatorBlock validator01 = new ValidatorBlock("Validator", "19-09-1980","ValidatorPublicKey", "0000", "GenesisSignature");
        assertEquals("GenesisSignature", validator01.getGenesisSignature());
    }

}
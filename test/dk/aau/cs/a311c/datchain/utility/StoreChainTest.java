package dk.aau.cs.a311c.datchain.utility;

import dk.aau.cs.a311c.datchain.Blockchain;
import dk.aau.cs.a311c.datchain.SetupChain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreChainTest {

    @Test
    void storeChainTest() {
        //create a default chain of size 7
        Blockchain chain = SetupChain.getDefaultChain();

        //assert that chain is written
        assertTrue(StoreChain.writeChainToFilesystem("data/", chain));

        //read stored chain into reconstitutedChain
        Blockchain reconstitutedChain = StoreChain.readChainFromFilesystem("data/");

        //assert that chain is equal to reconstitutedChain chain
        assertEquals(chain, reconstitutedChain);
    }
}
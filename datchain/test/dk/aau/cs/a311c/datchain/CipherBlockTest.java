package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static dk.aau.cs.a311c.datchain.cryptography.CipherBlock.issueChallenge;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CipherBlockTest {

    @Test
    void testIssueChallenge() {
        KeyPair keypair = RSA.keyPairInit();
        PrivateKey privateKey = RSA.getPrivateKey(keypair);
        PublicKey publicKey = RSA.getPublicKey(keypair);

        assertTrue(issueChallenge(privateKey, publicKey));
    }
}

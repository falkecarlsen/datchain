package dk.aau.cs.a311c.datchain;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;

class CitizenBlockTest {

    @Test
    void computeHash() {
        CitizenBlock block01 = new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", "9817324939382", "Validator", "ValidatorPubKey", "ValidatorSignature");
        String hashInput = "Citizen Name" + "CitizenPubKey" + "9817324939382" + "Validator" + "ValidatorPubKey" + block01.getTimestamp();

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;

        //get hashInput as bytes in UTF-8 and update messageDigest
        messageDigest.update(hashInput.getBytes(StandardCharsets.UTF_8));
        //create digest byte-array with resulting hash and encode digest as hex-string
        String hashOutput = String.format("%064x", new BigInteger(1, messageDigest.digest()));

        assertEquals(hashOutput, block01.getHash());
    }

    @Test
    void getHash() {
        CitizenBlock block01 = new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", "9817324939382", "Validator", "ValidatorPubKey", "ValidatorSignature");
        String hashInput = "Citizen Name" + "CitizenPubKey" + "9817324939382" + "Validator" + "ValidatorPubKey" + block01.getTimestamp();

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;

        //get hashInput as bytes in UTF-8 and update messageDigest
        messageDigest.update(hashInput.getBytes(StandardCharsets.UTF_8));
        //create digest byte-array with resulting hash and encode digest as hex-string
        String hashOutput = String.format("%064x", new BigInteger(1, messageDigest.digest()));

        assertEquals(hashOutput, block01.getHash());
    }

    @Test
    void getPrevHash() {
        CitizenBlock block01 = new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", "9817324939382", "Validator", "ValidatorPubKey", "ValidatorSignature");
        CitizenBlock block02= new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", block01.getHash(), "Validator", "ValidatorPubKey", "ValidatorSignature");

        assertEquals(block01.getHash(), block02.getPrevHash());
    }

    @Test
    void getTimestamp() {
        CitizenBlock block01 = new CitizenBlock("Validator", "19-09-1980","ValidatorPubkey", "Citizen Name", "CitizenPubKey", "0000000000", "ValidatorSignature");
        assertEquals(now().getEpochSecond(), block01.getTimestamp());
    }

    @Test
    void getPubKey() {
        CitizenBlock block01 = new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", "9817324939382", "Validator", "ValidatorPubKey", "ValidatorSignature");
        assertEquals("CitizenPubKey", block01.getIdentityPublicKey());
    }

    @Test
    void getIdentity() {
        CitizenBlock block01 = new CitizenBlock("Citizen Name", "19-09-1980","CitizenPubKey", "9817324939382", "Validator", "ValidatorPubKey", "ValidatorSignature");
        assertEquals("Citizen Name", block01.getIdentity());
    }
    
}

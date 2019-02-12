package dk.aau.cs.a311c.datchain.cryptography;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

    public static String computeHash(String hashInput) {
        //initialise MessageDigest
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support cryptographic hash algorithm: " + e.getMessage());
        }
        assert messageDigest != null;

        //get hashInput as bytes and update messageDigest
        messageDigest.update(hashInput.getBytes());
        //create digest byte-array with resulting hash and encode digest as hex-string
        return String.format("%064x", new BigInteger(1, messageDigest.digest()));
    }
}

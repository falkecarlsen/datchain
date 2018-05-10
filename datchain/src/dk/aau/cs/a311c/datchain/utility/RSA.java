package dk.aau.cs.a311c.datchain.utility;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

import static dk.aau.cs.a311c.datchain.utility.RSA.Constants.*;

public class RSA {

    class Constants {

        static final String RSAALGORITHM = "RSA";
        //choose RSA-variant with padding for encryption and decryption to disallow zero-char attacks
        static final String CRYPTALGORITHM = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding";
        static final int KEYBITLENGTH = 4096;
        static final String KEYLOCATION = "data/";
        static final String CITIZENLOCATION = "citizen/";
        static final String PRIVATE_KEY_FILE = "private.key";
        static final String PUBLIC_KEY_FILE = "public.key";
    }

    static public boolean keyPairWriter() {
        try {
            KeyPair keyPair = keyPairInit();
            //if KEYLOCATION doesn't exist, create the directory
            Path location = Paths.get(KEYLOCATION);
            if (!Files.exists(location)) Files.createDirectory(Paths.get(KEYLOCATION));

            //get key bytes and encode them to base64 strings for storage
            String encodedPrivateKey = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
            String encodedPublicKey = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));

            //create keyfile paths at KEYLOCATION
            Path privatekeyFile = Paths.get(KEYLOCATION + PRIVATE_KEY_FILE);
            Path publickeyFile = Paths.get(KEYLOCATION + PUBLIC_KEY_FILE);

            //write strings to file in UTF-8 and return true
            Files.write(privatekeyFile, encodedPrivateKey.getBytes("UTF-8"));
            Files.write(publickeyFile, encodedPublicKey.getBytes("UTF-8"));
            return true;

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: Unsupported encoding! (UTF-8) " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR: IOException caught! " + e.getMessage());
        }
        return false;
    }

    static private KeyPair keyPairInit() {

        try {
            //initialize and generate keys from constants
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSAALGORITHM);
            keyPairGenerator.initialize(KEYBITLENGTH);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("ERROR: System does not support RSA bitlength of " + KEYBITLENGTH + ". " + e.getMessage());
        } catch (InvalidPathException e) {
            System.out.println("ERROR: RSA key location cannot be resolved! Reason: " + e.getReason());
        } catch (SecurityException e) {
            System.out.println("ERROR: Program does not have permissions to write here! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Unknown exception: " + e.getMessage());
        }
        //return null, if something went wrong and exceptions hasn't been caught
        return null;
    }

    public static boolean keysPresent() {
        try {
            Path privateKey = Paths.get(KEYLOCATION + PRIVATE_KEY_FILE);
            Path publicKey = Paths.get(KEYLOCATION + PUBLIC_KEY_FILE);
            return Files.exists(privateKey) && Files.exists(publicKey);
        } catch (InvalidPathException e) {
            System.out.println("ERROR: Cannot get keys from: " + KEYLOCATION + ". " + e.getMessage());
            return false;
        }
    }

    public static byte[] encrypt(String cleartext, PublicKey key) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(CRYPTALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(cleartext.getBytes("Unicode"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: System does not have support for unicode encoding! " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not have support for RSA-cryptography! " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: Invalid key supplied! " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("ERROR: Invalid block size of bytes received! " + e.getMessage());
            //catch remaining padding exceptions from Cipher.getInstance
        } catch (Exception e) {
            System.out.println("ERROR: Padding error in initializing instance! " + e.getMessage());
        }
        return cipherText;
    }

    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedtext = null;
        try {
            final Cipher cipher = Cipher.getInstance(CRYPTALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedtext = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //poor null-handling, avoid
        assert decryptedtext != null;

        return new String(decryptedtext);
    }
}

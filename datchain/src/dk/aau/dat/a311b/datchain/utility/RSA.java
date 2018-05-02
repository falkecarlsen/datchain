package dk.aau.dat.a311b.datchain.utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import static dk.aau.dat.a311b.datchain.utility.RSA.Constants.*;

public class RSA {

    class Constants {
        static final String ALGORITHM = "RSA";
        static final int KEYBITLENGTH = 4096;
        static final String KEYLOCATION = "data";
        static final String PRIVATE_KEY_FILE = "private.key";
        static final String PUBLIC_KEY_FILE = "public.key";
    }

    static public boolean keyGenerator() {

            try {
                //initialize and generate keys from constants
                final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
                keyGen.initialize(KEYBITLENGTH);
                final KeyPair key = keyGen.generateKeyPair();

                //if KEYLOCATION doesn't exist, create the directory
                Path location = Paths.get(KEYLOCATION);
                if (!Files.exists(location)) Files.createDirectory(Paths.get(KEYLOCATION));

                //create keyfiles at KEYLOCATION
                Path privatekeyFile = Paths.get(KEYLOCATION + "/" + PRIVATE_KEY_FILE);
                Path publickeyFile = Paths.get(KEYLOCATION + "/" + PUBLIC_KEY_FILE);

                //get keys from generator
                PrivateKey privateKey = key.getPrivate();
                PublicKey publicKey = key.getPublic();

                //get key bytes and encode them to base64 strings for storage
                String encodedPrivateKey = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
                String encodedPublicKey = new String(Base64.getEncoder().encode(publicKey.getEncoded()));

                //write strings to file in UTF-8
                Files.write(privatekeyFile, encodedPrivateKey.getBytes("UTF-8"));
                Files.write(publickeyFile, encodedPublicKey.getBytes("UTF-8"));

                //TODO needs destroy of keygen states
            } catch (NoSuchAlgorithmException e) {
                System.out.println("ERROR: System does not support RSA generation! " + e.getMessage());
            } catch (InvalidParameterException e) {
                System.out.println("ERROR: System does not support RSA bitlength of " + KEYBITLENGTH + ". " + e.getMessage());
            } catch (IOException e) {
                System.out.println("ERROR: IO exception caught: " + e.getMessage());
            } catch (InvalidPathException e) {
                System.out.println("ERROR: RSA key location cannot be resolved! Reason: " + e.getReason());
            } catch (SecurityException e) {
                System.out.println("ERROR: Program does not have permissions to write here! " + e.getMessage());
            }
            return true;
    }

    //TODO
    public static boolean keysPresent() {
        return true;
    }

    public static byte[] encrypt(String cleartext, PublicKey key) {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
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
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
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

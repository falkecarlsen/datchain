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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    //declaring constants
    static final String RSAALGORITHM = "RSA";
    //choose RSA-variant with padding for encryption and decryption to disallow zero-char attacks
    static final String CRYPTALGORITHM = "RSA/ECB/OAEPWithSHA-512AndMGF1Padding";
    static final int KEYBITLENGTH = 4096;
    //static final String KEYLOCATION = "data/";
    static final String CITIZENLOCATION = "citizen/";
    static final String PRIVATE_KEY_FILE = "private.key";
    static final String PUBLIC_KEY_FILE = "public.key";

    static public boolean keyPairWriter(KeyPair keyPair, String directory) {
        try {
            //if keyLocation doesn't exist, create the directory
            if (!Files.exists(Paths.get(directory))) Files.createDirectory(Paths.get(directory));

            //get key bytes and encode them to base64 strings for storage
            String encodedPrivateKey = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
            String encodedPublicKey = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));

            //create keyfile paths at KEYLOCATION
            Path privatekeyFile = Paths.get(directory + PRIVATE_KEY_FILE);
            Path publickeyFile = Paths.get(directory + PUBLIC_KEY_FILE);

            //write strings to file in UTF-8 encoding and return true
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

    static public PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    static public PrivateKey getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    static public KeyPair keyPairInit() {
        try {
            //initialize and generate keys from constants, allowing SecureRandom implementation to be chosen at runtime
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSAALGORITHM);
            keyPairGenerator.initialize(KEYBITLENGTH);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("ERROR: System does not support RSA bitlength of " + KEYBITLENGTH + ". " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Unknown exception: " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    public static PrivateKey getPrivateKeyFromFile(String filename) {
        try {
            //get path to filename supplied
            Path filePath = Paths.get(filename);

            //read all bytes from file, stored in unicode, and decode bytes to raw encoding
            byte[] keyByteArray = Base64.getDecoder().decode(Files.readAllBytes(filePath));

            //initialise keyfactory with cryptographic algorithm used
            KeyFactory keyFactory = KeyFactory.getInstance(RSAALGORITHM);
            //return regenerated privatekey-spec as privatekey object
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyByteArray));

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: Unsupported encoding exception! Cannot get bytes from file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught! " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("ERROR: Invalid key specification, cannot get private key specification! " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    public static PublicKey getPublicKeyFromFile(String filename) {
        try {
            //get path to filename supplied
            Path filePath = Paths.get(filename);

            //read all bytes from file, stored in unicode, and decode bytes to raw encoding
            byte[] keyByteArray = Base64.getDecoder().decode(Files.readAllBytes(filePath));

            //initialise keyfactory with cryptographic algorithm used
            KeyFactory keyFactory = KeyFactory.getInstance(RSAALGORITHM);
            //return regenerated publickey-spec as publickey object
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyByteArray));

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: Unsupported encoding exception! Cannot get bytes from file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught! " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("ERROR: Invalid key specification, cannot get private key specification! " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    public static boolean keysPresent(String directory) {
        try {
            Path privateKey = Paths.get(directory + PRIVATE_KEY_FILE);
            Path publicKey = Paths.get(directory + PUBLIC_KEY_FILE);
            return Files.exists(privateKey) && Files.exists(publicKey);
        } catch (InvalidPathException e) {
            System.out.println("ERROR: Cannot get keys from: " + directory + ". " + e.getMessage());
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

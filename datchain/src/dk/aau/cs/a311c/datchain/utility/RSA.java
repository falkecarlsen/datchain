package dk.aau.cs.a311c.datchain.utility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import java.util.Arrays;
import java.util.Base64;

public class RSA {

    //declaring constants
    private static final String keyAlgorithm = "RSA";
    //choose RSA-variant with padding for encryption and decryption to discourage zero-char attacks
    private static final String cryptoAlgorithm = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    //choosing 2^8 as blocklimit for playing along with filesystem preferences
    private static final int blockLimit = 256;
    private static final int keyBitlength = 4096;
    private static final String privateKeyFilename = "private.key";
    private static final String publicKeyFilename = "public.key";

    static public KeyPair keyPairInit() {
        try {
            //initialize and generate keys from constants, allowing SecureRandom implementation to be chosen at runtime
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
            keyPairGenerator.initialize(keyBitlength);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("ERROR: System does not support RSA bitlength of " + keyBitlength + ". " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Unknown exception: " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    static public byte[][] blockCipherEncrypt(String cleartext, Key key) {
        //divides length of cleartext with limit and adds one, as int division floors, ensuring adequate block size
        int numBlocks = (cleartext.getBytes().length / blockLimit) + 1;

        //initialise bytearray for holding cleartext
        byte[] sourceBytes;
        //get bytes from cleartext for splitting up in blocks and encrypting
        sourceBytes = cleartext.getBytes();

        //create two-dimensional bytearray for holding ciphertext chunks of numBlocks times blockLimit
        byte[][] ciphertextBytes = new byte[numBlocks][blockLimit];

        //create counter for keeping track of current block of text
        int startIndex = 0;
        //for number of blocks needed to hold ciphertext, do
        for (int i = 0; i < numBlocks; i++) {
            //copy bytes from startIndex blockLimit bytes forward and pass resulting bytes through encrypt method
            ciphertextBytes[i] = encrypt(Arrays.copyOfRange(sourceBytes, startIndex, startIndex + blockLimit), key);
            //move startIndex ahead blockLimit for next pass
            startIndex += blockLimit;
        }
        return ciphertextBytes;
    }

    static public String blockCipherDecrypt(byte[][] ciphertextBytes, Key key) {
        //initialise StringBuilder for appending each block of cleartext
        StringBuilder cleartext = new StringBuilder();

        //for each ciphertextBlock in array of ciphertextBytes, do
        for (byte[] ciphertextBlock : ciphertextBytes) {
            //call decrypt method with ciphertextBlock and supplied key and append resulting String to StringBuilder
            cleartext.append(decrypt(ciphertextBlock, key));
        }
        //return string trimmed of padding
        return cleartext.toString().trim();
    }

    static public boolean keyPairWriter(KeyPair keyPair, String directory) {
        try {
            //if keyLocation doesn't exist, create the directory
            if (!Files.exists(Paths.get(directory))) Files.createDirectory(Paths.get(directory));

            //get key bytes and encode them to base64 strings for storage
            String encodedPrivateKey = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
            String encodedPublicKey = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));

            //create keyfile paths at KEYLOCATION
            Path privatekeyFile = Paths.get(directory + privateKeyFilename);
            Path publickeyFile = Paths.get(directory + publicKeyFilename);

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

    public static PrivateKey getPrivateKeyFromFile(String filename) {
        try {
            //get path to filename supplied
            Path filePath = Paths.get(filename);

            //read all bytes from file, stored in unicode, and decode bytes to raw encoding
            byte[] keyByteArray = Base64.getDecoder().decode(Files.readAllBytes(filePath));

            //initialise keyfactory with cryptographic algorithm used
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
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
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
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
            Path privateKey = Paths.get(directory + privateKeyFilename);
            Path publicKey = Paths.get(directory + publicKeyFilename);
            return Files.exists(privateKey) && Files.exists(publicKey);
        } catch (InvalidPathException e) {
            System.out.println("ERROR: Cannot get keys from: " + directory + ". " + e.getMessage());
            return false;
        }
    }

    public static byte[] encrypt(byte[] cleartext, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(cleartext);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not have support for RSA-cryptography of this type! " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: Invalid key supplied! " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("ERROR: Invalid block size of bytes received! " + e.getMessage());
            //catch remaining padding exceptions from Cipher.getInstance
        } catch (Exception e) {
            System.out.println("ERROR: Padding error in initializing instance! " + e.getMessage());
        }
        return null;
    }

    public static String decrypt(byte[] ciphertext, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(ciphertext));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: could not get + " + cryptoAlgorithm + " for decrypting! " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: key does not match expected values! " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println("ERROR: Padding specified does not exist! " + e.getMessage());
        } catch (BadPaddingException e) {
            System.out.println("ERROR: Padding has encountered an error! " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("ERROR: Block size is different than expected! " + e.getMessage());
        }
        return null;
    }
}

package dk.aau.cs.a311c.datchain.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
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
    private static final String keyAlgorithm = "RSA";
    //choose RSA-variant with padding for encryption and decryption to discourage zero-char attacks
    private static final String cryptAlgorithm = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String signatureAlgorithm = "SHA512withRSA";
    private static final int bitlengthKey = 4096;
    private static final String privateKeyFilename = "private.key";
    private static final String publicKeyFilename = "public.key";

    static public KeyPair keyPairInit() {
        try {
            //initialize and generate keys from constants, allowing SecureRandom implementation to be chosen at runtime
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
            keyPairGenerator.initialize(bitlengthKey);
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("ERROR: System does not support RSA bitlength of " + bitlengthKey + ". " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Unknown exception: " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
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

    static public boolean publicKeyWriter(String publicKey, File directory) {
        try {
            //create keyfile paths at KEYLOCATION
            Path publickeyFile = Paths.get(directory + "public.key");

            //write strings to file in UTF-8 encoding and return true
            Files.write(publickeyFile, publicKey.getBytes("UTF-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    public static PublicKey getPublicKeyFromFile(String filename) {
        try {
            //get path to filename supplied
            Path filePath = Paths.get(filename);

            //read all bytes from file, stored in unicode, and decode bytes to raw encoding
            return generatePublicKey(Base64.getDecoder().decode(Files.readAllBytes(filePath)));

        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: Unsupported encoding exception! Cannot get bytes from file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught! " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    private static PublicKey generatePublicKey(byte[] keyByteArray) {
        try {
            //initialise keyfactory with cryptographic algorithm used
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            //return regenerated publickey-spec as publickey object
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyByteArray));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("ERROR: Invalid key specification, cannot get private key specification! " + e.getMessage());
        }
        //if exceptions hasn't been caught
        return null;
    }

    public static PrivateKey getPrivateKeyFromFile(String filename) {
        try {
            //get path to filename supplied
            Path filePath = Paths.get(filename);

            //pass decoded bytes to generatePrivateKey and return generated private key
            return generatePrivateKey(Base64.getDecoder().decode(Files.readAllBytes(filePath)));
        } catch (UnsupportedEncodingException e) {
            System.out.println("ERROR: Unsupported encoding exception! Cannot get bytes from file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR: IO exception caught! " + e.getMessage());
        }
        return null;
    }

    private static PrivateKey generatePrivateKey(byte[] keyByteArray) {
        try {
            //initialise keyfactory with cryptographic algorithm used
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            //return regenerated privatekey-spec as privatekey object
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyByteArray));

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not support RSA generation with: " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("ERROR: Invalid key specification, cannot get private key specification! " + e.getMessage());
        }
        return null;
    }

    public static String getEncodedPrivateKey(KeyPair keyPair) {
        return new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
    }

    public static String getEncodedPrivateKey(PrivateKey privateKey) {
        return new String(Base64.getEncoder().encode(privateKey.getEncoded()));
    }

    public static PrivateKey getPrivateKeyFromEncoded(String encodedKey) {
        return generatePrivateKey(Base64.getDecoder().decode(encodedKey));
    }

    public static String getEncodedPublicKey(KeyPair keyPair) {
        return new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));
    }

    public static String getEncodedPublicKey(PublicKey publicKey) {
        return new String(Base64.getEncoder().encode(publicKey.getEncoded()));
    }

    public static PublicKey getPublicKeyFromEncoded(String encodedKey) {
        return generatePublicKey(Base64.getDecoder().decode(encodedKey));
    }

    static byte[] encrypt(byte[] cleartext, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance(cryptAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(cleartext);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not have support for RSA-cryptography of this type! " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: Invalid key supplied! " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("ERROR: Invalid block size of bytes received! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: Encryption encountered an error! " + e.getMessage());
        }
        return null;
    }

    public static byte[] sign(String cleartext, PrivateKey privateKey) {
        try {
            //get instance of Signature with given constant; signatureAlgorithm
            final Signature signature = Signature.getInstance(signatureAlgorithm);
            //initialise signature with private key
            signature.initSign(privateKey);
            //pass cleartext as bytes to signature and update
            signature.update(cleartext.getBytes());
            //return base64 encoded bytearray of signature
            return Base64.getEncoder().encode(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: Invalid key supplied! " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("ERROR: Some error has occurred when verifying signature! " + e.getMessage());
        }
        return null;
    }

    public static boolean verifySignature(String cleartext, byte[] textSignature, PublicKey publicKey) {
        try {
            //get instance of Signature with given constant; signatureAlgorithm
            final Signature signature = Signature.getInstance(signatureAlgorithm);
            //initialise signature with public key
            signature.initVerify(publicKey);
            //update signature with expected cleartext
            signature.update(cleartext.getBytes());
            //return boolean on whether keypair, where public key originates, signed textSignature
            return signature.verify(Base64.getDecoder().decode(textSignature));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: System does not have support for RSA-signature of this type!" + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("ERROR: Some error has occurred when verifying signature! " + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("ERROR: Invalid key supplied! " + e.getMessage());
        }
        return false;
    }

    static byte[] decrypt(byte[] ciphertext, Key key) {
        try {
            final Cipher cipher = Cipher.getInstance(cryptAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: could not get + " + cryptAlgorithm + " for decrypting! " + e.getMessage());
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

    static public PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    static public PrivateKey getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }
}

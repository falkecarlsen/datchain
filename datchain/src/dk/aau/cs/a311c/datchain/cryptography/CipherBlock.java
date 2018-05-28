package dk.aau.cs.a311c.datchain.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import static dk.aau.cs.a311c.datchain.cryptography.RSA.decrypt;
import static dk.aau.cs.a311c.datchain.cryptography.RSA.encrypt;

public class CipherBlock {

    //choosing 2^8, below modulo of RSA keys and allowing for padding, as blockSize, catering to filesystem preferences as well
    private static final int blockSize = 256;
    final private String cleartext;
    private byte[][] cipherBlock;
    private byte[][] clearBlock;
    private String decryptedText;
    private byte[] signature;

    public CipherBlock(String source) {
        //assign source string to cleartext and build block
        this.cleartext = source;
        buildBlock();
    }

    private void buildBlock() {
        //get numBlocks from current input by utilising int division flooring and adding one
        int numBlocks = (this.cleartext.getBytes().length / blockSize) + 1;

        //create dimensions of both two-dimensional byte arrays with calculated properties
        cipherBlock = new byte[numBlocks][blockSize];
        clearBlock = new byte[numBlocks][blockSize];

        //create counter for keeping track of current block of text
        int startIndex = 0;

        //for number of blocks needed to hold source in block, do
        for (int i = 0; i < numBlocks; i++) {
            //copy cleartext bytes from startIndex, blockSize-bytes forward and pass bytes to cipherBlock
            cipherBlock[i] = Arrays.copyOfRange(this.cleartext.getBytes(), startIndex, startIndex + blockSize);
            //move startIndex ahead blockLimit for next pass
            startIndex += blockSize;
        }
    }

    public void encryptBlock(PublicKey key) {
        //declare counter to keep track of block currently iterating over
        int index = 0;

        //for each block in built block, decrypt with key supplied and overwrite current index
        for (byte[] block : this.cipherBlock) {
            cipherBlock[index] = encrypt(block, key);
            index++;
        }
    }

    public void decryptBlock(PrivateKey key) {
        //declare counter to keep track of block currently iterating over
        int index = 0;

        //for each block, decrypt with key supplied and assign to current index in clearBlock
        for (byte[] block : this.cipherBlock) {
            clearBlock[index] = decrypt(block, key);
            index++;
        }
        //finally build decrypted cipher to string
        this.buildDecryptedText();
    }

    //issues an RSA challenge based on the two selected keys
    public static boolean issueChallenge(PrivateKey privateKey, PublicKey publicKey) {
        //create cipherblock and build, based on random string
        CipherBlock cipherBlock = new CipherBlock(RandomChallenge.generateRandomChallenge());

        //do operations on block
        cipherBlock.encryptBlock(publicKey);
        cipherBlock.decryptBlock(privateKey);

        //do a check, and see if the challenge is passed by the decrypted text, being the same as the cleartext
        return cipherBlock.getDecryptedText().equals(cipherBlock.getCleartext());
    }

    private void buildDecryptedText() {
        //initialise StringBuilder for appending each block of decrypted string
        StringBuilder stringBuilder = new StringBuilder();

        //for each block, append new string of bytes in block
        for (byte[] block : this.clearBlock) {
            stringBuilder.append(new String(block));
        }
        //assign newly built and trimmed string to decryptedText
        this.decryptedText = stringBuilder.toString().trim();
    }

    public void signBlock(PrivateKey privateKey) {
        this.signature = RSA.sign(this.cleartext, privateKey);
    }

    public boolean verifyBlock(PublicKey publicKey) {
        return RSA.verifySignature(this.cleartext, this.signature, publicKey);
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public String getCleartext() {
        return this.cleartext;
    }

    public String getDecryptedText() {
        return this.decryptedText;
    }
}

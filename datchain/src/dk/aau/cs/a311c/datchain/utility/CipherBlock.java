package dk.aau.cs.a311c.datchain.utility;

import java.security.Key;
import java.util.Arrays;

import static dk.aau.cs.a311c.datchain.utility.RSA.decrypt;
import static dk.aau.cs.a311c.datchain.utility.RSA.encrypt;

public class CipherBlock {

    //choosing 2^8 as blockSize for playing along with filesystem preferences
    private static final int blockSize = 256;
    private static int numBlocks = 0;
    final private String cleartext;
    private byte[][] cipherBlock;
    private byte[][] clearBlock;
    private String decryptedText;

    //TODO implement reverse operation (RSA signature)

    public CipherBlock(String source) {

        //assign source to cleartext
        this.cleartext = source;
    }

    public void buildBlock() {

        //get numBlocks from current input by utilising int division flooring and adding one
        numBlocks = (this.cleartext.getBytes().length / blockSize) + 1;

        //create dimensions of both blocks with calculated properties
        cipherBlock = new byte[numBlocks][blockSize];
        clearBlock = new byte[numBlocks][blockSize];

        //assign temporary bytearray of cleartext in bytes
        byte[] cleartextBytes = this.cleartext.getBytes();
        //create counter for keeping track of current block of text
        int startIndex = 0;

        //for number of blocks needed to hold source, do
        for (int i = 0; i < numBlocks; i++) {
            //copy bytes from startIndex, blockSize-bytes forward and pass bytes to cipherBlock
            cipherBlock[i] = Arrays.copyOfRange(cleartextBytes, startIndex, startIndex + blockSize);
            //move startIndex ahead blockLimit for next pass
            startIndex += blockSize;
        }
    }

    //TODO below
    //by passing generic key, both encrypt and decrypt operations can be facilitated with one method

    public void encryptBlock(Key key) {

        //declare counter to keep track of block currently iterating over
        int index = 0;

        //for each block in built block, decrypt with key supplied and overwrite current index
        for (byte[] block : this.cipherBlock) {
            cipherBlock[index] = encrypt(block, key);
            index++;
        }
    }

    public void decryptBlock(Key key) {

        //declare counter to keep track of block currently iterating over
        int index = 0;

        //for each block, decrypt with key supplied and assign to current index in clearBlock
        for (byte[] block : this.cipherBlock) {
            clearBlock[index] = decrypt(block, key);
            index++;
        }
    }

    public void buildDecryptedText() {
        //initialise StringBuilder for appending each block of decrypted string
        StringBuilder stringBuilder = new StringBuilder();

        //for each block, append new string of bytes in block
        for (byte[] block : this.clearBlock) {
            stringBuilder.append(new String(block));
        }

        //assign newly built and trimmed string to decryptedText
        this.decryptedText = stringBuilder.toString().trim();
    }

    public String getCleartext() {
        return this.cleartext;
    }

    public String getDecryptedText() {
        return this.decryptedText;
    }

    int getBlockSize() {
        return blockSize;
    }

    int getNumBlocks() {
        return numBlocks;
    }
}

package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.CipherBlock;
import dk.aau.cs.a311c.datchain.utility.RSA;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class Datchain {

    public static void main(String[] args) {

        GenesisBlock genesis01 = new GenesisBlock("Genesis", "19-09-1980","GenesisPublicKey", "0000");
        ValidatorBlock validator01 = new ValidatorBlock("Validator", "19-09-1980", "ValidatorPublicKey", genesis01.getHash(), "GenesisSignature");
        ValidatorBlock validator02 = new ValidatorBlock("Validator", "19-09-1980", "ValidatorPublicKey", validator01.getHash(), "GenesisSignature");
        ValidatorBlock validator03 = new ValidatorBlock("Validator", "19-09-1980", "ValidatorPublicKey", validator02.getHash(), "GenesisSignature");
        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1", "19-09-1980", "CitizenPublicKey", validator03.getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen02 = new CitizenBlock("Citizen Name2", "19-09-1980", "CitizenPublicKey", citizen01.getHash(), validator02.getIdentity(), validator02.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen03 = new CitizenBlock("Citizen Name3", "19-09-1980", "CitizenPublicKey", citizen02.getHash(), validator03.getIdentity(), validator03.getIdentityPublicKey(), "ValidatorSignature");

        Blockchain chain02 = new Blockchain();
        chain02.addValidatedBlock(genesis01, validator01);
        chain02.addValidatedBlock(validator01, validator01);
        chain02.addValidatedBlock(validator02, validator01);
        chain02.addValidatedBlock(validator03, validator01);
        chain02.addValidatedBlock(citizen01, validator01);
        chain02.addValidatedBlock(citizen02, validator01);
        chain02.addValidatedBlock(citizen03, validator01);

        System.out.println("chain02 validated: " + chain02.validateChain());


        System.out.println("RSA-keys present: " + RSA.keysPresent("data/"));

        KeyPair keyPair = RSA.keyPairInit();

        PublicKey publicKey = RSA.getPublicKey(keyPair);
        PrivateKey privateKey = RSA.getPrivateKey(keyPair);

        System.out.println("RSA keys generated: " + RSA.keyPairWriter(keyPair, "data/"));

        PublicKey publicKeyFromFile = RSA.getPublicKeyFromFile("data/public.key");
        PrivateKey privateKeyFromFile = RSA.getPrivateKeyFromFile("data/private.key");

        System.out.println("Public keys from file equal? " + publicKey.equals(publicKeyFromFile));
        System.out.println("Private keys from file equal? " + privateKey.equals(privateKeyFromFile));


        //test out encryption/decryption in cipherblocks
        String lorem1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque a risus posuere, malesuada nisl non, tempor nunc. Quisque quis sem vitae nunc fermentum rutrum a et neque. Curabitur sed mauris pulvinar, lobortis lacus sed, commodo ligula. Praesent suscipit, mauris eu mattis pulvinar, ante risus mollis tortor, ac iaculis augue elit porta nisl. Pellentesque quis urna interdum velit ultricies rhoncus imperdiet vel libero. Maecenas nec leo massa. Pellentesque lorem purus, scelerisque et ante sit amet, bibendum vehicula metus. Nunc cursus dui at erat pellentesque, eu mollis neque ultrices. Integer consequat varius dui, eget placerat diam egestas eget. Nulla rhoncus odio sed velit commodo, id accumsan erat venenatis. Suspendisse ut nulla ante. Quisque sed urna nunc. Sed egestas, tortor id fermentum convallis, eros quam vulputate neque, eget condimentum quam turpis sed enim. Maecenas metus est, congue id mollis non, tempus vel nisi. In elementum velit ipsum, quis euismod lorem suscipit at. Morbi malesuada nullam.";

        CipherBlock cipherBlock = new CipherBlock(lorem1);

        cipherBlock.encryptBlock(publicKey);

        cipherBlock.decryptBlock(privateKey);

        String decryptedText = cipherBlock.getDecryptedText();

        System.out.println("Are strings equal? " + cipherBlock.getCleartext().equals(cipherBlock.getDecryptedText()));

        //test out sign/verify in RSA

        byte[] signed = RSA.sign(lorem1, privateKey);

        System.out.println("did test pass signature? " + RSA.verifySignature(lorem1, signed, publicKey));

        //test out sign/verify on cipherblocks

        CipherBlock cipherBlock1 = new CipherBlock(lorem1);

        cipherBlock1.signBlock(privateKey);

        System.out.println("did cipherblock pass signature? " + cipherBlock1.verifyBlock(publicKey));
    }
}

package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.RSA;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Datchain {

    public static void main(String[] args) {

        GenesisBlock genesis01 = new GenesisBlock("Genesis", "GenesisPublicKey", "0000");
        ValidatorBlock validator01 = new ValidatorBlock("Validator", "ValidatorPublicKey", genesis01.getHash(), "GenesisSignature");
        ValidatorBlock validator02 = new ValidatorBlock("Validator", "ValidatorPublicKey", validator01.getHash(), "GenesisSignature");
        ValidatorBlock validator03 = new ValidatorBlock("Validator", "ValidatorPublicKey", validator02.getHash(), "GenesisSignature");
        CitizenBlock citizen01 = new CitizenBlock("Citizen Name1", "CitizenPublicKey", validator03.getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen02 = new CitizenBlock("Citizen Name2", "CitizenPublicKey", citizen01.getHash(), validator02.getIdentity(), validator02.getIdentityPublicKey(), "ValidatorSignature");
        CitizenBlock citizen03 = new CitizenBlock("Citizen Name3", "CitizenPublicKey", citizen02.getHash(), validator03.getIdentity(), validator03.getIdentityPublicKey(), "ValidatorSignature");

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


    }
}

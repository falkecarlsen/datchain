package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SetupChain {

    public static Blockchain getDefaultChain() {
        //create keypairs for testing
        KeyPair genesisKeypair = RSA.keyPairInit();
        PrivateKey genesisPrivateKey = RSA.getPrivateKey(genesisKeypair);
        PublicKey genesisPublicKey = RSA.getPublicKey(genesisKeypair);
        RSA.keyPairWriter(genesisKeypair, "data/");

        KeyPair validatorKeypair01 = RSA.keyPairInit();
        PrivateKey validatorPrivate01 = RSA.getPrivateKey(validatorKeypair01);
        PublicKey validatorPublic01 = RSA.getPublicKey(validatorKeypair01);

        KeyPair validatorKeypair02 = RSA.keyPairInit();
        PrivateKey validatorPrivate02 = RSA.getPrivateKey(validatorKeypair02);
        PublicKey validatorPublic02 = RSA.getPublicKey(validatorKeypair02);

        KeyPair validatorKeypair03 = RSA.keyPairInit();
        PrivateKey validatorPrivate03 = RSA.getPrivateKey(validatorKeypair03);
        PublicKey validatorPublic03 = RSA.getPublicKey(validatorKeypair03);

        KeyPair citizenKeypair01 = RSA.keyPairInit();
        PublicKey citizenPublicKey01 = RSA.getPublicKey(citizenKeypair01);

        KeyPair citizenKeypair02 = RSA.keyPairInit();
        PublicKey citizenPublicKey02 = RSA.getPublicKey(citizenKeypair02);

        KeyPair citizenKeypair03 = RSA.keyPairInit();
        PublicKey citizenPublicKey03 = RSA.getPublicKey(citizenKeypair03);


        GenesisBlock genesis01 = new GenesisBlock("Genesis Identity", "29-05-2018", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Mike Johnson", "30-05-1944", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator01, genesis01);

        ValidatorBlock validator02 = new ValidatorBlock("Jessica Peterson", "13-02-1995", RSA.getEncodedPublicKey(validatorPublic02), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator02, genesis01);

        ValidatorBlock validator03 = new ValidatorBlock("Oscar Mikkelsen", "22-03-1974", RSA.getEncodedPublicKey(validatorPublic03), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator03, genesis01);

        CitizenBlock citizen01 = new CitizenBlock("Peter Jensen", "01-05-1987", RSA.getEncodedPublicKey(citizenPublicKey01), chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        chain.addValidatedBlock(citizen01, validator01);

        CitizenBlock citizen02 = new CitizenBlock("Morten Hansen", "02-12-1955", RSA.getEncodedPublicKey(citizenPublicKey02), citizen01.getHash(), chain.getHead().getHash(), validator02.getIdentityPublicKey(), validatorPrivate02);
        chain.addValidatedBlock(citizen02, validator01);

        CitizenBlock citizen03 = new CitizenBlock("Dorte Espensen", "05-02-1994", RSA.getEncodedPublicKey(citizenPublicKey03), citizen02.getHash(), chain.getHead().getHash(), validator03.getIdentityPublicKey(), validatorPrivate03);
        chain.addValidatedBlock(citizen03, validator01);
        return chain;
    }
}

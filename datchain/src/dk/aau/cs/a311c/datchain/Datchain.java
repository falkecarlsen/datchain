package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.CipherBlock;
import dk.aau.cs.a311c.datchain.cryptography.RSA;
import dk.aau.cs.a311c.datchain.utility.StoreChain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class Datchain {

    public static void main(String[] args) {

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


        GenesisBlock genesis01 = new GenesisBlock("Genesis", "19-09-1980", RSA.getEncodedPublicKey(genesisPublicKey), "0000");
        Blockchain chain = new Blockchain(genesis01);

        ValidatorBlock validator01 = new ValidatorBlock("Validator1", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic01), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator01, genesis01);

        ValidatorBlock validator02 = new ValidatorBlock("Validator2", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic02), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator02, genesis01);

        ValidatorBlock validator03 = new ValidatorBlock("Validator3", "19-09-1980", RSA.getEncodedPublicKey(validatorPublic03), chain.getHead().getHash(), genesisPrivateKey);
        chain.addValidatedBlock(validator03, genesis01);

        CitizenBlock citizen01 = new CitizenBlock("Citizen1", "19-09-1980", RSA.getEncodedPublicKey(citizenPublicKey01), chain.getHead().getHash(), validator01.getIdentity(), validator01.getIdentityPublicKey(), validatorPrivate01);
        chain.addValidatedBlock(citizen01, validator01);

        CitizenBlock citizen02 = new CitizenBlock("Citizen2", "19-09-1980", RSA.getEncodedPublicKey(citizenPublicKey02), citizen01.getHash(), chain.getHead().getHash(), validator02.getIdentityPublicKey(), validatorPrivate02);
        chain.addValidatedBlock(citizen02, validator01);

        CitizenBlock citizen03 = new CitizenBlock("Citizen3", "19-09-1980", RSA.getEncodedPublicKey(citizenPublicKey03), citizen02.getHash(), chain.getHead().getHash(), validator03.getIdentityPublicKey(), validatorPrivate03);
        chain.addValidatedBlock(citizen03, validator01);


        System.out.println("is chain validated: " + chain.validateChain());

        //test serialisation

        System.out.println("Could chain be serialised and written to filesystem? " + StoreChain.writeChainToFilesystem("data/", chain));

        Blockchain reconstitutedChain = StoreChain.readChainFromFilesystem("data/");
        //assert that reconstitutedChain is not null
        

        System.out.println("is reconstituted chain valid? " + reconstitutedChain.validateChain());

        System.out.println("is reconstituted chain size: " + reconstitutedChain.size());
        System.out.println("does reconstituted chain contain validator01? " + reconstitutedChain.validatorExistsOnChain(validator01));
        System.out.println("does reconstituted chain contain validator02? " + reconstitutedChain.validatorExistsOnChain(validator02));
        System.out.println("does reconstituted chain contain validator03? " + reconstitutedChain.validatorExistsOnChain(validator03));

        //test keys

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

        //System.out.println(Arrays.toString(cipherBlock1.getSignature()));

        CipherBlock cipherBlock2 = new CipherBlock(lorem1);

        cipherBlock2.signBlock(privateKey);

        //System.out.println(Arrays.toString(cipherBlock2.getSignature()));

        System.out.println("did cipherblock pass signature? " + cipherBlock1.verifyBlock(publicKey));
    }
}

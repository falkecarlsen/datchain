package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.RSA;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ValidatorBlock extends Block {

    private final byte[] genesisSignature;

    public ValidatorBlock(String identity, String identityDOB, String identityPublicKey, String prevHash, PrivateKey validatorPrivateKey) {
        //call super-constructor with all variables as hashInput
        super(identity, identityDOB, identityPublicKey, prevHash, identity + identityDOB + identityPublicKey + prevHash);
        //compute signature from volatile information
        this.genesisSignature = RSA.sign(getVolatileInformation(), validatorPrivateKey);
    }

    public byte[] getGenesisSignature() {
        return this.genesisSignature;
    }


    public boolean verifySignature(PublicKey validatorPublicKey) {
        return RSA.verifySignature(this.getVolatileInformation(), this.getGenesisSignature(), validatorPublicKey);
    }
}

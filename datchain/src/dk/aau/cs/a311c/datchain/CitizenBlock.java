package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;

import java.security.PrivateKey;

public class CitizenBlock extends Block {

    private final String validatorIdentity;
    private final String validatorPublicKey;
    private final byte[] validatorSignature;

    public CitizenBlock(String identity, String identityDOB, String identityPublicKey, String prevHash, String validatorIdentity, String validatorPublicKey, PrivateKey validatorPrivateKey) {
        //call super-constructor with all variables for hashInput, equal to volatile information and validator information
        super(identity, identityDOB, identityPublicKey, prevHash, identity + identityPublicKey + prevHash + validatorIdentity + validatorPublicKey);
        //assign fields with variables on validator
        this.validatorIdentity = validatorIdentity;
        this.validatorPublicKey = validatorPublicKey;
        //compute signature from volatile information
        this.validatorSignature = RSA.sign(getVolatileInformation(), validatorPrivateKey);
    }

    //verify signature of validator by passing expected input, signature and validator public key to RSA.verifySignature
    public boolean verifySignature() {
        //returns true if signature is verified by stored public key, signature and expected volatile information
        return RSA.verifySignature(this.getVolatileInformation(), this.validatorSignature, RSA.getPublicKeyFromEncoded(this.validatorPublicKey));
    }

    public byte[] getGenesisSignature() {
        return this.validatorSignature;
    }
}

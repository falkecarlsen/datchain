package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.RSA;

import java.security.PrivateKey;

public class CitizenBlock extends Block {

    private final String validatorIdentity;
    private final String validatorPublicKey;
    private final byte[] validatorSignature;

    public CitizenBlock(String identity, String identityDOB, String identityPublicKey, String prevHash, String validatorIdentity, String validatorPublicKey, PrivateKey validatorPrivateKey) {
        super(identity, identityDOB, identityPublicKey, prevHash, identity + identityPublicKey + prevHash + validatorIdentity + validatorPublicKey);
        this.validatorIdentity = validatorIdentity;
        this.validatorPublicKey = validatorPublicKey;
        //compute signature from volatile information
        this.validatorSignature = RSA.sign(getVolatileInformation(), validatorPrivateKey);
    }

    public byte[] getGenesisSignature() {
        return this.validatorSignature;
    }

    public boolean verifySignature() {
        //returns true if signature is verified by stored public key, signature and expected volatile information
        return RSA.verifySignature(this.getVolatileInformation(), this.validatorSignature, RSA.getPublicKeyFromEncoded(this.validatorPublicKey));
    }
}

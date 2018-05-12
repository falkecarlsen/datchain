package dk.aau.cs.a311c.datchain;

public class CitizenBlock extends Block {

    private final String identityDOB;
    private final String validatorIdentity;
    private final String validatorPublicKey;
    private final String validatorSignature;

    public CitizenBlock(String identity, String identityDOB, String identityPublicKey, String prevHash, String validatorIdentity, String validatorPublicKey, String validatorSignature) {
        super(identity, identityPublicKey, prevHash, identity + identityPublicKey + prevHash + validatorIdentity + validatorPublicKey);
        this.identityDOB = identityDOB;
        this.validatorIdentity = validatorIdentity;
        this.validatorPublicKey = validatorPublicKey;
        this.validatorSignature = validatorSignature;
    }


}

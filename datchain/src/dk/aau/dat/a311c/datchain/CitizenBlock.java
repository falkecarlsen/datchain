package dk.aau.dat.a311c.datchain;

public class CitizenBlock extends Block {

    private final String validatorIdentity;
    private final String validatorPublicKey;
    private final String validatorSignature;

    public CitizenBlock(String identity, String identityPublicKey, String prevHash, String validatorIdentity, String validatorPublicKey, String validatorSignature) {
        super(identity, identityPublicKey, prevHash, identity + identityPublicKey + prevHash + validatorIdentity + validatorPublicKey);
        this.validatorIdentity = validatorIdentity;
        this.validatorPublicKey = validatorPublicKey;
        this.validatorSignature = validatorSignature;
    }


}

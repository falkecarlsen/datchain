package dk.aau.cs.a311c.datchain;

public class GenesisBlock extends Block {

    public GenesisBlock(String identity, String identityDOB, String identityPublicKey, String prevHash) {
        //call super-constructor with all variables as hashInput
        super(identity, identityDOB, identityPublicKey, prevHash, identity + identityPublicKey + prevHash);
    }
}

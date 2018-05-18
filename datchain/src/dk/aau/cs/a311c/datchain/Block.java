package dk.aau.cs.a311c.datchain;

import static dk.aau.cs.a311c.datchain.utility.SHA.computeHash;
import static java.time.Instant.now;

public abstract class Block {

    //common fields for all blocks
    private final String identity;
    private final String identityPublicKey;
    private final String prevHash;
    private final String hash;
    private final String identityDOB;
    private final long timestamp;

    protected Block(String identity, String identityDOB, String identityPublicKey, String prevHash, String hashInput) {
        this.identity = identity;
        this.identityDOB = identityDOB;
        this.identityPublicKey = identityPublicKey;
        this.prevHash = prevHash;
        this.timestamp = now().getEpochSecond();
        this.hash = computeHash(hashInput + this.timestamp);
    }

    public String getHash() {
        return this.hash;
    }

    public String getPrevHash() {
        return this.prevHash;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getIdentity() {
        return this.identity;
    }

    public String getIdentityPublicKey() {
        return this.identityPublicKey;
    }

    public String getIdentityDOB() {
        return identityDOB;
    }

    public String getVolatileInformation() {
        return this.identity + this.identityDOB + this.identityPublicKey + this.prevHash;
    }
}



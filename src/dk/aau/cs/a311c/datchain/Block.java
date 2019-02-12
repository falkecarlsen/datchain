package dk.aau.cs.a311c.datchain;

import java.util.Objects;

import static dk.aau.cs.a311c.datchain.cryptography.SHA.computeHash;
import static java.time.Instant.now;

public abstract class Block {

    //union of fields for all blocks
    private final String identity;
    private final String identityDOB;
    private final String identityPublicKey;
    private final String prevHash;
    private final String hash;
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

    String getVolatileInformation() {
        return this.identity + this.identityDOB + this.identityPublicKey + this.prevHash;
    }

    //overriding default equals- and hashCode-methods for proper equals behaviour
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return getTimestamp() == block.getTimestamp() &&
                Objects.equals(getIdentity(), block.getIdentity()) &&
                Objects.equals(getIdentityDOB(), block.getIdentityDOB()) &&
                Objects.equals(getIdentityPublicKey(), block.getIdentityPublicKey()) &&
                Objects.equals(getPrevHash(), block.getPrevHash()) &&
                Objects.equals(getHash(), block.getHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentity(), getIdentityDOB(), getIdentityPublicKey(), getPrevHash(), getHash(), getTimestamp());
    }
}
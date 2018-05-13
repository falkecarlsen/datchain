package dk.aau.cs.a311c.datchain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static dk.aau.cs.a311c.datchain.utility.SHA.computeHash;
import static java.time.Instant.now;

abstract class Block {

    //common fields for all blocks
    private final String identity;
    private final String identityPublicKey;
    private final String prevHash;
    private final String hash;
    private final String identityDOB;
    private final long timestamp;

    protected Block(String identity, String identityPublicKey, String prevHash, String hashInput, String identityDOB) {
        this.identity = identity;
        this.identityPublicKey = identityPublicKey;
        this.prevHash = prevHash;
        this.timestamp = now().getEpochSecond();
        this.hash = computeHash(hashInput + this.timestamp);
        this.identityDOB = identityDOB;
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
}



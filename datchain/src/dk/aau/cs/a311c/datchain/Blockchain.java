package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.cryptography.RSA;

import java.security.PublicKey;
import java.util.ArrayList;

public class Blockchain extends ArrayList<Block> {

    //no args constructor needed for deserialising chain
    public Blockchain() {
    }

    public Blockchain(GenesisBlock genesisBlock) {
        this.add(genesisBlock);
    }

    public boolean addValidatedBlock(Block block, Block validator) {

        //if local, current chain is not valid, abort adding block
        if (!this.validateChain()) return false;

        //search for public key on chain equal to proposed block and count instances
        int duplicateBlockInstances = 0;
        String duplicateBlockDOB = "";
        for (Block localBlock : this) {
            if (localBlock.getIdentityPublicKey().equals(block.getIdentityPublicKey())) {
                duplicateBlockDOB = localBlock.getIdentityDOB();
                duplicateBlockInstances++;
            }
        }

        //if public key occurs on chain exactly once, and name reads "Revoked", do
        if (duplicateBlockInstances == 1 &&
                block.getIdentity().equals("Revoked") &&
                block.getIdentityDOB().equals(duplicateBlockDOB)) {
            //return return value of add block method call
            return add(block);
            //if identity already has been revoked, by existing more than once on chain, adding revoking block is illegal
        } else if (duplicateBlockInstances > 1) {
            System.out.println("ERROR: Adding a block with equal public key to revoked block is illegal!");
            return false;
        }

        // if block to be added is of GenesisBlock-type, return false - as only constructor can add Genesis
        if (block instanceof GenesisBlock) return false;
            //check if block to be added is of ValidatorBlock-type and if chainsize is greater than 0 and if block at 0 contains public key of genesis-block passed
        else if (block instanceof ValidatorBlock && this.size() > 0 && this.get(0).getIdentityPublicKey().equals(validator.getIdentityPublicKey())) {
            //return return value of add block method call
            return this.add(block);

            //check if block to be added is of CitizenBlock-type and if chainsize is greater than 0 and whether validator exists on chain using typecasting
        } else if (block instanceof CitizenBlock && this.size() > 1 && validatorExistsOnChain((ValidatorBlock) validator)) {
            //return return value of add block method call
            return this.add(block);

            //if none match, block is not recognized and a fatal error has occurred
        } else {
            System.out.println("ERROR: Could not add block, some dependency is not satisfied!");
            return false;
        }
    }

    public boolean validateChain() {

        String currHash, nextPrevHash;
        long currTime, nextTime;

        //stop loop short one of this.size() as last block will not have next
        for (int i = 0; i < this.size() - 1; i++) {

            //assign hashes to new strings for code legibility
            currHash = get(i).getHash();
            currTime = get(i).getTimestamp();

            nextPrevHash = get(i + 1).getPrevHash();
            nextTime = get(i + 1).getTimestamp();

            //check hash congruency through blocks
            if (!currHash.equals(nextPrevHash)) return false;

            //check time is equal or later through blocks
            if (currTime > nextTime) return false;
        }

        for (Block block : this) {
            //if block is of ValidatorBlock type, genesis signed block's signature
            if (block instanceof ValidatorBlock) {
                //verify signature of genesis in given ValidatorBlock, if returns false, chain is invalid
                if (!((ValidatorBlock) block).verifySignature(this.getGenesisPublicKey())) return false;
            } else if (block instanceof CitizenBlock) {
                //verify signature of validator in given CitizenBlock, if returns false, chain is invalid
                if (!((CitizenBlock) block).verifySignature()) return false;
            } else {
                //for all other blocks, if not genesis, return false, as chain is invalid
                if (!(block instanceof GenesisBlock)) return false;
            }
        }
        //if no congruency errors are found, chain is valid
        return true;
    }

    //check all blocks in chain whether validators public key exists on chain
    private boolean validatorExistsOnChain(ValidatorBlock validatorBlock) {
        for (Block block : this) {
            if (block instanceof ValidatorBlock && block.getIdentityPublicKey().equals(validatorBlock.getIdentityPublicKey()))
                return true;
        }
        return false;
    }

    private PublicKey getGenesisPublicKey() {
        return RSA.getPublicKeyFromEncoded(this.get(0).getIdentityPublicKey());
    }

    //ArrayList doesn't implement a .last() method, thus we implement one ourselves
    public Block getHead() {

        Block head;
        try {
            //throw exception if no blocks are added, probably not the prettiest handling
            if (this.size() == 0) throw new RuntimeException("ERROR: No blocks added, cannot get head");
            head = this.get(this.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("ERROR: No blocks added, cannot get head" + e.getMessage());
        }
        return head;
    }

    public Blockchain getChain() {
        return this;
    }
}

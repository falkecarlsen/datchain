package dk.aau.cs.a311c.datchain;

import dk.aau.cs.a311c.datchain.utility.RSA;

import java.security.PublicKey;
import java.util.ArrayList;

public class Blockchain extends ArrayList<Block> {

    public Blockchain(GenesisBlock genesisBlock) {
        this.add(genesisBlock);
    }

    public boolean addValidatedBlock(Block block, Block validator) {

        //if local, current chain is not valid, abort adding block
        if (!this.validateChain()) return false;

        // if block to be added is of GenesisBlock-type, return false - as only constructor can add Genesis
        if (block instanceof  GenesisBlock) return false;
        //check if block to be added is of ValidatorBlock-type and if chainsize is greater than 0
        // and if block at 0 contains public key of genesis-block passed
        else if (block instanceof ValidatorBlock && this.size() > 0 && this.get(0).getIdentityPublicKey().equals(validator.getIdentityPublicKey())) {
            System.out.println("Block is of ValidatorBlock-type and chain has at least one block");
            this.add(block);

            //check if block to be added is of CitizenBlock-type and if chainsize is greater than 0
            // and whether validator exists on chain using typecasting
        } else if (block instanceof CitizenBlock && this.size() > 1 && validatorExistsOnChain((ValidatorBlock) validator)) {
            System.out.println("Block is of citizen type and validator exists on chain");
            this.add(block);

            //if none match, block is not recognized and a fatal error has occurred
        } else {
            throw new RuntimeException("ERROR: Could not add block, some dependency is not satisfied!");
        }
        return true;
    }

    //might not be necessary, however signature doesn't match when addValidatedBlock is considered
    @Override
    public boolean add(Block block) {
        try {
            super.add(block);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
            return false;
        }
    }

    public boolean validateChain() {

        String currHash, nextPrevHash;
        long currTime, nextTime;

        //stop loop short one of this.size() as last block will not have next
        for (int i = 0; i < this.size() - 1; i++) {

            //assign hashes to new strings for code legibility
            currHash = getBlock(i).getHash();
            currTime = getBlock(i).getTimestamp();

            nextPrevHash = getBlock(i + 1).getPrevHash();
            nextTime = getBlock(i + 1).getTimestamp();

            //check hash congruency through blocks
            if (!currHash.equals(nextPrevHash)) return false;

            //check time is equal or later through blocks
            if (currTime > nextTime) return false;

            //TODO should also test chain of RSA-signature from genesis to all validators and possibly citizens
        }
        //if no congruency errors are found, chain is valid
        return true;
    }

    //check all blocks in chain whether validators public key exists on chain
    public boolean validatorExistsOnChain(ValidatorBlock validatorBlock) {
        for (Block block : this) {
            if (block instanceof ValidatorBlock && block.getIdentityPublicKey().equals(validatorBlock.getIdentityPublicKey()))
                return true;
        }
        return false;
    }

    public PublicKey getGenesisPublicKey() {
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

    public Block getBlock(int index) {
        Block block;
        try {
            block = get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Index out of bounds, can't get block");
        }
        return block;
    }

    public Blockchain getChain() {
        return this;
    }

}

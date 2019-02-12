package dk.aau.cs.a311c.datchain.utility;

import dk.aau.cs.a311c.datchain.Block;
import dk.aau.cs.a311c.datchain.Blockchain;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private final ArrayList<String> arraySource = new ArrayList<>();
    private List<ExtractedResult> searchResults = new ArrayList<>();
    private final ArrayList<Block> blockResults = new ArrayList<>();

    public ArrayList<Block> FuzzySearchIdentity(String term, Blockchain chain, int cutoff) {

        //avoid OutOfBounds exception
        if (cutoff > chain.size()) cutoff = chain.size();

        //deep copy block.getIdentity to arraySource
        for (Block block : chain) {
            this.arraySource.add(block.getIdentity());
        }
        //run fuzzywuzzy on string-copy of identities with a size of cutoff
        searchResults = FuzzySearch.extractTop(term, arraySource, cutoff);

        //for cutoff, get blocks from chain, from searchResults and add to primitive arraylist
        for (ExtractedResult result : searchResults) {
            blockResults.add(chain.get(result.getIndex()));
        }
        return blockResults;
    }

    public ArrayList<Block> FuzzySearchIdentityPublicKey(String term, Blockchain chain, int cutoff) {

        //avoid OutOfBounds exception
        if (cutoff > chain.size()) cutoff = chain.size();

        //deep copy block to arraySource
        for (Block block : chain) {
            this.arraySource.add(block.getIdentityPublicKey());
        }
        //run fuzzywuzzy on string-copy of public keys with a size of cutoff
        searchResults = FuzzySearch.extractTop(term, arraySource, cutoff);

        //for cutoff, get blocks from chain, from searchResults and add to primitive arraylist
        for (ExtractedResult searchResult : searchResults) {
            blockResults.add(chain.get(searchResult.getIndex()));
        }
        return blockResults;
    }

    public ArrayList<Block> FuzzySearchIdentityDOB(String term, Blockchain chain, int cutoff) {

        //avoid OutOfBounds exception
        if (cutoff > chain.size()) cutoff = chain.size();

        //deep copy block to arraySource
        for (Block block : chain) {
            this.arraySource.add(block.getIdentityDOB());
        }
        //run fuzzywuzzy on string-copy of public keys with a size of cutoff
        searchResults = FuzzySearch.extractTop(term, arraySource, cutoff);

        //for cutoff, get blocks from chain, from searchResults and add to primitive arraylist
        for (ExtractedResult searchResult : searchResults) {
            blockResults.add(chain.get(searchResult.getIndex()));
        }
        return blockResults;
    }
}

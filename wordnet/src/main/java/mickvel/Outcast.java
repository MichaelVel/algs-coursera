package mickvel;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        this.wordnet = wordnet;
    }
   
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) { 
        if (nouns == null) throw new IllegalArgumentException();

        String word = null;
        int maxDist = -1;
        for (String noun: nouns) {
            int nounDist = 0;

            for (String nounB: nouns) {
                if (noun.equals(nounB)) continue;
                nounDist += wordnet.distance(noun, nounB);
            }

            if (nounDist > maxDist) {
                word = noun;
                maxDist = nounDist;
            }

        }
	    return word;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

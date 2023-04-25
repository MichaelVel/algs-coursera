package mickvel;

import java.util.HashMap;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    HashMap<String,Bag<Integer>> map;
    Digraph graph;
    private int N = 0;

   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        parseSynsets(synsets);
        parseHypernyms(hypernyms);
    }


    private void parseSynsets(String namefile) {
        map = new HashMap<>();
        In stream = new In(namefile);
        while (stream.hasNextLine()) {
            N++;
            String[] lineData = stream.readLine().split(",");
            int id = Integer.parseInt(lineData[0]);
            String[] synsets = lineData[1].split(" ");
            for (String synset: synsets) {
                map.putIfAbsent(synset, new Bag<>());
                map.get(synset).add(id);
            }
        }
    }

    private void parseHypernyms(String namefile) {
        graph = new Digraph(N);
        In stream = new In(namefile);
        while (stream.hasNextLine()) {
            String[] lineData = stream.readLine().split(",");
            int v = Integer.parseInt(lineData[0]);
            for (int i = 1; i < lineData.length; i++) {
                int w = Integer.parseInt(lineData[i]);
                graph.addEdge(v, w);
            }
        }
   }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0; 
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return nounB;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);

        StdOut.println("The nouns in wordnet");
        for (Entry<String, Bag<Integer>> p: wn.map.entrySet()) {
            StdOut.print(p.getKey() + ": [ ");
            for (int id: p.getValue()) StdOut.print(id + " ");
            StdOut.println("]");
        }

        StdOut.println(wn.graph);
    }
}

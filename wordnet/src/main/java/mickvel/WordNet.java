package mickvel;

import java.util.HashMap;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private HashMap<String,Bag<Integer>> nounsMap;
    private HashMap<Integer,String[]> synsetsMap;
    private Digraph graph;
    private int N = 0;

   // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        parseSynsets(synsets);
        parseHypernyms(hypernyms);
    }


    private void parseSynsets(String namefile) {
        if (namefile == null) throw new IllegalArgumentException();
        nounsMap = new HashMap<>();
        synsetsMap = new HashMap<>();
        In stream = new In(namefile);
        while (stream.hasNextLine()) {
            N++;
            String[] lineData = stream.readLine().split(",");
            int id = Integer.parseInt(lineData[0]);
            String[] synsets = lineData[1].split(" ");

            synsetsMap.put(id, synsets);

            for (String synset: synsets) {
                nounsMap.putIfAbsent(synset, new Bag<>());
                nounsMap.get(synset).add(id);
            }
        }
    }

    private void parseHypernyms(String namefile) {
        if (namefile == null) throw new IllegalArgumentException();
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

        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle()) throw new IllegalArgumentException();
        
   }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNouns(new String[] {nounA, nounB});
        SAP sap = new SAP(graph);
        return sap.length(nounsMap.get(nounA), nounsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNouns(new String[] {nounA, nounB});
        SAP sap = new SAP(graph);
        int synset = sap.length(nounsMap.get(nounA), nounsMap.get(nounB));
        return synsetsMap.get(synset)[0];
    }

    private void validateNouns(String[] nouns){
        for (String noun: nouns) 
            if (!isNoun(noun)) throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);

        StdOut.println("The nouns in wordnet");
        for (Entry<String, Bag<Integer>> p: wn.nounsMap.entrySet()) {
            StdOut.print(p.getKey() + ": [ ");
            for (int id: p.getValue()) StdOut.print(id + " ");
            StdOut.println("]");
        }

        StdOut.println(wn.graph);
    }
}

import java.util.stream.IntStream;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class QuickUnionUF {
    // In this data structure the id array store a reference for each element
    // to its parent element. A element pointing to intself is said to be the 
    // root of the tree of the connected elements.

    private int[] id;

    QuickFindUF(int N) {
        id = new int[N];
        for (int i = 0; i < N; i++) id[i] = i;
    }
    
    private Pair<Integer, Integer> root(int elem) {
        int height = 0;
        while (elem != id[elem]) {
            elem = id[elem];
            height++;
        }

        return new Pair<>(elem, height);
    }

    public void union(int p, int q) {
        var rootP = root(p);
        var rootQ = root(q);
        
        if (rootP.getValue() < rootQ.getValue()) {
            id[rootP.getKey()] = rootQ.getKey();
        } else {
            id[rootQ.getKey()] = rootP.getKey();
        }

    }

    public boolean connected(int p, int q) {
        return getRoot(p).getKey() == getRoot(q).getKey();
    }

    public int find(int p) {
        return getRoot(p).getKey();
    }

    public int count() {
        return (int) IntStream.of(id).distinct().count();
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        QuickFindUF uf = new QuickFindUF(N);

        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();

            if (!uf.connected(p, q)) {
                uf.union(p,q);
                StdOut.println(p + " " + q);
            }
        }
    }
}

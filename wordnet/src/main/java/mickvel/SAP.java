package mickvel;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph graph;

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
       if ( G == null) throw new IllegalArgumentException();
       this.graph = new Digraph(G);
   }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkInRange(v); checkInRange(w);
        return sap(bfdp(v), bfdp(w))[0];
    }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such pathpublic
    public int ancestor(int v, int w) {
        checkInRange(v); checkInRange(w);
        return sap(bfdp(v), bfdp(w))[1];
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v); checkIterable(w);
        return sap(bfdp(v), bfdp(w))[0];
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
       checkIterable(v); checkIterable(w);
        return sap(bfdp(v), bfdp(w))[1];
   }

   private void checkIterable(Iterable<Integer> v) {
       if (v == null) throw new IllegalArgumentException();
       for (Integer i: v) if (i == null) throw new IllegalArgumentException();
   }

   private void checkInRange(int v) {
       if ( v < 0 || v > graph.V()) throw new IllegalArgumentException();
   }

   private BreadthFirstDirectedPaths bfdp(Iterable<Integer> v) {
       return new BreadthFirstDirectedPaths(graph, v);
   }
   
   private BreadthFirstDirectedPaths bfdp(int v) {
       return new BreadthFirstDirectedPaths(graph, v);
   }

   private int[] sap(BreadthFirstDirectedPaths s1, BreadthFirstDirectedPaths s2) {
        int dist = -1;
        int ancestor = -1;

        for (int vertex = 0; vertex < graph.V(); vertex++) {
            if (s1.hasPathTo(vertex) && s2.hasPathTo(vertex)) {
                int lDist = s1.distTo(vertex) + s2.distTo(vertex);
                if (dist == -1 || lDist < dist) {
                    dist = lDist;
                    ancestor = vertex;
                }
            }
        }

        return new int[]{dist, ancestor};
   }

   // do unit testing of this class
   public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

   }
}

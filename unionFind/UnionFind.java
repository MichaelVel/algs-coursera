import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class UnionFind {

    UnionFind(int N) {

    }

    public void union(int p, int q) {
    }

    public boolean connected(int p, int q) {
        return true;
    }

    public int find(int p) {
        return 0;
    }

    public int count() {
        return 0;
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        UnionFind uf = new UF(N);

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

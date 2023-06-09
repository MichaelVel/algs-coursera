package mickvel;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> bag = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            bag.enqueue(str);
        }

        for (int i = 0; i < k; i++) 
            StdOut.println(bag.dequeue());
    }
}

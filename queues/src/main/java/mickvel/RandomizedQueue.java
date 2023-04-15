package mickvel;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 8;

    private Item[] queue;
    private int n;
    
    // Better to have this ugly code only in one place in the type.
    private Item[] createArray(int capacity) {
        return (Item[]) new Object[capacity];
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = createArray(INIT_CAPACITY);
        n = 0;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = createArray(capacity);
        for (int i = 0; i < n; i++)
            copy[i] = queue[i];
        queue = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == queue.length) resize(2*queue.length);
        queue[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int index = StdRandom.uniformInt(n);
        Item val = queue[index];

        queue[index] = queue[n-1];
        queue[n-1] = null;
        n -= 1;
        
        if (n > 0 && n == queue.length/4) resize(queue.length/2);

        return val;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        int index = StdRandom.uniformInt(n);
        Item val = queue[index];
        
        return val;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] indexs;
        private int i;

        public RandomIterator() {
            indexs = new int[n];
            for (int i=0; i < n; i++) indexs[i] = i;
            StdRandom.shuffle(indexs);

            i = n-1;
        }

		@Override
		public boolean hasNext() {
			return i >= 0;
		}

		@Override
		public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int index = indexs[i--];
			return queue[index];
		}

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) queue.enqueue(i);

        // Each round should yield unique sequence
        for (int i= 0; i < 3; i++) {
            for (int val: queue) StdOut.print(val + " ");
            StdOut.println();
        }

        // Empty the queue
        for (int i = 0; i < 10; i++) StdOut.print(queue.dequeue() + " ");
        StdOut.println();

        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            StdOut.println("Correct behaviour to an dequeue operation on a empty queue");
        }

        try {
            queue.enqueue(null);
        } catch (IllegalArgumentException e) {
            StdOut.println("Correct behaviour to try to enqueue an null val");
        }
    }

}

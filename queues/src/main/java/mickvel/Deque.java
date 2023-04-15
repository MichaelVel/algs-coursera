package mickvel;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;

        Node(Item item) { this.item = item; }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0; 
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // handle special case when deque is empy.
    private boolean addIfEmpty(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        
        if (!isEmpty()) { return false; }

        Node<Item> node = new Node<>(item);
        first = node;
        last = node;

        n += 1;
        return true;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (addIfEmpty(item)) return;

        Node<Item> node = new Node<>(item);
        first.prev = node;
        node.next = first;
        first = node;

        n += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (addIfEmpty(item)) return;

        Node<Item> node = new Node<>(item);
        node.prev = last;
        last.next = node;
        last = node;

        n += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) 
            throw new NoSuchElementException();
        
        Item val = first.item;

        if ( n == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        n -= 1;

        return val;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) 
            throw new NoSuchElementException();

        Item val = last.item;

        if ( n == 1) {
           first = null;
           last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        
        n -= 1;

        return val;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    // an iterator to the deque
    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
			return item;
		}

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        
        deque.addLast(1); // [ 1 ]
        deque.addLast(2); // [ 1 2 ]
        deque.addLast(3); // [ 1 2 3 ]
                          
        deque.addFirst(0); // [ 2 1 2 3 ]
        deque.addFirst(-1); // [ 3 2 1 2 3 ]
        
        for (int val: deque) {
            StdOut.print(val + " " );
        }

        StdOut.println();
        StdOut.println("Size: " + deque.size());

        int i = 0;
        while (!deque.isEmpty()) {
            if (i%2 == 0) {
                StdOut.println("Remove from first: " + deque.removeFirst());
            } else {
                StdOut.println("Remove from last: " + deque.removeLast());
            }

            i++;
        }
    }

}

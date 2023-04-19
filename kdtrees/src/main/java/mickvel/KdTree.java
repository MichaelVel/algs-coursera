package mickvel;

import java.awt.Point;
import java.util.Stack;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;     // root of the BST

    private static final boolean Y = true;
    private static final boolean X = false;


    // BST helper node data type
    private class Node {
        private Point2D val;         
        private Node left, right;  
        private int size;          
        private boolean direction;

        public Node(boolean direction, Point2D val, int size) {
            this.val = val;
            this.size = size;
            this.direction = direction;
        }

        public double key() {
            return direction == Y ? val.y() : val.x();
        }

        public int compareToKey(Point2D p) {
            double pKey = direction == X ? p.x() : p.y();
            if (key() < pKey) return -1; // this node key lesser than p
            if (key() > pKey) return 1;  // this node key greater than p
            return 0;
        }

        public void draw() {
        }
    }
    
    public KdTree() { }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }
    
    public boolean isEmpty() { return root == null; }

    public int size() { return size(root); }

    private Point2D get(Node x, Point2D p) {
        while (x != null) {
            int compare = x.compareToKey(p);

            if      (compare > 0) x = x.left;
            else if (compare < 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, Y);
    }

    private Node insert(Node x, Point2D p, boolean direction) {
        if (x == null) return new Node(direction, p, 1);
        int cmp = x.compareToKey(p);
        if      (cmp > 0) x.left  = insert(x.left, p, !direction);
        else if (cmp < 0) x.right = insert(x.right, p, !direction);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public boolean contains(Point2D p) {
        return get(root,p) != null;       
    }

    public void draw() {
        draw(root, null);
    }

    private void draw(Node n, Node parent) {
        if (n == null) return ; 
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);

        Point2D p = n.val;
        p.draw();

        StdDraw.setPenColor(n.direction == Y ? StdDraw.RED : StdDraw.BLUE);

        Point2D from;
        Point2D to;

        if (n.direction == Y) {
            double x = p.x();
            double yFrom = parent == null ? 0 : ;
            double yTo = parent == null ? 1;
        } else {
        }

        from.drawTo(to);

        draw(n.left, n);
        draw(n.right, n);
    }

    

    public Iterable<Point2D> range(RectHV rect) {
        throw new UnsupportedOperationException();
    }

    public Point2D nearest(Point2D p) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
    }

}

package mickvel;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

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
            return direction == Y ? val.x() : val.y();
        }

        public int compareToKey(Point2D p) {
            double pKey = direction == Y ? p.x() : p.y();
            if (key() < pKey) return -1; // this node key lesser than p
            if (key() > pKey) return 1;  // this node key greater than p
            return 0;
        }
        
        public String toString() {
            String direction = this.direction ? "vertical" : "horizontal";
            String childs = String.format(
                    "left: %s, right: %s", 
                    left != null ? left.val : "None",
                    right != null ? right.val : "None");

            return String.format(
            "Point %s, with direction %s. childs: %s", val, direction, childs);
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
        draw(root, 0.0, 1.0, 0.0, 1.0);
    }

    private void draw(Node n, double xMin, double xMax, double yMin, double yMax) {
        if (n == null) return ; 

        Point2D p = n.val;
        Point2D from;
        Point2D to;

        StdOut.println(n);

        if (n.direction == Y) {
            from = new Point2D(p.x(), yMin);
            to = new Point2D(p.x(), yMax);
        } else {
            from = new Point2D(xMin, p.y());
            to = new Point2D(xMax, p.y());
        }

        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(n.direction == Y ? StdDraw.RED : StdDraw.BLUE);

        from.drawTo(to);

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);

        p.draw();

        double middle = n.direction == Y ? p.x() : p.y();
        
        if (n.direction == Y ){
            draw(n.left, xMin, middle, yMin, yMax);
            draw(n.right, middle, xMax, yMin, yMax);
        } else {
            draw(n.left, xMin, xMax, yMin, middle);
            draw(n.right, xMin, xMax, middle, yMax);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        ResizingArrayBag<Point2D> bag = new ResizingArrayBag<>();
        range(root, rect, bag); 
        return bag;
    }

    private void range(Node n, RectHV rect, ResizingArrayBag<Point2D> bag) {
        if (n == null) return;

        Point2D p = n.val;
        RectHV division = n.direction == Y 
            ? new RectHV(p.x(), 0, p.x(), 1)
            : new RectHV(0, p.y(), 1, p.y());
        int comp = n.compareToKey(new Point2D(rect.xmin(), rect.ymin()));

        if (rect.contains(p)) bag.add(p);

        if (rect.intersects(division)) {
            range(n.left, rect, bag);
            range(n.right, rect, bag);
        } 
        else if (comp > 0) 
            // not intersects and node key is greater than arbitrary point 
            // in rect: only search left.
            range(n.left, rect, bag);
        else if (comp < 0) 
            // not intersects and node key is lesser than arbitrary point 
            // in rect: only search right.
            range(n.right, rect, bag);
        
    }

    public Point2D nearest(Point2D p) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        RectHV queryRect = new RectHV(0.1, 0.2, 0.6, 0.8);
        double[][] points = {
            {0.7, 0.2}, {0.5, 0.4}, {0.2, 0.3}, {0.4, 0.7}, {0.9, 0.6}
        };

        for (int i = 0; i< points.length; i++) 
            kd.insert(new Point2D(points[i][0], points[i][1]));

        kd.draw();
        queryRect.draw();

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.WHITE);

        for (Point2D p: kd.range(queryRect)) p.draw();
    }

}

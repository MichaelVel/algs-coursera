package mickvel;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ResizingArrayBag;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean Y = true;
    private static final boolean X = false;

    private Node root;     // root of the BST
                           //
    private class Node {
        private Point2D val;
        private RectHV nRect;
        private Node left, right;  
        private RectHV leftR, rightR;  
        private boolean direction;
        private int size;          

        public Node(Point2D p, RectHV rect, boolean direction, int size) {
            this.val = p;
            this.nRect = rect;
            this.size = size;
            this.direction = direction;
            leftR = direction == Y 
                ? new RectHV(nRect.xmin(), nRect.ymin(), val.x(), nRect.ymax())
                : new RectHV(nRect.xmin(), nRect.ymin(), nRect.xmax(), val.y());
            rightR = direction == Y 
                ? new RectHV(val.x(), nRect.ymin(), nRect.xmax(), nRect.ymax())
                : new RectHV(nRect.xmin(), val.y(), nRect.xmax(), nRect.ymax());
        }

        public double key() {
            return direction == Y ? val.x() : val.y();
        }

        public int compareToKey(Point2D p) {
            if (p.compareTo(val) == 0) return 0;
            double pKey = direction == Y ? p.x() : p.y();
            if (key() < pKey) return -1;  // this node key lesser than p
            else return 1;                // this node key greater than p
        }

        public RectHV leftRect() { return leftR;}
        public RectHV rightRect() { return rightR; }

        public Node[] nodes(Point2D p) {
            if (left == null && right == null) return null;
            if (left == null) return new Node[] {right};
            if (right == null) return new Node[] {left};
            
            return leftRect().contains(p) 
                ? new Node[] {left, right}
                : new Node[] {right, left};
        }
        
        public String toString() {
            String dir = this.direction ? "vertical" : "horizontal";
            String childs = String.format(
                    "left: %s, right: %s", 
                    left != null ? left.val : "None",
                    right != null ? right.val : "None");

            return String.format(
            "Point %s, with direction %s. childs: %s", val, dir, childs);
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
        root = insert(root, p, new RectHV(0, 0, 1, 1), Y);
    }

    private Node insert(Node x, Point2D p, RectHV rect, boolean direction) {
        if (x == null) return new Node(p, rect, direction, 1);

        int cmp = x.compareToKey(p);
        if  (cmp > 0) 
            x.left  = insert(x.left, p, x.leftRect(), !direction);
        else if (cmp < 0) 
            x.right = insert(x.right, p, x.rightRect(), !direction);

        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p) != null;
    }

    public void draw() { draw(root); }

    private void draw(Node n) {
        if (n == null) return; 

        Point2D p = n.val;
        RectHV rect = n.direction == Y
            ? new RectHV(p.x(), n.nRect.ymin(), p.x(), n.nRect.ymax())
            : new RectHV(n.nRect.xmin(), p.y(), n.nRect.xmax(), p.y());

        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(n.direction == Y ? StdDraw.RED : StdDraw.BLUE);

        rect.draw();

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);

        p.draw();

        draw(n.left);
        draw(n.right);
        
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ResizingArrayBag<Point2D> bag = new ResizingArrayBag<>();
        range(root, rect, bag); 
        return bag;
    }

    private void range(Node n, RectHV rect, ResizingArrayBag<Point2D> bag) {
        if (n == null) return;

        Point2D p = n.val;
        if (rect.contains(p)) bag.add(p);

        if (rect.intersects(n.leftRect())) range(n.left, rect, bag);
        if (rect.intersects(n.rightRect())) range(n.right, rect, bag);
    }

    private Point2D minPoint(Point2D p, Point2D c1, Point2D c2) {
        // nearest point of p between c1, and c2
        if (c2 == null) return c1;
        return p.distanceSquaredTo(c2) < p.distanceSquaredTo(c1) ? c2 : c1;
    }

    private Point2D nearestInNode(Node n, Point2D p) {
        if (n == null) return null;

        Point2D champion = n.val;
        Node[] nodes = n.nodes(p); // child nodes sorted by distance to p

        // if the node has no childrens return current champion
        if (nodes == null) return champion;

        // if node only have one child return the nearest between then
        // current champion and the childs champion.
        if (nodes.length == 1) 
            return  minPoint(p,champion,nearestInNode(nodes[0], p));

        Point2D candidate = nearestInNode(nodes[0], p);
        champion = minPoint(p, champion, candidate);

        if (champion.distanceSquaredTo(p) < nodes[1].nRect.distanceSquaredTo(p))
            // if the distance between the node rect to the point is 
            // greater than the distance between the point and the champion 
            // prune the search.
            return champion;

        candidate = nearestInNode(nodes[1], p);

        return minPoint(p, champion, candidate);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearestInNode(root, p);
    }

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        RectHV queryRect = new RectHV(0.1, 0.2, 0.6, 0.8);
        Point2D queryPoint = new Point2D(0.3, 0.2);
        double[][] points = {
            {0.7, 0.2}, {0.5, 0.4}, {0.2, 0.3}, {0.4, 0.7}, {0.9, 0.6}
        };

        for (int i = 0; i < points.length; i++) 
            kd.insert(new Point2D(points[i][0], points[i][1]));

        kd.draw();
        queryRect.draw();
        
        StdDraw.setPenColor(StdDraw.YELLOW);
        queryPoint.draw();

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.WHITE);

        for (Point2D p: kd.range(queryRect)) p.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        kd.nearest(queryPoint).draw();
    }

}

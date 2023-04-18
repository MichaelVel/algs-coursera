package mickvel;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        set.add(p);
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p: set) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Bag<Point2D> points = new Bag<>();
        for (Point2D p: set) if (rect.contains(p)) points.add(p);

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;

        Point2D nearest = null;
        for (Point2D lP: set) {
            double distToPoint = lP.distanceSquaredTo(p);
            if (nearest == null || distToPoint < nearest.distanceSquaredTo(p)) 
                nearest = lP;
        }

        return nearest;

    }

    public static void main(String[] args) {
        PointSET pSet = new PointSET(); 

        assert pSet.isEmpty();

        for (int i = 0; i <= 10; i++) {
            double coord = 0.1*i;
            pSet.insert(new Point2D(coord, coord));
        }

        assert pSet.size() == 11;

        for (int i = 0; i <= 10; i++) {
            double coord = 0.1*i;
            assert pSet.contains(new Point2D(coord,coord));
        }

        assert pSet.size() == 11;

        Point2D p = new Point2D(0.5, 0.55);
        RectHV square = new RectHV(0.0, 0.0, 0.5, 0.5);
        
        StdOut.print(pSet.nearest(p));
        assert pSet.nearest(p).equals(new Point2D(0.5,0.5));
        
        p.draw();
        square.draw();
        pSet.draw();

        StdDraw.setPenColor(StdDraw.RED);
        for (Point2D rP: pSet.range(square)) rP.draw();
    }

}

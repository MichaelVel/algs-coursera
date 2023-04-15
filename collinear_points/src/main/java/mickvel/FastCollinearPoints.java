package mickvel;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        checkInput(points);

        ArrayList<LineSegment> res = new ArrayList<>();
        Point[] temp = points.clone();

        // loop through points in backup array, and sort the temp points array
        for (Point p : points) {
            Arrays.sort(temp, p.slopeOrder());
            findSegments(temp, p, res);
        }

        segments = res.toArray(new LineSegment[res.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    private void checkInput(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();

        Point[] localPoints = points.clone();

        for (int i = 0; i < localPoints.length; i++) 
            if (points[i] == null) 
                throw new IllegalArgumentException();

        Arrays.sort(localPoints);
        Point current = points[0];
        for (int i = 1; i < localPoints.length; i++) {
            if (current.compareTo(points[i]) == 0) 
                throw new IllegalArgumentException();
            current = points[i];
        }
    }

    private void findSegments(Point[] points, Point p, ArrayList<LineSegment> res) {
        // start from position 1, since position 0 will be the point p itself
        int start = 1;
        double slop = p.slopeTo(points[1]);

        for (int i = 2; i < points.length; i++) {
            double tempSlop = p.slopeTo(points[i]);
            if (!collinearSlop(tempSlop, slop)) {
                // check to see whether there have already 3 equal points
                if (i - start >= 3) {
                    Point[] ls = genSegment(points, p, start, i);
                    /**
                     * Important Point: only add line segment which starts form point p to avoid
                     * duplicate
                     */
                    if (ls[0].compareTo(p) == 0) {
                        res.add(new LineSegment(ls[0], ls[1]));
                    }
                }
                // update
                start = i;
                slop = tempSlop;
            }
        }
        // situation when the last several points in the array are collinear
        if (points.length - start >= 3) {
            Point[] lastPoints = genSegment(points, p, start, points.length);
            if (lastPoints[0].compareTo(p) == 0) {
                res.add(new LineSegment(lastPoints[0], lastPoints[1]));
            }
        }
    }

    private boolean collinearSlop(double tempSlop, double slop) {
        return Double.compare(slop, tempSlop) == 0;
    }

    private Point[] genSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(p);
        for (int i = start; i < end; i++) {
            temp.add(points[i]);
        }
        temp.sort(null);
        return new Point[] { temp.get(0), temp.get(temp.size() - 1) };
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

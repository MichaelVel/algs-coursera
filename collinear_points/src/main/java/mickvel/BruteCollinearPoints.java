package mickvel;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private Object[] pSegs;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        checkInput(points);

        int pLen = points.length;
        int sId = 0;
        pSegs = new LineSegment[pLen];
            
        for (int i = 0; i<pLen; i++) 
          for (int j = i+1; j<pLen; j++) 
            for (int k = j+1; k<pLen; k++) 
              for (int m = k+1; m<pLen; m++) {
                Point p = points[i];
                Point q = points[j];
                Point r = points[k];
                Point s = points[m];

                if (p.slopeTo(q)== p.slopeTo(r) && p.slopeTo(r) == p.slopeTo(s)) {
                  Point[] tuple = new Point[] {p,q,r,s};
                  Arrays.sort(tuple);
                  
                  pSegs[sId++] = new LineSegment(tuple[0], tuple[3]);

                  if (sId == pSegs.length) {
                      resize(sId * 2);
                  }

                }
             }

        segments = new LineSegment[sId];

        for (int i = 0; i < sId; i++) 
            segments[i] = (LineSegment) pSegs[i];

    }

    private void resize(int capacity) {
        Object[] temp = new Object[capacity];
        System.arraycopy(pSegs, 0, temp, 0, pSegs.length);
        pSegs = temp;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

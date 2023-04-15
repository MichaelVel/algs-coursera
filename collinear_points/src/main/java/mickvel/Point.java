package mickvel;

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

	@Override
	public int compareTo(Point other) {
        if (this.y < other.y ) return -1;
        if (this.y == other.y && this.x < other.x ) return -1;
        if (this.y == other.y && this.x == other.x) return 0;
		return 1;
	}

    public double slopeTo(Point other) {
        if (this.compareTo(other) == 0 ) return Double.NEGATIVE_INFINITY;
        if (this.x == other.x) return Double.POSITIVE_INFINITY;
        if (this.y == other.y) return 0.0;

        return ((double)(other.y - this.y))/(other.x - this.x);
    }

    private static class PointComparator<T> implements Comparator<Point> {
        Point point0;

        PointComparator(Point point) {
            this.point0 = point;
        }

		@Override
		public int compare(Point point1, Point point2) {
            double slopeTo1 = point0.slopeTo(point1);
            double slopeTo2 = point0.slopeTo(point2);

            if (slopeTo1 < slopeTo2) return -1;
            if (slopeTo1 > slopeTo2) return 1;
			return 0;
		}
    }

    public Comparator<Point> slopeOrder() {
        return new PointComparator<>(this);
    }


    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}

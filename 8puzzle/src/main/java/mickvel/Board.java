package mickvel;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int N;
    private final int[][] tiles;
    private int[] emptyCell;
    private int[] tile1;
    private int[] tile2;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) 
            throw new IllegalArgumentException();

        N = tiles.length;
        this.tiles = new int[N][N];

        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) 
                    emptyCell = new int[] {i,j};

                this.tiles[i][j] = tiles[i][j];
            }
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                str.append(String.format("%2d ", tiles[i][j]));
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) 
                if (tiles[i][j] != (N * i) + j + 1) count++;

        count--;
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                int cellVal = tiles[i][j];
                if (cellVal == 0) continue;
                sum += dist_manhattan(i, j, (cellVal-1)/N, (cellVal-1)%N);
            }

        return sum;
    }

    private int dist_manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) 
            return false;

        Board other = (Board) y;
        if (other.N != N) return false;
        return Arrays.deepEquals(this.tiles, other.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        
        if (emptyCell[0] > 0) {
            Board upN = new Board(tiles);
            upN.emptyCell[0] = emptyCell[0] - 1;
            upN.exchange(upN.tiles, emptyCell, upN.emptyCell);
            stack.push(upN);
        }

        if (emptyCell[0] < N-1) {
            Board downN = new Board(tiles);
            downN.emptyCell[0] = emptyCell[0] + 1;
            downN.exchange(downN.tiles, emptyCell, downN.emptyCell);
            stack.push(downN);
        }

        if (emptyCell[1] > 0) {
            Board leftN = new Board(tiles);
            leftN.emptyCell[1] = emptyCell[1] - 1;
            leftN.exchange(leftN.tiles, emptyCell, leftN.emptyCell);
            stack.push(leftN);
        }

        if (emptyCell[1] < N-1) {
            Board rightN = new Board(tiles);
            rightN.emptyCell[1] = emptyCell[1] + 1;
            rightN.exchange(rightN.tiles, emptyCell, rightN.emptyCell);
            stack.push(rightN);
        }

        return stack;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(tiles);
        
        if (tile1 == null || tile2 == null) {
            tile1 = randomTile();
            tile2 = randomTile();

            while (Arrays.equals(tile1, tile2)) 
                tile2 = randomTile();
        }

        exchange(twin.tiles, tile1, tile2);

        return twin;

    }

    private int[] randomTile() {
        int i = StdRandom.uniformInt(N);
        int j = StdRandom.uniformInt(N);
        int[] tile = new int[] {i,j};
        while (Arrays.equals(emptyCell,tile)) {
            i = StdRandom.uniformInt(N);
            j = StdRandom.uniformInt(N);
            tile = new int[] {i,j};
        }
        return tile;
    }

    private void exchange(int[][] arr, int[] a, int[] b) {
        int temp = arr[a[0]][a[1]];
        arr[a[0]][a[1]] = arr[b[0]][b[1]];
        arr[b[0]][b[1]] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        for (String filename : args) {
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
             }

            Board initial = new Board(tiles);
            Board initialCopy = new Board(tiles);
            Board twin = initial.twin();

            StdOut.println("Initial");
            StdOut.println(initial);
            StdOut.println("Hamming: " + initial.hamming());
            StdOut.println("Manhattan: " + initial.manhattan());
            StdOut.println("A == A(copy)? " + initial.equals(initialCopy));
            StdOut.println("A == A(twin)? " + initial.equals(twin));

            StdOut.println("Twin");
            StdOut.println(twin);
            StdOut.println("Hamming: " + twin.hamming());
            StdOut.println("Manhattan: " + twin.manhattan());

            for (Board b: initial.neighbors()) {
                StdOut.println(b);
            }
        }
    }

}

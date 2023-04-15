package com.mickvel;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int nOpen = 0;
    private int n;
    private WeightedQuickUnionUF paths;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();
        grid = new boolean[n+1][n+1];
        paths = new WeightedQuickUnionUF((n*n)+2);
        this.n = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);

        if (isOpen(row, col)) return;
        
        ++nOpen;
        grid[row][col] = true;
        int lP = linearPos(row, col);

        if (row != 1 && isOpen(row-1, col)) {
            int lP2 = linearPos(row-1, col);
            paths.union(lP,lP2);
        }

        if (row != n && isOpen(row+1, col)) {
            int lP2 = linearPos(row+1, col);
            paths.union(lP,lP2);
        }

        if (col != 1 && isOpen(row, col-1)) {
            int lP2 = linearPos(row, col-1);
            paths.union(lP,lP2);
        }

        if (col != n && isOpen(row, col+1)) {
            int lP2 = linearPos(row, col+1);
            paths.union(lP,lP2);
        }

        if (row == 1) {
            paths.union(0,lP);
        }
        
        if (row == n) {
            paths.union((n*n)+1,lP);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return paths.find(linearPos(row, col)) == paths.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return paths.find((n*n)+1) == paths.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {
    }

    private int linearPos(int row, int col) {
        return ( (row-1) * n ) + col;
    }

    private void checkRange(int row, int col) {
        if (row < 1 || col < 1 || row > n || col > n)
            throw new IllegalArgumentException();
    }

}

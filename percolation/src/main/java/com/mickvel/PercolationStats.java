package com.mickvel;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trials;
    private double mean;
    private double stddev;
    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        this.trials = new double[trials];
        this.n = n;

        for (int i = 0; i < trials; i++) {
            this.trials[i] = trial(new Percolation(n));
        }

        mean = StdStats.mean(this.trials);
        stddev = StdStats.stddev(this.trials);
        
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean - ((1.96*this.stddev)/Math.sqrt(trials.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean + ((1.96*this.stddev)/Math.sqrt(trials.length));
    }

   // test client (see below)
   public static void main(String[] args) {
       int n = Integer.parseInt(args[0]);
       int trials = Integer.parseInt(args[1]);

       PercolationStats stats = new PercolationStats(n, trials);
       StdOut.printf("mean                    = %s\n", stats.mean);
       StdOut.printf("stddev                  = %s\n", stats.stddev);
       StdOut.printf("95%% confidence interval = [%s, %s]\n", 
               stats.confidenceLo(), stats.confidenceHi());
   }

   private double trial(Percolation per) {
       while (!per.percolates()) {
           int randX = StdRandom.uniformInt(1, n+1);
           int randY = StdRandom.uniformInt(1, n+1);
           per.open(randX, randY);
       }
       
       return per.numberOfOpenSites() / (double) (n*n);
   }

}

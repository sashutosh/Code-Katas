import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double CONFIDENCE_95 = 1.96;
    private final int trialCount;
    private final double[] openSitesPerTrial;
    private double meanValue;
    private double stdDevValue;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if(n<=0 || trials <=0)
            throw new IllegalArgumentException("Wrong argument values");

        openSitesPerTrial = new double[trials];
        trialCount = trials;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            openSitesPerTrial[i] = ((double) percolation.numberOfOpenSites() / (double) (n * n));
        }

    }

    // test client (see below)
    public static void main(String[] args) {

        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        try {
            int n = Integer.parseInt(args[0]);
            int trial = Integer.parseInt(args[1]);
            PercolationStats percolationStats = new PercolationStats(n, trial);
            StdOut.printf("mean                      = %f", percolationStats.mean());
            StdOut.println();
            StdOut.printf("stdDev                    = %f", percolationStats.stddev());
            StdOut.println();
            StdOut.print("95% confidence interval    =");
            StdOut.printf("[%f, %f]", percolationStats.confidenceLo(), percolationStats.confidenceHi());
        } catch (IllegalArgumentException ex) {
            System.out.println("Usage is not right -number, -trials");
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        meanValue = StdStats.mean(openSitesPerTrial);
        return meanValue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stdDevValue = StdStats.stddev(openSitesPerTrial);
        return stdDevValue;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return (mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return (mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trialCount));
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int size;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF wQUF;
    private int openSites;

    public Percolation(int n) {
        if(n<=0)
            throw new IllegalArgumentException("Incorrect grid dimension");
        size = n;
        grid = new boolean[size][size];
        wQUF = new WeightedQuickUnionUF(n * n + 2);
        openSites = 0;
        initGrid();
    }

/*    // test client (optional)
    public static void main(String[] args) {

    }*/

    private void initGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = false;
            }
        }


        int beginIndex = (size - 1) * size + 1;
        int lastIndex = (size * size) + 1;
        for (int i = 0; i < size; i++) {

            // Union top row
            wQUF.union(0, i + 1);

            // Union bottom row
            wQUF.union(lastIndex, beginIndex + i);
        }


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row, col);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            unionNeighbouringOpenSite(row, col);
            openSites++;
        }
    }

    private void unionNeighbouringOpenSite(int row, int col) {
        // top
        if (isValidIndex(row - 1, col)) {
            if (isOpen(row - 1, col)) {
                wQUF.union(getIndex(row, col), getIndex(row - 1, col));
            }
        }
        // bottom
        if (isValidIndex(row + 1, col)) {
            if (isOpen(row + 1, col)) {
                wQUF.union(getIndex(row, col), getIndex(row + 1, col));
            }
        }
        // left
        if (isValidIndex(row, col - 1)) {
            if (isOpen(row, col - 1)) {
                wQUF.union(getIndex(row, col), getIndex(row, col - 1));
            }
        }
        // right
        if (isValidIndex(row, col + 1)) {
            if (isOpen(row, col + 1)) {
                wQUF.union(getIndex(row, col), getIndex(row, col + 1));
            }
        }
    }

    private int getIndex(int i, int col) {
        return (i - 1) * size + col;
    }

    private void validateIndex(int row, int col) {
        if (!isValidIndex(row, col)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidIndex(int row, int col) {
        return row <= size && col <= size && row >= 1 && col >= 1;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndex(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        return isOpen(row, col)? wQUF.find(0)==wQUF.find(getIndex(row,col)):false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        /* if (!isRowClosed(1)) {
            isRowClosed(size);
        } */
        return wQUF.find(0)== wQUF.find(size * size + 1);
    }

    /* private boolean isRowClosed(int i) {
        for (int col = 1; col < size + 1; col++) {
            if (isOpen(i, col)) {
                return false;
            }
        }
        return true;
    } */


}

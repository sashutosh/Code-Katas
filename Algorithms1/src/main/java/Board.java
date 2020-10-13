import edu.princeton.cs.algs4.StdRandom;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {

    private final int[][] board;
    private final int dimension;
    private final int hamming;
    private final int manhattan;
    private int numberToSwap1;
    private int numberToSwap2;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        board = copyBoard(tiles);
        getGoalBoard(dimension);
        getTwinNumbers();
        hamming = calculateHamming();
        manhattan = calculateManhattan();
    }

    private int[][] copyBoard(int[][] tiles) {

        int[][] tempBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tempBoard[i][j] = tiles[i][j];
            }
        }
        return tempBoard;
    }

    private int[][] getGoalBoard(int dimension) {
        int n = 1;
        int[][] goalBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                goalBoard[i][j] = n++;
            }
        }
        goalBoard[dimension - 1][dimension - 1] = 0;
        return goalBoard;
    }


    // string representation of this board
    public String toString() {

        String boardStr = "";
        boardStr += dimension + "\n";

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardStr = boardStr + board[i][j] + " ";
            }
            boardStr += "\n";
        }
        return boardStr;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    private int calculateHamming() {
        int hamming = 0;
        int[][] goalBoard = getGoalBoard(dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (goalBoard[i][j] != 0 && goalBoard[i][j] != board[i][j]) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    private int calculateManhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int val = board[i][j];
                if (val != 0) {
                    manhattan += getDistance(val, i, j);
                }
            }
        }
        return manhattan;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }


    private int getDistance(int val, int i, int j) {
        int row, col;
        row = ((val - 1) / dimension);
        col = (val - 1) % dimension;
        return Math.abs(row - i) + Math.abs(col - j);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.equals(new Board(getGoalBoard(dimension)));
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this == that) {
            return true;
        }
        if (this.dimension != that.dimension) {
            return false;
        }
        return contentEqual(this.board, that.board);
    }

    private boolean contentEqual(int[][] board, int[][] otherBoard) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != otherBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return () -> new BoardNeighbourIterator();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = copyBoard(this.board);
        AbstractMap.SimpleEntry<Integer, Integer> index1 = getIndexOf(numberToSwap1);
        AbstractMap.SimpleEntry<Integer, Integer> index2 = getIndexOf(numberToSwap2);
        twinBoard[index2.getKey()][index2.getValue()] = numberToSwap1;
        twinBoard[index1.getKey()][index1.getValue()] = numberToSwap2;
        return new Board(twinBoard);
    }

    private void getTwinNumbers() {
        int maxNum = dimension * dimension - 1;
        numberToSwap1 = StdRandom.uniform(1, maxNum);
        numberToSwap2 = StdRandom.uniform(1, maxNum);

        while (numberToSwap2 == numberToSwap1) {
            numberToSwap2 = StdRandom.uniform(maxNum);
        }
    }

    private AbstractMap.SimpleEntry<Integer, Integer> getIndexOf(int numberToSwap1) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == numberToSwap1) {
                    return new AbstractMap.SimpleEntry<>(i, j);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(0, 0);
    }

    private class BoardNeighbourIterator implements Iterator<Board> {
        int current = 0;
        AbstractMap.SimpleEntry<Integer, Integer> emptyNodeIndex;
        List<int[][]> neighbours = getNeighbours();

        private List<int[][]> getNeighbours() {
            emptyNodeIndex = getEmptyNodeIndex();
            List<AbstractMap.SimpleEntry<Integer, Integer>> nIndexes = getNIndexes(emptyNodeIndex);
            return getNeighboursFromIndexes(nIndexes);

        }

        private List<int[][]> getNeighboursFromIndexes(List<AbstractMap.SimpleEntry<Integer, Integer>> nIndexes) {
            List<int[][]> neighbours = new ArrayList<>();
            for (AbstractMap.SimpleEntry<Integer, Integer> index : nIndexes) {
                int row = index.getKey();
                int col = index.getValue();
                int val = board[row][col];
                int[][] tempBoard = copyBoard(board);
                tempBoard[emptyNodeIndex.getKey()][emptyNodeIndex.getValue()] = val;
                tempBoard[row][col] = 0;
                neighbours.add(tempBoard);
            }
            return neighbours;
        }

        private List<AbstractMap.SimpleEntry<Integer, Integer>> getNIndexes(AbstractMap.SimpleEntry<Integer, Integer> index) {
            List<AbstractMap.SimpleEntry<Integer, Integer>> indexes = new ArrayList<>();
            if (index.getKey() - 1 >= 0) {
                indexes.add(new AbstractMap.SimpleEntry<>(index.getKey() - 1, index.getValue()));
            }
            if (index.getKey() + 1 < dimension) {
                indexes.add(new AbstractMap.SimpleEntry<>(index.getKey() + 1, index.getValue()));
            }
            if (index.getValue() - 1 >= 0) {
                indexes.add(new AbstractMap.SimpleEntry<>(index.getKey(), index.getValue() - 1));
            }
            if (index.getValue() + 1 < dimension) {
                indexes.add(new AbstractMap.SimpleEntry<>(index.getKey(), index.getValue() + 1));
            }

            return indexes;

        }

        private AbstractMap.SimpleEntry<Integer, Integer> getEmptyNodeIndex() {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] == 0) {
                        return new AbstractMap.SimpleEntry<>(i, j);
                    }
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return current < neighbours.size();
        }

        @Override
        public Board next() {
            return new Board(neighbours.get(current++));
        }
    }

}
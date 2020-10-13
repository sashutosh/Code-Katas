import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BoardTest {

    private Board getBoard(String puzzle) {
        In in = new In(puzzle);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        return initial;
    }

    @Test
    public void testHamming() {
        int[][] board = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board1 = new Board(board);
        assertEquals(5, board1.hamming());

    }

    @Test
    public void testManhattan() {
        int[][] board = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board1 = new Board(board);
        assertEquals(10, board1.manhattan());

    }

    @Test
    public void testNeighbours() {
        int[][] board = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board1 = new Board(board);
        Iterable<Board> neighbors = board1.neighbors();
        List<Board> boardList = new ArrayList<>();
        for (Board current : neighbors) {
            boardList.add(current);
        }
        assertEquals(3, boardList.size());
    }

    @Test
    public void testEquality() {
        int[][] board = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board1 = new Board(board);
        Board board2 = new Board(board);
        assertEquals(true, board1.equals(board2));
    }

    @Test
    public void testNeighbours1() {
        int[][] board = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board1 = new Board(board);
        List<Board> boardList = new ArrayList<>();
        for (Board current : board1.neighbors()) {
            boardList.add(current);
        }
        assertEquals(4, boardList.size());
    }

    @Test
    public void testTwin() {
        int[][] board = {{2, 0}, {3, 1}};
        Board board1 = new Board(board);
        Board twin = board1.twin();
        assertNotNull(twin);
    }

    @Test
    public void testTwinPuzzle04() {
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle04.txt");
        Board twin = initial.twin();
        assertNotNull(twin);
    }

}

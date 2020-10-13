import edu.princeton.cs.algs4.In;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolverTest {
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
    public void testDemoBoardIsSolvable(){
        int[][] board = {{0,1,3},{4,2,5},{7,8,6}};
        Board board1 = new Board(board);
        Solver solver = new Solver(board1);
        assertEquals(true,solver.isSolvable());
    }

    @Test
    public void testSimplestBoardIsSolvable(){
        int[][] board = {{1,2,3},{4,5,6},{7,0,8}};
        Board board1 = new Board(board);
        Solver solver = new Solver(board1);
        assertEquals(true,solver.isSolvable());
    }

    @Test
    public void testGoalBoardIsSolvable(){
        int[][] board = {{1,2,3},{4,5,6},{7,8,0}};
        Board board1 = new Board(board);
        Solver solver = new Solver(board1);
        assertEquals(true,solver.isSolvable());
    }
    @Test
    public void testUnSolvableBoard(){
        int[][] board = {{1,2,3},{4,5,6},{8,7,0}};
        Board board1 = new Board(board);
        Solver solver = new Solver(board1);
        assertEquals(false,solver.isSolvable());
    }

    @Test
    public void test2x2Board(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle2x2-03.txt");
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
    }

    @Test
    public void testComplexBoard(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle04.txt");

        // solve the puzzle
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
        assertEquals(4, solver.moves());
    }


    @Test
    public void test4x4Board(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle4x4-01.txt");
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
    }

    @Test
    public void testPuzzle07(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle07.txt");
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
        assertEquals(7,solver.moves());
    }

    /*@Test
    public void testPuzzle11(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle11.txt");
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
        assertEquals(11,solver.moves());
    }*/

    @Test
    public void testPuzzle00(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle00.txt");
        Solver solver = new Solver(initial);
        assertEquals(true,solver.isSolvable());
        assertEquals(0,solver.moves());
    }

    @Test
    public void test4x4BoardUnsolvable(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle4x4-unsolvable.txt");
        Solver solver = new Solver(initial);
        assertEquals(false,solver.isSolvable());
    }


    @Test
    public void testSolution(){
        Board initial = getBoard("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\puzzle08.txt");
        Solver solver = new Solver(initial);
        assertEquals(8,solver.moves());
        for (Board board :
                solver.solution()) {
            System.out.println(board);
        }
    }

}

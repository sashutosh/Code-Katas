import edu.princeton.cs.algs4.In;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testng.annotations.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParametrizedSolverTest extends TestCase {

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

    @Parameterized.Parameters
    public static Collection<Object[]> parameters(){
        return Arrays.asList(new Object[][] {
                {"puzzle01.txt",1},
                {"puzzle02.txt",2},
                {"puzzle03.txt",3},
                {"puzzle04.txt",4},
                {"puzzle05.txt",5},
                {"puzzle06.txt",6},
                {"puzzle07.txt",7},
                {"puzzle08.txt",8},
                {"puzzle09.txt",9},
                {"puzzle10.txt",10},
                {"puzzle11.txt",11},
                {"puzzle12.txt",12},
                {"puzzle13.txt",13},
                {"puzzle14.txt",14},
                {"puzzle15.txt",15},
                {"puzzle16.txt",16}


        });
    }

    private String inputFileName;
    private int expectedMoves;

    public ParametrizedSolverTest(String inputFileName, int expectedMoves) {
        this.expectedMoves=expectedMoves;
        this.inputFileName=inputFileName;
    }

    @Test
    public void testSolverBoards(){
        String fullPath = "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\8puzzle\\" + inputFileName;
        Board board = getBoard(fullPath);
        Solver solver = new Solver(board);
        assertEquals(true,solver.isSolvable());
        assertEquals(expectedMoves,solver.moves());

    }

}

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private final MinPQ<ABoard> boardMinPQOriginal;
    private final MinPQ<ABoard> boardMinPQAlternate;
    private final List<MinPQ<ABoard>> pqList;
    private final Stack<Board> moves = new Stack<>();
    private final int ALTERNATE = 1;
    private final int ORIGINAL = 0;
    private boolean isSolvable = false;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null or empty board");
        }

        Comparator<ABoard> manhattanComparator = new ManhattanComparator();
        boardMinPQOriginal = new MinPQ<>(manhattanComparator);
        boardMinPQAlternate = new MinPQ<>(manhattanComparator);
        boardMinPQOriginal.insert(new ABoard(initial));
        boardMinPQAlternate.insert(new ABoard(initial.twin()));

        pqList = new ArrayList<>();
        pqList.add(boardMinPQOriginal);
        pqList.add(boardMinPQAlternate);

        if (BoardResult.SOLVED == solveBoard()) {
            isSolvable = true;
        }

    }

    private BoardResult solveBoard() {
        while (true) {
            if (pqList.get(ORIGINAL).isEmpty()) {
                return BoardResult.UNSOLVABLE;
            }
            List<ABoard> nextNodes = getNextNodes();
            BoardResult result = checkSolved(nextNodes);
            if (result == BoardResult.SOLVED || result == BoardResult.UNSOLVABLE) {
                if (result == BoardResult.SOLVED) {
                    generatePath(nextNodes.get(ORIGINAL));
                }
                return result;
            } else {
                addChildrenToPQ(nextNodes.get(ORIGINAL), ORIGINAL);
                addChildrenToPQ(nextNodes.get(ALTERNATE), ALTERNATE);
            }

        }
    }

    private void generatePath(ABoard aBoard) {
        ABoard current = aBoard;
        while (current != null) {
            moves.push(current.getBoard());
            current = current.getParent();
        }
    }

    private List<ABoard> getNextNodes() {
        List<ABoard> nextNodes = new ArrayList<>();
        if (!pqList.get(ORIGINAL).isEmpty()) {
            ABoard nextNode = pqList.get(ORIGINAL).delMin();
            nextNodes.add(nextNode);
        }
        if (!pqList.get(ALTERNATE).isEmpty()) {
            nextNodes.add(pqList.get(ALTERNATE).delMin());
        }
        return nextNodes;
    }

    private void addChildrenToPQ(ABoard current, int index) {
        int parentMoves = current.getMoves() + 1;
        //clearPQ(pqList.get(index));
        for (Board child : current.getBoard().neighbors()) {

            if (!isAlreadyVisited(child, current)) {
                ABoard childBoard = new ABoard(child);
                childBoard.setMoves(parentMoves);
                childBoard.setParent(current);
                pqList.get(index).insert(childBoard);
            }
        }
    }

    private BoardResult checkSolved(List<ABoard> nextNodes) {
        if (nextNodes.get(0).getBoard().isGoal()) {
            return BoardResult.SOLVED;
        }
        if (nextNodes.get(1) != null && nextNodes.get(1).getBoard().isGoal()) {
            return BoardResult.UNSOLVABLE;
        }
        return BoardResult.NOT_GOAL;
    }

    private boolean isAlreadyVisited(Board child, ABoard parent) {

        ABoard gp = parent.getParent();
        if (gp == null) {
            return false;
        } else {
            return gp.getBoard().equals(child);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) {
            return -1;
        }
        return moves.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        } else {
            return () -> moves.iterator();
        }
    }

    private enum BoardResult {
        SOLVED,
        UNSOLVABLE,
        NOT_GOAL
    }

    private static class HammingComparator implements Comparator<Board> {
        @Override
        public int compare(Board o1, Board o2) {
            if (o1.hamming() < o2.hamming()) {
                return -1;
            } else if (o1.hamming() > o2.hamming()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static class ManhattanComparator implements Comparator<ABoard> {
        @Override
        public int compare(ABoard o1, ABoard o2) {
            if (o1.heuristics() >= o2.heuristics()) {
                if (o1.heuristics() > o2.heuristics()) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        }
    }

    private static class ABoard {

        private final Board board;
        private int moves;
        private ABoard parent;

        public ABoard(Board board) {
            this.board = board;
            moves = 0;
        }

        public int getMoves() {
            return moves;
        }

        public void setMoves(int moves) {
            this.moves = moves;
        }

        public ABoard getParent() {
            return parent;
        }

        public void setParent(ABoard parent) {
            this.parent = parent;
        }

        public int heuristics() {
            return board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

    }

}
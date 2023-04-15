package mickvel;

import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final int moves;
    private final ResizingArrayStack<Board> boards;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        SearchNode initialNode = new SearchNode(initial, null, 0);
        SearchNode twinNode = new SearchNode(initial.twin(), null, 0);

        Comparator<SearchNode> manhattanComparator = manhattanPriorityFunction();

        MinPQ<SearchNode> initialMinPQ = new MinPQ<>(manhattanComparator);
        MinPQ<SearchNode> twinMinPQ = new MinPQ<>(manhattanComparator);

        initialMinPQ.insert(initialNode);
        twinMinPQ.insert(twinNode);

        while (!initialNode.boardIsGoal() && !twinNode.boardIsGoal()) {
            initialNode = initialMinPQ.delMin();
            twinNode = twinMinPQ.delMin();

            for (Board b: initialNode.board.neighbors()) {
                if (initialNode.prev != null && initialNode.prev.board.equals(b)) 
                    continue;

                initialMinPQ.insert(
                    new SearchNode(b, initialNode, initialNode.nMoves + 1));
            }

            for (Board b: twinNode.board.neighbors()) {
                if (twinNode.prev != null && twinNode.prev.board.equals(b)) 
                    continue;

                twinMinPQ.insert(
                    new SearchNode(b, twinNode, twinNode.nMoves + 1));
            }
        }

        

        if (!initialNode.boardIsGoal()) {
            moves = -1;
            boards = null;
        } else {
            ResizingArrayStack<Board> localBoards = new ResizingArrayStack<>();
            while (initialNode != null) {
                localBoards.push(initialNode.board);
                initialNode = initialNode.prev;
            }

            boards = localBoards;
            moves = boards.size();
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? moves - 1 : -1;
    }

    public Iterable<Board> solution() {
        return boards == null ? null : boards;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
    private class SearchNode {
        Board board;
        SearchNode prev;
        int nMoves;
        int manhattan;
        int hamming;

        SearchNode(Board b, SearchNode p, int n) {
            prev = p;
            board = b;
            nMoves = n;
            manhattan = b.manhattan();
            hamming = b.hamming();
        }

        boolean boardIsGoal() {
            return hamming == 0;
        }
    }
    
    private Comparator<SearchNode> hammingPriorityFunction() {
        return new Comparator<SearchNode>() { 
            public int compare(SearchNode a, SearchNode b) {
                Integer aPriority = a.nMoves + a.hamming;
                Integer bPriority = b.nMoves + b.hamming;
                return aPriority.compareTo(bPriority);
            }
        };
    }

    private Comparator<SearchNode> manhattanPriorityFunction() {
        return new Comparator<SearchNode>() { 
            public int compare(SearchNode a, SearchNode b) {
                Integer aPriority = a.nMoves + a.manhattan;
                Integer bPriority = b.nMoves + b.manhattan;
                return aPriority.compareTo(bPriority);
            }
        };
    }
}


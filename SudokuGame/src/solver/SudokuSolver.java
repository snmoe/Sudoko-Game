package solver;

import exceptions.InvalidGame;
import model.Game;
import java.util.ArrayList;

public class SudokuSolver {

    private FlyweightVerifier verifier;

    public SudokuSolver() {
        this.verifier = new FlyweightVerifier();
    }

 
    public int[][] solve(Game game) throws InvalidGame {
        // 1. Find the 5 empty cells
        int[][] emptyPositions = findEmptyCells(game);

        if (emptyPositions.length != 5) {
            throw new InvalidGame("Solver requires exactly 5 empty cells, found: " + emptyPositions.length);
        }

        // 2. Try all permutations
        PermutationIterator iter = new PermutationIterator();

        while (iter.hasNext()) {
            int[] values = iter.next();

            // 3. Verify with Flyweight
            if (verifier.isValidWith(game, emptyPositions, values)) {
                // Found a solution!
                return formatSolution(emptyPositions, values);
            }
        }

        throw new InvalidGame("No valid solution found");
    }

  
    private int[][] findEmptyCells(Game game) {
        ArrayList<int[]> empties = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (game.getCell(row, col) == 0) {
                    empties.add(new int[] { row, col });
                }
            }
        }

        // Convert to array
        int[][] result = new int[empties.size()][2];
        for (int i = 0; i < empties.size(); i++) {
            result[i] = empties.get(i);
        }
        return result;
    }

    /**
     * Format solution as [[row, col, value], [row, col, value], ...]
     */
    private int[][] formatSolution(int[][] emptyPositions, int[] values) {
        int[][] solution = new int[5][3];
        for (int i = 0; i < 5; i++) {
            solution[i][0] = emptyPositions[i][0]; // row
            solution[i][1] = emptyPositions[i][1]; // col
            solution[i][2] = values[i]; // value
        }
        return solution;
    }
}
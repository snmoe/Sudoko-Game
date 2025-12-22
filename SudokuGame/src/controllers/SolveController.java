package controllers;

import exceptions.InvalidGame;
import model.Game;
import solver.SudokuSolver;

public class SolveController {

    private final SudokuSolver solver;

    public SolveController() {
        this.solver = new SudokuSolver();
    }

    public int[] solveGame(Game game) throws InvalidGame {
        return solver.solve(game);
    }
}

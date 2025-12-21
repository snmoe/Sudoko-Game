package adapter;

import exceptions.InvalidGame;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import filehandler.CSVFileHandler;
import interfaces.Viewable;
import java.io.IOException;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import view.Controllable;
import view.UserAction;

public class ViewAdapter implements Controllable {

    private final Viewable controller;

    public ViewAdapter(Viewable controller) {
        this.controller = controller;
    }

    @Override
    public boolean[] getCatalog() {
        controller.getCatalog();
        return new boolean[]{
            Catalog.isCurrent(), Catalog.isAllModesExist()
        };

    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {

        DifficultyEnum difficulty = switch (level) {
            case 'E' ->
                DifficultyEnum.EASY;
            case 'M' ->
                DifficultyEnum.MEDIUM;
            case 'H' ->
                DifficultyEnum.HARD;
            default ->
                throw new IllegalArgumentException("Invalid difficulty");
        };

        Game game = controller.getGame(difficulty);
        return game.getGrid();

    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        try {
            int[][] grid = CSVFileHandler.CSVReader(sourcePath);
            controller.driveGames(new Game(grid));
        } catch (IOException ex) {
            throw new SolutionInvalidException("Failed to load source solution");
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        String Result = controller.verifyGame(new Game(game));
        boolean valid = Result.equals("VALID");
        boolean[][] feedback = new boolean[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                feedback[i][j] = valid;
            }
        }
        return feedback;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        controller.solveGame(new Game(game));
        return game;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toString());
    }

}

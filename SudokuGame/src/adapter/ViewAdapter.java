package adapter;

import exceptions.InvalidGame;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import filehandler.CSVFileHandler;
import java.io.IOException;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
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
        controller.driveGamesFromPath(sourcePath);
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        String Result = controller.verifyGame(new Game(game));
        return null;

    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        int[] results = controller.solveGame(new Game(game));
        for (int posResult : results) {

        }
        return null;

    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toString());
    }

}

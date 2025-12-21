package adapter;

import exceptions.InvalidGame;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import facade.ControllerFacade;
import java.io.IOException;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import view.UserAction;

public class ViewAdapter implements Controllable {

    private final Viewable controller;

    public ViewAdapter() {
        this.controller = new ControllerFacade();
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
    public boolean[][] verifyGame(int[][] grid) {

        Game game = new Game(grid);
        boolean[][] invalid = new boolean[9][9];
        
        for(int[] pos : controller.getInvalidPositions(game)) {
            invalid[pos[0]][pos[1]] = true;
        }
        return invalid;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        int[] cellssolver = controller.solveGame(new Game(game));
        for (int i = 0; i < 5; i++) {
            game[cellssolver[i] / 100][(cellssolver[i] / 10) % 10] = cellssolver[i] % 10;

        }
        return game;

    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toString());
    }
    
    public void updateCurrentGame(int[][] game) throws IOException{
        Game g = new Game(game);
        controller.updateCurrentGame(g);
        
    } 
}

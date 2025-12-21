package facade;

import controllers.GenerateController;
import controllers.SolveController;
import controllers.VerifyController;
import exceptions.InvalidGame;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import filehandler.ActionLogger;
import filehandler.FileManager;
import generator.RandomPairs;
import adapter.Viewable;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.util.List;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;
import verification.DuplicationVerificationMode;

public class ControllerFacade implements Viewable {

    private GenerateController generateController;
    private SolveController solveController;
    private VerifyController verifyController;

    public ControllerFacade() {
        this.generateController = new GenerateController();
        this.solveController = new SolveController();
        this.verifyController = new VerifyController();
    }

    @Override
    public Catalog getCatalog() {
        boolean hasCurrent;
        try {
            FileManager.loadCurrentGame();
            hasCurrent = true;
        } catch (Exception e) {
            hasCurrent = false;
        }

        Catalog.setCurrent(hasCurrent);

        boolean allExist = FileManager.hasGameOfLevel(DifficultyEnum.EASY)
                && FileManager.hasGameOfLevel(DifficultyEnum.MEDIUM)
                && FileManager.hasGameOfLevel(DifficultyEnum.HARD);

        Catalog.setAllModesExist(allExist);

        return new Catalog();
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            return FileManager.loadGame(level);
        } catch (IOException e) {
            throw new NotFoundException("Failed to load game for level " + level);
        }
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        try {
            generateController.generate(sourceGame);
        } catch (Exception e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    @Override
    public String verifyGame(Game game) {
        return verifyController.verify(game);
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        return solveController.solveGame(game);
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        ActionLogger.logAction(userAction);
    }

    public void driveGamesFromPath(String sourcePath) throws SolutionInvalidException {
        try {
            Game sourceGame = FileManager.loadGameFromPath(sourcePath);
            driveGames(sourceGame);
        } catch (Exception e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    public List<int[]> getInvalidPositions(Game game) {

        return verifyController.getInvalidAbsolutePositions(game);
    }
    
    public void updateCurrentGame(Game game) throws IOException{
        FileManager.saveCurrentGame(game);
    }

    public Game getCurrentGame() throws NotFoundException {
        try {
            return FileManager.loadCurrentGame();
        } catch (NotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new RuntimeException("Error reading current game file: " + ex.getMessage());
        }
    }

}

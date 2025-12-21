package adapter;

import java.io.IOException;
import model.Catalog;
import model.Game;
import exceptions.*;
import java.util.List;
import model.DifficultyEnum;

public interface Viewable {

    Catalog getCatalog();

    Game getGame(DifficultyEnum level) throws NotFoundException;

    void driveGames(Game sourceGame) throws SolutionInvalidException;

    String verifyGame(Game game);

    int[] solveGame(Game game) throws InvalidGame;

    void logUserAction(String userAction) throws IOException;

    void driveGamesFromPath(String path) throws SolutionInvalidException;

    List<int[]> getInvalidPositions(Game game);

    Game getCurrentGame() throws NotFoundException;
}

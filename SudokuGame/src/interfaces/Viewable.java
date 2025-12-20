
package interfaces;

import java.io.IOException;
import model.Catalog;
import model.Game;
import model.DifficultyEnum;
import exceptions.*;

public interface Viewable {
  Catalog getCatalog();
  Game getGame(DifficultyEnum level) throws NotFoundException;
  void driveGames(Game sourceGame) throws SolutionInvalidException;
  String verifyGame(Game game);
  int[] solveGame(Game game) throws InvalidGame;
  void logUserAction(String userAction) throws IOException;
}

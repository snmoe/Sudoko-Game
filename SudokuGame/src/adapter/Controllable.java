package adapter;

import exceptions.*;
import java.io.IOException;
import view.UserAction;

public interface Controllable {

  boolean[] getCatalog();
  int[][] getGame(char level) throws NotFoundException;
  void driveGames(String sourcePath) throws SolutionInvalidException;
  boolean[][] verifyGame(int[][] game);
  int[][] solveGame(int[][] game) throws InvalidGame;
  void logUserAction(UserAction userAction) throws IOException;
  void updateCurrentGame(int[][] game) throws IOException;
   int[][] getCurrentGame() throws NotFoundException;

}

package generator;

import exceptions.InvalidGame;
import filehandler.FileManager;
import java.io.IOException;
import java.util.List;
import model.DifficultyEnum;
import model.Game;
import verification.VerificationEnum;
import verification.GameValidator;

public class GameDriver {

    public void drive(Game sourceSolution) throws InvalidGame, IOException {

        if (sourceSolution == null) {
            throw new InvalidGame("Source solution cannot be null.");
        }

        if (GameValidator.validate(sourceSolution) == VerificationEnum.INCOMPLETE) {
            throw new InvalidGame("Game is INCOMPLETE");
        } else if (GameValidator.validate(sourceSolution) == VerificationEnum.INVALID) {
            throw new InvalidGame("Game is INVALID");
        }

        generateAndSaveLevel(sourceSolution, DifficultyEnum.HARD, 25);
        generateAndSaveLevel(sourceSolution, DifficultyEnum.MEDIUM, 20);
        generateAndSaveLevel(sourceSolution, DifficultyEnum.EASY, 10);
    }

    private void generateAndSaveLevel(Game source, DifficultyEnum difficulty, int cellsToRemove) throws IOException {

        Game copy = source.clone();
        int[][] newGrid = copy.getGrid();

        RandomPairs pairsGenerator = new RandomPairs();
        List<int[]> pairsToRemove = pairsGenerator.generateDistinctPairs(cellsToRemove);

        for (int[] pair : pairsToRemove) {
            int row = pair[0];
            int col = pair[1];
            newGrid[row][col] = 0;
        }

        Game newGame = new Game(newGrid);

        try {
            FileManager.saveGame(difficulty, newGame);
        } catch (Exception e) {
            throw new IOException("Failed to save game level: " + difficulty);
        }
    }

}

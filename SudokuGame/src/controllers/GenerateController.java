package controllers;

import filehandler.FileManager;
import generator.RandomPairs;
import java.io.IOException;
import java.util.List;
import model.DifficultyEnum;
import model.Game;

public class GenerateController {

    private final RandomPairs randomPairs = new RandomPairs();
    

    private void generateLevel(Game solved, DifficultyEnum level, int remove) throws IOException {
        Game copy = solved.clone();
        List<int[]> cells = randomPairs.generateDistinctPairs(remove);
        for (int[] p : cells) {
            copy.setCell(p[0], p[1], 0);
        }
        FileManager.saveGame(level, copy);
    }

    public void generateAndSave(Game solved) throws IOException {
        generateLevel(solved, DifficultyEnum.EASY, 10);
        generateLevel(solved, DifficultyEnum.MEDIUM, 20);
        generateLevel(solved, DifficultyEnum.HARD, 25);
    }
}

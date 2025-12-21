package controllers;

import exceptions.InvalidGame;
import filehandler.FileManager;
import generator.GameDriver;
import generator.RandomPairs;
import java.io.IOException;
import java.util.List;
import model.DifficultyEnum;
import model.Game;

public class GenerateController {

    private GameDriver gameDriver;

    public GenerateController() {
        this.gameDriver = new GameDriver();
    }

    public void generate(Game sourceGame) throws InvalidGame, IOException {
        gameDriver.drive(sourceGame);
    }

}

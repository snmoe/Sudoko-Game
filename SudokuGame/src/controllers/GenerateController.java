package controllers;

import exceptions.InvalidGame;

import generator.GameDriver;

import java.io.IOException;

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

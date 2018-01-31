package scene;

import texture.TextureManager;
import controller.Controller;
import controller.PlayerController;
import controller.AIController;
import grid.Tile;
import grid.Coordinates;
import texture.Texture;
import config.Config;

public class Level1 extends GameManager {

    /// Initialise controllers.
    public Level1(TextureManager textureManager) {

        super(textureManager, new Controller[] {new PlayerController(),
                                                new AIController()});

    }

    /// Get next level or null if game lost.
    public GameManager getNext() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return null;
        }
        return new Level2(mTextureManager);

    }

    /// Setup units and buildings.
    protected void setup() {

        addHq(new Coordinates(8, 8), Config.PLAYER1);
        addHq(new Coordinates(8, 1), Config.PLAYER2);

        addSnake(new Coordinates(9, 1), Config.PLAYER2);
        addSnake(new Coordinates(7, 1), Config.PLAYER2);
        addWheelSnake(new Coordinates(8, 2), Config.PLAYER2);

        addSnake(new Coordinates(7, 6), Config.PLAYER1);
        addWheelSnake(new Coordinates(5, 9), Config.PLAYER1);
        addTankSnake(new Coordinates(4, 8), Config.PLAYER1);
        addJetSnake(new Coordinates(3, 7), Config.PLAYER1);

    }

    /// Initialise grid tiles.
    protected void initTiles() {

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                addPlains(new Coordinates(i, j));
            }
        }

        for (int i = 2; i < 5; ++i) {
            for (int j = 4; j < 6; ++j) {
                addMountain(new Coordinates(i, j));
            }
        }

        for (int i = 5; i < 10; ++i) {
            for (int j = 4; j < 6; ++j) {
                addWater(new Coordinates(i, j));
            }
        }

        for (int j = 0; j < 10; ++j) {
            addRoad(new Coordinates(1, j), true, 1);
        }
        
    }

    /// Get text explaining the level.
    protected String getInitialInfo() {

        return "Level 1\n" +
               "Welcome to Snake Advance! In this game you control a variety of\n" +
               "units that can move and attack. The goal is to destroy the enemy's\n" + 
               "HQ building while protecting your own. Each different unit type can\n" +
               "move before carrying out an action each turn. Click on your units\n" +
               "to see what actions are available to them. Each unit has different\n" +
               "attack power and defense. Different units can move more easily\n" +
               "over different types of terrain so be sure to try out the\n" +
               "differences between each unit!";

    }

    /// Get text explaining the result of the game.
    protected String getEndInfo() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return "Game Over";
        } else {
            return "Level 1 Complete!";
        }

    }

}

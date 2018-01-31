package scene;

import texture.TextureManager;
import controller.Controller;
import controller.PlayerController;
import controller.AIController;
import grid.Tile;
import grid.Coordinates;
import texture.Texture;
import config.Config;

public class Level2 extends GameManager {

    /// Initialise controllers.
    public Level2(TextureManager textureManager) {

        super(textureManager, new Controller[] {new PlayerController(),
                                                new AIController()});

    }

    /// Get next level or null if game lost.
    public GameManager getNext() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return null;
        }
        return new Level3(mTextureManager);

    }

    /// Setup units and buildings.
    protected void setup() {

        addHq(new Coordinates(0, 9), Config.PLAYER1);
        addHq(new Coordinates(9, 2), Config.PLAYER2);

        addSnake(new Coordinates(0, 8), Config.PLAYER1);
        addSnake(new Coordinates(8, 2), Config.PLAYER2);

        Coordinates[] flagPositions = {new Coordinates(0, 6),
                                       new Coordinates(1, 6),
                                       new Coordinates(8, 7),
                                       new Coordinates(0, 0),
                                       new Coordinates(9, 0),
                                       new Coordinates(3, 3)};

        for (int i = 0; i < flagPositions.length; ++i) {
            addFlag(flagPositions[i]);
        }

    }

    /// Initialise grid tiles.
    protected void initTiles() {

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                addPlains(new Coordinates(i, j));
            }
        }

        for (int i = 7; i < 10; ++i) {
            for (int j = 5; j < 10; ++j) {
                if (i != 8 || j != 7) {
                    addWater(new Coordinates(i, j));
                }
            }
        }

        for (int j = 0; j < 5; ++j) {
            addMountain(new Coordinates(7, j));
        }
        for (int j = 5; j < 9; ++j) {
            addMountain(new Coordinates(2, j));
        }
        for (int i = 0; i < 2; ++i) {
            addMountain(new Coordinates(i, 5));
            addMountain(new Coordinates(i, 1));
        }
        addMountain(new Coordinates(1, 0));

        for (int i = 5; i < 9; ++i) {
            addRoad(new Coordinates(i, 2), true, 0);
        }
        addRoad(new Coordinates(4, 2), false, 0);
        for (int j = 3; j < 9; ++j) {
            addRoad(new Coordinates(4, j), true, 1);
        }
        addRoad(new Coordinates(4, 9), false, 2);
        for (int i = 1; i < 4; ++i) {
            addRoad(new Coordinates(i, 9), true, 0);
        }

        
    }

    /// Get text explaining the level.
    protected String getInitialInfo() {

        return "Level 2\n" +
               "In this level you will need to build your own army of snakes.\n" +
               "Move a unit over a flag and it will have the option of using its\n" +
               "action for the turn on capturing the flag. At the start of the turn\n" +
               "you gain 500 points for each flag you control. Then, when you\n" +
               "have enough points you can spend them to recruit new snakes\n" +
               "from your HQ. Capture flags to build an army of snakes and\n" +
               "defeat the enemy!";

    }

    /// Get text explaining the result of the game.
    protected String getEndInfo() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return "Game Over";
        } else {
            return "Level 2 Complete!";
        }

    }

}

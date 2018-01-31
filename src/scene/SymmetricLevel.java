package scene;

import texture.TextureManager;
import controller.Controller;
import grid.Tile;
import grid.Coordinates;
import texture.Texture;
import config.Config;

public class SymmetricLevel extends GameManager {

    /// Initialise controllers.
    public SymmetricLevel(TextureManager textureManager, Controller[] controllers) {

        super(textureManager, controllers);

    }

    /// Get next level or null if game lost.
    public GameManager getNext() {
        return null;
    }

    /// Setup units and buildings.
    protected void setup() {

        addHq(new Coordinates(0, 0), Config.PLAYER1);
        addHq(new Coordinates(9, 9), Config.PLAYER2);

        addSnake(new Coordinates(1, 0), Config.PLAYER1);
        addSnake(new Coordinates(8, 9), Config.PLAYER2);

        Coordinates[] flagPositions = {new Coordinates(0, 9),
                                       new Coordinates(9, 0),
                                       new Coordinates(6, 3),
                                       new Coordinates(3, 6),
                                       new Coordinates(3, 0),
                                       new Coordinates(6, 9)};

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

        for (int i = 1; i < 9; ++i) {
            for (int j = 4; j < 6; ++j) {
                addWater(new Coordinates(i, j));
            }
        }

        for (int i = 3; i < 7; ++i) {
            for (int j = 4; j < 6; ++j) {
                addMountain(new Coordinates(i, j));
            }
        }

        for (int i = 1; i < 9; ++i) {
            addRoad(new Coordinates(i, 2), true, 0);
            addRoad(new Coordinates(i, 7), true, 0);
        }

        for (int j = 3; j < 7; ++j) {
            addRoad(new Coordinates(0, j), true, 1);
            addRoad(new Coordinates(9, j), true, 1);
        }
        addRoad(new Coordinates(0, 2), false, 0);
        addRoad(new Coordinates(9, 2), false, 1);
        addRoad(new Coordinates(0, 7), false, 3);
        addRoad(new Coordinates(9, 7), false, 2);
        
    }

    /// Get text explaining the level.
    protected String getInitialInfo() {

        return "Take turns to try and destroy the enemy HQ!";

    }

    /// Get text explaining the result of the game.
    protected String getEndInfo() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return "Player 2 Wins!";
        } else {
            return "Player 1 Wins!";
        }

    }

}

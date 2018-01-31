package scene;

import texture.TextureManager;
import controller.Controller;
import controller.PlayerController;
import controller.AIController;
import controller.Controller;
import grid.Tile;
import grid.Coordinates;
import texture.Texture;
import config.Config;

public class Level3 extends GameManager {

    /// Initialise controllers.
    public Level3(TextureManager textureManager) {

        super(textureManager, new Controller[] {new PlayerController(),
                                                new AIController()});

    }

    /// Get next level or null if game lost.
    public GameManager getNext() {
        return null;
    }

    /// Setup units and buildings.
    protected void setup() {

        addHq(new Coordinates(4, 8), Config.PLAYER1);
        addHq(new Coordinates(8, 3), Config.PLAYER2);
        addHq(new Coordinates(0, 0), Config.PLAYER2);

        addSnake(new Coordinates(3, 8), Config.PLAYER1);
        addSnake(new Coordinates(5, 8), Config.PLAYER1);
        addSnake(new Coordinates(7, 3), Config.PLAYER2);
        addSnake(new Coordinates(0, 1), Config.PLAYER2);

        Coordinates[] flagPositions = {new Coordinates(1, 9),
                                       new Coordinates(8, 9),
                                       new Coordinates(1, 3),
                                       new Coordinates(9, 1),
                                       new Coordinates(2, 6),
                                       new Coordinates(5, 4)};

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

        for (int i = 0; i < 10; ++i) {
            addWater(new Coordinates(i, 5));
        }

        for (int i = 0; i < 7; ++i) {
            if (i != 4) {
                addRoad(new Coordinates(i, 8), true, 0);
            }
        }
        addRoad(new Coordinates(7, 8), false, 2);
        addRoad(new Coordinates(7, 7), false, 0);
        addRoad(new Coordinates(8, 7), false, 2);
        for (int i = 1; i < 8; ++i) {
            addRoad(new Coordinates(i, 0), true, 0);
        }
        addRoad(new Coordinates(8, 0), false, 1);
        for (int j = 1; j < 7; ++j) {
            if (j != 3) {
                addRoad(new Coordinates(8, j), true, 1);
            }
        }

        for (int i = 3; i < 5; ++i) {
            for (int j = 1; j < 5; ++j) {
                addMountain(new Coordinates(i, j));
            }
        }
        for (int i = 0; i < 4; ++i) {
            addMountain(new Coordinates(i, 5));
        }

    }

    /// Get text explaining the level.
    protected String getInitialInfo() {

        return "Level 3\n" +
               "You've made it to the final level! Use all that you've learned\n" +
               "to defeat the enemy. This time the enemy has two HQs. Destroy\n" +
               "them both to win!";

    }

    /// Get text explaining the result of the game.
    protected String getEndInfo() {

        if (getStatus(Config.PLAYER1).hasLost()) {
            return "Game Over";
        } else {
            return "Level 3 Complete!\n" +
                   "Congratulations! You've completed every level.\n" +
                   "Now test your skills against a friend in multiplayer.";
        }

    }

}

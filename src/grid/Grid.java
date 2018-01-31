package grid;

import java.util.Iterator;
import java.util.ArrayList;
import gameobject.RenderableObject;
import processing.core.PApplet;
import processing.core.PVector;
import config.Config;


/// Grid of tiles.
public class Grid extends RenderableObject {

    /// Grid size.
    private static int GRID_LENGTH = 10;
    private static float TILE_SIZE = 75f;

    /// Tiles making up the grid.
    private final Tile[][] mTiles;

    /// Initialise grid.
    public Grid() {

        mTiles = new Tile[GRID_LENGTH][GRID_LENGTH];
        setTranslation(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT - (TILE_SIZE * mTiles[0].length / 2f));

    }

    /// Get tile at specified coordinates.
    public Tile getTile(Coordinates coords) {
        return mTiles[coords.x][coords.y];
    }

    /// Set tile at specified coordinates.
    public void setTile(Coordinates coords, Tile tile) {
        mTiles[coords.x][coords.y] = tile;
    }

    /// Get tile that is at target position.
    public Tile getTile(PVector pos) {

        Tile selected = null;
        for (int i = 0; selected == null && i < mTiles.length; ++i) {
            for (int j = 0; selected == null && j < mTiles[0].length; ++j) {
                if (mTiles[i][j].getBounds().transform(this).contains(pos)) {
                    selected = mTiles[i][j];
                }
            }
        }
        return selected;

    }

    /// Update the all the tiles in the grid.
    public void update(float delta) {

        for (int i = 0; i < mTiles.length; ++i) {
            for (int j = 0; j < mTiles[0].length; ++j) {
                mTiles[i][j].update(delta);
            }
        }

    }
    
    /// Render each tile then the units on those tiles.
    public void renderCurrent(PApplet core) {

        for (int i = 0; i < mTiles.length; ++i) {
            for (int j = 0; j < mTiles[0].length; ++j) {
                mTiles[i][j].render(core);
            }
        }
        for (int i = 0; i < mTiles.length; ++i) {
            for (int j = 0; j < mTiles[0].length; ++j) {
                mTiles[i][j].renderUnit(core);
            }
        }

    }


    /// Get the coordinates of tiles neighbouring specified position.
    public ArrayList<Coordinates> getNeighbourCoords(Coordinates coords) {

        ArrayList<Coordinates> neighbours = new ArrayList<Coordinates>();

        boolean left = coords.x > 0;
        boolean right = coords.x < (mTiles.length - 1);
        boolean upper = coords.y > 0;
        boolean lower = coords.y < (mTiles[0].length - 1);

        if (left) {
            neighbours.add(new Coordinates(coords.x - 1, coords.y));
        }
        if (right) {
            neighbours.add(new Coordinates(coords.x + 1, coords.y));
        }
        if (upper) {
            neighbours.add(new Coordinates(coords.x, coords.y - 1));
        }
        if (lower) {
            neighbours.add(new Coordinates(coords.x, coords.y + 1));
        }

        return neighbours;

    }

    /// Align the positions of tiles to match logical representation.
    public void alignTiles() {

        float x = -(TILE_SIZE * (mTiles.length - 1)) / 2f;
        float y = -(TILE_SIZE * (mTiles[0].length - 1)) / 2f;

        for (int i = 0; i < mTiles.length; ++i) {
            float currentY = y;
            for (int j = 0; j < mTiles[0].length; ++j) {
                mTiles[i][j].setTranslation(x, currentY);
                currentY += TILE_SIZE;
            }
            x += TILE_SIZE;
        }

    }

}

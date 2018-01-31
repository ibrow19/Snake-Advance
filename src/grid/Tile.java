package grid;

import processing.core.PApplet;
import gameobject.RenderableObject;
import gameobject.TextureObject;
import texture.Texture;
import unit.Unit;
import terrain.Terrain;
import building.Building;
import rect.Rect;

// Tile that is used to make up game grid.
public class Tile extends RenderableObject {

    /// Coordinates tile is located at.
    private final Coordinates mCoords;

    /// Terrain on this tile.
    private final Terrain mTerrain;

    /// Texture to use for selection.
    private final TextureObject mSelect;

    /// Texture to use for highlighting.
    private final TextureObject mHighlight;

    /// Building on this tile.
    private Building mBuilding;

    /// Unit on this tile.
    private Unit mUnit;

    /// Whether this tile is currently selectable.
    private boolean mSelectable;

    /// Whether this tile is currently highlighted.
    private boolean mHighlighted;

    /// Initialise tile.
    public Tile(Coordinates coords,
                Terrain terrain, 
                Texture select, 
                Texture highlight) {

        mCoords = coords;
        mTerrain = terrain;
        mHighlight = new TextureObject(highlight);
        mSelect = new TextureObject(select);

        mBuilding = null;
        mUnit = null;

        mSelectable = false;
        mHighlighted = false;

    }

    /// Set unit on tile.
    public void setUnit(Unit unit) {

        mUnit = unit;
        if (unit != null) {
            unit.setCoords(mCoords);
        }

    }

    /// Set building on tile.
    public void setBuilding(Building building) {
        mBuilding = building;
        building.setCoords(mCoords);
    }

    /// Get coordinates of tile in grid.
    public Coordinates getCoords() {
        return mCoords;
    }

    /// Get unit on tile.
    public Unit getUnit() {
        return mUnit;
    }

    /// Get building on tile.
    public Building getBuilding() {
        return mBuilding;
    }

    /// Get terrain on tile.
    public Terrain getTerrain() {
        return mTerrain;
    }

    /// Set whether tile is highlighted.
    public void setHighlight(boolean highlighted) {
        mHighlighted = highlighted;
    }

    /// Deselect tile.
    public void deselect() {
        mSelectable = false;
    }

    /// Set tile as selectable by a player.
    public void setSelectable(int player) {
        mSelect.setClip(player); 
        mSelectable = true;
    }

    /// Get bounding rectangle for tile.
    public Rect getBounds() {
        Rect bounds = mTerrain.getBounds();
        bounds.transform(this);
        return bounds;
    }

    // Update tile by updating unit and removing it if it is destroyed.
    public void update(float delta) {

        if (mUnit != null) {
            mUnit.update(delta);
            if (!mUnit.isBusy() && mUnit.isDestroyed()) {
                mUnit = null;
            }
        }

    }

    /// Render the main elements of the tile other than the unit.
    public void renderCurrent(PApplet core) {

        mTerrain.render(core);
        if (mBuilding != null) {
            mBuilding.render(core);
        }
        if (mSelectable) {
            mSelect.render(core);
        }
        if (mHighlighted) {
            mHighlight.render(core);
        }

    }

    /// Render the unit on the tile.
    public void renderUnit(PApplet core) {

        if (mUnit != null) {
            core.pushMatrix();
            applyTransform(core);
            mUnit.render(core);
            core.popMatrix();
        }

    }

}

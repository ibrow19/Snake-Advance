package terrain;

import gameobject.TextureObject;
import texture.Texture;
import unit.Unit;

/// Terrain for a single tile on game grid.
public abstract class Terrain extends TextureObject {

    /// Costs of movement over terrain for each unit move type. Move cost < 0 
    /// means terrain is impassable for that movement type.
    private final int[] mMoveCosts;

    /// Cover defense modifier provided by terrain in combat.
    private final float mCover;

    /// Initialise terrain.
    public Terrain(Texture texture, int clip, int[] moveCosts, float cover) {

        super(texture);
        setClip(clip);
        mMoveCosts = moveCosts;
        mCover = cover;
        assert mMoveCosts.length == Unit.MOVE_TYPE_COUNT;

    }

    /// Check if terrain is passable for a unit move type.
    public boolean isPassable(int moveType) {

        return mMoveCosts[moveType] >= 0;

    }

    /// Get move cost for crossing terrain for a unit move type.
    public int getMoveCost(int moveType) {

        return mMoveCosts[moveType];

    }

    /// Get the terrain's cover modifier.
    public float getCover() {

        return mCover;

    }

}

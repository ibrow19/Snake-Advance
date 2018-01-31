package building;

import gameobject.TextureObject;
import texture.Texture;
import grid.Coordinates;


/// A building on the grid that can be captured by different units.
public class Building extends TextureObject {

    /// Attack boost multiplier.
    private final float mBoost;

    /// Protection multiplier.
    private final float mProtection;

    /// Coordinates on grid.
    private Coordinates mCoords;

    /// Player ID of current owner of building.
    private int mOwner;

    /// Initialise building.
    public Building(Texture texture, float boost, float protection) {

        super(texture);
        mProtection = protection;
        mBoost = boost;
        mCoords = null;
        mOwner = 0;
        setClip(mOwner + 1);

    }

    /// Set the owner of the building.
    public void setOwner(int player) {
        mOwner = player;
        setClip(mOwner + 1);
    }

    /// Set the position of the building on the grid.
    public void setCoords(Coordinates coords) {
        mCoords = coords;
    }

    /// Get the current owner of the building.
    public int getOwner() {
        return mOwner;
    }

    /// Get the current location of the building on the grid.
    public Coordinates getCoords() {
        return mCoords;
    }

    /// Get attack boost multiplier.
    public float getBoost() {
        return mBoost;
    }

    /// Get protection multiplier.
    public float getProtection() {
        return mProtection;
    }

}

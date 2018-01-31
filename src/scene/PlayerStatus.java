package scene;

import java.util.Iterator;
import java.util.ArrayList;
import processing.core.PApplet;
import texture.TextureManager;
import gameobject.RenderableObject;
import gameobject.TextObject;
import colour.Colour;
import unit.HeadQuarters;
import unit.Unit;
import building.Flag;
import config.Config;

/// Records the status of a player: the units/flags they control in addition to
/// managing the points they have.
public class PlayerStatus extends RenderableObject {

    private static final int TEXT_SIZE = 22;

    /// Player ID status is for.
    private final int mPlayer;

    /// Points the player has.
    private int mPoints;

    /// Status text to display.
    private final TextObject mText;

    /// HQs the player controls.
    private ArrayList<HeadQuarters> mHqs;

    /// Other units the player controls.
    private ArrayList<Unit> mUnits;

    /// Flags in the game.
    private ArrayList<Flag> mFlags;

    /// Initialise status.
    public PlayerStatus(int player) {

        mPlayer = player;
        mPoints = 0;
        mText = new TextObject(TEXT_SIZE, false);
        
        mText.setColour(Config.PLAYER_COLOURS[player]);
        setText();

        mHqs = new ArrayList<>();
        mUnits = new ArrayList<>();
        mFlags = new ArrayList<>();

    }

    /// Update by removing destroyed units and HQs and updating displayed
    /// text.
    public void update(float delta) {

        Iterator<HeadQuarters> hqIt = mHqs.iterator();
        while (hqIt.hasNext()) {
            if (hqIt.next().isDestroyed()) {
                hqIt.remove();
            }
        }
        Iterator<Unit> unitIt = mUnits.iterator();
        while (unitIt.hasNext()) {
            if (unitIt.next().isDestroyed()) {
                unitIt.remove();
            }
        }
        setText();

    }

    /// Display status text.
    public void renderCurrent(PApplet core) {
        mText.render(core);
    }

    /// Player loses when no HQs remain.
    public boolean hasLost() {
        return mHqs.isEmpty();
    }

    /// Get a copy of HQ list.
    public ArrayList<HeadQuarters> getHqs() {
        ArrayList<HeadQuarters> hqs = new ArrayList<>();
        hqs.addAll(mHqs);
        return hqs;
    }

    /// Get a copy of unit list.
    public ArrayList<Unit> getUnits() {
        ArrayList<Unit> units = new ArrayList<>();
        units.addAll(mUnits);
        return units;
    }

    /// Get a copy of flag list.
    public ArrayList<Flag> getFlags() {
        ArrayList<Flag> flags = new ArrayList<>();
        flags.addAll(mFlags);
        return flags;
    }

    /// Start turn by activating units and gaining points for
    /// each owned flag.
    public void startTurn() {

        for (HeadQuarters hq : mHqs) {
            hq.activate();
        }
        for (Unit unit : mUnits) {
            unit.activate();
        }

        for (Flag flag : mFlags) {
            if (flag.getOwner() == mPlayer) {
                mPoints += Flag.POINTS_VALUE;            
            }
        }

    }

    /// End turn bu deactivating all HQs and units.
    public void endTurn() {

        for (HeadQuarters hq : mHqs) {
            hq.deactivate();
        }
        for (Unit unit : mUnits) {
            unit.deactivate();
        }

    }

    public void addHq(HeadQuarters hq) {
        mHqs.add(hq);
    }

    public void addUnit(Unit unit) {
        mUnits.add(unit);
    }

    public void addFlag(Flag flag) {
        mFlags.add(flag);
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public void subPoints(int points) {
        mPoints -= points;
    }

    private void setText() {
        mText.setText("Player " + mPlayer + ": " + mPoints + " Points");
    }

}

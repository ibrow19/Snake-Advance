package unit;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.lang.Runnable;
import gameobject.RenderableObject;
import gameobject.TextureObject;
import gameobject.TextObject;
import texture.Texture;
import texture.Animation;
import grid.Grid;
import grid.Coordinates;
import grid.Tile;
import menu.Button;
import menu.ActionButton;
import scene.GameManager;
import terrain.Terrain;
import config.Config;
import building.Building;

/// Unit that can move and perform actions.
public abstract class Unit extends RenderableObject {

    // Move type constants.
    public final static int STANDARD_MOVE = 0;
    public final static int VEHICLE_MOVE = 1;
    public final static int AIR_MOVE = 2;
    public final static int MOVE_TYPE_COUNT = 3;

    /// Time taken to move across one tile.
    private final static float MOVE_DURATION = 0.5f;

    /// Time taken for one step in an attack to complete.
    private final static float ATTACK_DURATION = 0.25f;

    /// Modifier for damage when counter attacking.
    private final static float COUNTER_ATTACK_MODIFIER = 0.6f;

    /// Size of text used for unit status.
    private final static int STATUS_TEXT_SIZE = 15;

    /// Offset from centre for unit status.
    private final static float STATUS_OFFSET = 30f;

    /// Unit HP.
    private final static int BASE_HITPOINTS = 100;

    /// Base attack rating.
    private final int mAttack;

    /// Defense modifier.
    private final float mDefense;

    /// Type of movement unit uses.
    private final int mMoveType;

    /// Movement range of unit.
    private final int mMoveRange;

    /// Texture to use for displaying unit.
    private final TextureObject mTexture;

    /// Animation to use for unit.
    private Animation mAnimation;

    /// Text displaying unit HP.
    private final TextObject mHealthStatus;

    /// Text displaying whether unit has a move or action remaining.
    private final TextObject mActionStatus;

    /// Current HP.
    private int mHitPoints;

    /// Whether the unit has moved.
    private boolean mMoved;

    /// Whether the unit has carried out an action.
    private boolean mActed;

    /// Whether the unit is currently occupied with another action.
    private boolean mBusy;

    /// Whether the unit has attacked in its attack step.
    private boolean mAttacked;

    /// Whether it is the attacker step (or else counter attack step) of an attack.
    private boolean mAttackerStep;

    /// Coordinates of unit being attacked.
    private Coordinates mAttackCoords;

    /// Time passed carrying out current action.
    private float mActionProgress;

    /// Map of available destinations to paths to that destination.
    private Map<Coordinates, ArrayList<Coordinates>> mPaths;

    /// Current path being followed.
    private ArrayList<Coordinates> mMovePath;

    /// Current coordinate of tile being moved across.
    private Coordinates mMoveCoords;

    /// Id of player owning this unit.
    protected final int mPlayer;

    /// Texture to use for buttons.
    protected final Texture mButtonTexture;

    /// Grid unit is on.
    protected final Grid mGrid;

    /// Coordinates of unit on grid.
    protected Coordinates mCoords;

    /// Initialise unit.
    public Unit(Texture unitTexture, 
                Texture buttonTexture, 
                int player,
                Grid grid,
                int attack,
                float defense,
                int moveRange,
                int moveType) {

        mTexture = new TextureObject(unitTexture);
        mAnimation = null;
        mHealthStatus = new TextObject(STATUS_TEXT_SIZE, true);
        mActionStatus = new TextObject(STATUS_TEXT_SIZE, true);
        mHealthStatus.setTranslation(0f, -STATUS_OFFSET);
        mActionStatus.setTranslation(0f, STATUS_OFFSET);
        mHealthStatus.setColour(Config.PLAYER_COLOURS[player]);
        mActionStatus.setColour(Config.PLAYER_COLOURS[player]);
        mHitPoints = BASE_HITPOINTS;

        mButtonTexture = buttonTexture;
        mPlayer = player;
        mGrid = grid;
        mCoords = null;

        mPaths = null;
        mMovePath = null;
        mMoveCoords = null;

        mAttacked = false;
        mAttackerStep = true;
        mAttackCoords = null;

        mActionProgress = 0f;

        mMoved = true;
        mActed = true;
        mBusy = false;

        mAttack = attack;
        mDefense = defense;
        mMoveRange = moveRange;
        mMoveType = moveType;

        setStatus();
        mTexture.setClip(player);

    }

    /// Update the unit's animation, status display and attack/move.
    /// \param delta time since last update.
    public void update(float delta) {

        setStatus();
        if (mAnimation != null) {
            mAnimation.update(delta);
            mTexture.setClip(mAnimation.getClip());
        }

        if (mMovePath != null) {
            updateMove(delta);
        } else if (mAttackCoords != null) {
            updateAttack(delta);
        }

    }

    /// Render the unit and its actions status and HP.
    public void renderCurrent(PApplet core) {

        mTexture.render(core);
        mHealthStatus.render(core);
        mActionStatus.render(core);

    }

    /// Activate the unit by resetting whether it has moved/acted.
    public void activate() {

        mMoved = mMoveRange <= 0;
        mActed = false;

    }

    /// Deactivate unit preventing it from moving or acting.
    public void deactivate() {

        mMoved = true;
        mActed = true;

    }

    /// Check whether unit is destroyed.
    public boolean isDestroyed() {
        return mHitPoints <= 0;
    }

    /// Get ID of owner of unit.
    public int getPlayer() {
        return mPlayer;
    }

    /// Get Coordinates unit is at.
    public Coordinates getCoords() {
        return mCoords;
    }

    /// Check if unit is currently occupied with another action.
    public boolean isBusy() {
        return mBusy;
    }

    /// Set whether the unit is busy.
    public void setBusy(boolean busy) {
        mBusy = busy;
    }

    /// Initiate an attack on unit at target coordinates.
    public void attack(Coordinates coords) {

        Unit defender = mGrid.getTile(coords).getUnit();

        assert !mBusy;
        assert !defender.isBusy();

        mBusy = true;
        defender.setBusy(true);

        mActionProgress = 0f;
        mAttacked = false;
        mAttackerStep = true;
        mAttackCoords = coords;

    }

    /// Get the unit's unmodified base attack.
    public int getAttack() {
        return mAttack;
    }

    /// Get attack modified based on bonuses provided by buildings.
    public int getModifiedAttack() {

        float attack = mAttack;
        Tile tile = mGrid.getTile(mCoords); 
        Building building = tile.getBuilding();
        if (building != null && building.getOwner() == mPlayer) {
            attack += attack * building.getBoost();
        }
        return (int)attack;

    }

    /// Deal attack damage to this unit. Damage is decreased by defense
    /// multiplier, terrain cover multiplier and building protection multiplier.
    /// \param attack base damage to deal to unit.
    public void dealDamage(int attack) {

        float damage = attack;
        Tile tile = mGrid.getTile(mCoords); 
        damage -= damage * tile.getTerrain().getCover();

        Building building = tile.getBuilding();
        if (building != null && building.getOwner() == mPlayer) {
            damage -= damage * building.getProtection();
        }

        damage -= damage * mDefense;
        mHitPoints -= (int)damage;

    }

    /// Set coordinates of the unit. If unit is already at another position
    /// then initiate move between old position and new position.
    /// \param coords position to move unit to.
    public void setCoords(Coordinates coords) {

        if (mCoords != null) {

            assert !mMoved;
            assert !mActed;
            assert !mBusy;
            assert mPaths.containsKey(coords);

            mMoveCoords = mCoords;
            mActionProgress = 0f;
            mMovePath = mPaths.get(coords);
            mCoords = coords;
            mBusy = true;
            mMoved = true;

            // Update initial offset for movement.
            moveTransform();

        } else {
            mCoords = coords;
        }
        mPaths = null;

    }

    /// Get actions available to unit.
    /// \param gameManager game to carry out actions in.
    /// \return buttons for each available action.
    public ArrayList<Button> getActions(GameManager gameManager) {

        ArrayList<Button> buttons = new ArrayList<>();
        if (mActed) {
            return buttons;
        }
        Button moveButton = mMoved ? null : createMoveButton(gameManager);
        Button attackButton = createAttackButton(gameManager);
        Button captureButton = createCaptureButton(gameManager);
        if (moveButton != null) {
            buttons.add(moveButton);
        }
        if (attackButton != null) {
            buttons.add(attackButton);
        }
        if (captureButton != null) {
            buttons.add(captureButton);
        }
        buttons.addAll(getExtraActions(gameManager));
        return buttons;

    }

    /// Get move destinations available for unit.
    /// \return array of positions unit can move to.
    public ArrayList<Coordinates> getDestinations() {

        ArrayList<Coordinates> destinations = new ArrayList<>();
        if (mCoords != null) {
            mPaths = getPaths();
            for (Coordinates coords : mPaths.keySet()) {
                destinations.add(coords);
            }
        }
        return destinations;

    }

    /// Get coordinates of positions this unit can attack from its current position.
    /// \return coordinates of positions this unit can attack from its current position.
    public ArrayList<Coordinates> getAttackTargets() {
        return getAttackTargets(mCoords);
    }

    /// Get coordinates of positions this unit can attack from its target position.
    /// \param coords target position to check attacks from.
    /// \return coordinates of positions this unit can attack from its target position.
    public ArrayList<Coordinates> getAttackTargets(Coordinates coords) {

        ArrayList<Coordinates> targets = mGrid.getNeighbourCoords(coords);
        Iterator<Coordinates> it = targets.iterator();
        while(it.hasNext()) {
            Unit unit = mGrid.getTile(it.next()).getUnit();
            if (unit == null || unit.getPlayer() == mPlayer) {
                it.remove();
            }
        }
        return targets;

    }

    /// Get shortest path unit would need to take to get to target.
    /// \param target target to get shortest path to.
    /// \return list of coordinates making up shortest path to target or null if no path exists.
    public ArrayList<Coordinates> getShortestPath(Coordinates target) {

        // Evaluated coordinates.
        HashSet<Coordinates> evaluated = new HashSet<Coordinates>();

        // Previous step to reach a cell.
        HashMap<Coordinates, Coordinates> previousStep = new HashMap<Coordinates, Coordinates>();

        // Cost of reaching a cell.
        HashMap<Coordinates, Float> baseCost = new HashMap<Coordinates, Float>();
        baseCost.put(mCoords, 0f);

        // Unevaluated coordinates with baseCost + admissable heuristic (Directly to the target).
        PriorityQueue<PathCost> unevaluated = new PriorityQueue<PathCost>(11, new PathComparator());
        unevaluated.add(new PathCost(mCoords, mCoords.distance(target)));

        boolean found = false;
        Coordinates current = null;

        // Search until target found or no more cells to evaluate.
        while (!found && unevaluated.peek() != null) {

            // Get lowest cost unevaluated nodes.
            current = unevaluated.poll().coords;
            found = current.equals(target);

            // If it is not target and a better path has not been evaluated already
            // then evaluate its neighbours.
            if (!found && !evaluated.contains(current)) {

                evaluated.add(current);  
                ArrayList<Coordinates> neighbours = mGrid.getNeighbourCoords(current);
                Iterator<Coordinates> it = neighbours.iterator();

                // Evaluate neighbours.
                while (it.hasNext()) {

                    Coordinates n = it.next();
                    Terrain nTerrain = mGrid.getTile(n).getTerrain();
                    if (!evaluated.contains(n) && nTerrain.isPassable(mMoveType)) {

                        float g = baseCost.get(current) + nTerrain.getMoveCost(mMoveType);
                        if (!baseCost.containsKey(n) || baseCost.get(n) > g) {

                            baseCost.put(n, g); 
                            previousStep.put(n, current);
                            float f = g + n.distance(target);
                            unevaluated.add(new PathCost(n, f));

                        }
                    }
                }
            }
        }

        // Reconstruct path if a path was found.
        if (found) {
            ArrayList<Coordinates> path = new ArrayList<Coordinates>();
            while (!current.equals(mCoords)) {
                path.add(0, current);
                current = previousStep.get(current);
            }
            return path;
        }

        // Return null if no path was found.
        return null;

    }

    /// Get any additional actions the unit can perform. To be overridden by subclasses that wish
    /// to perform more than the basic set of actions.
    protected ArrayList<Button> getExtraActions(GameManager gameManager) {
        return new ArrayList<>();
    }

    /// Set animation to be used by unit.
    /// \param startClip texture clip to begin animation at.
    /// \param endClip texture clip to end animation at.
    /// \param duration how long it takes animation to loop.
    protected void setAnimation(int startClip, int endClip, float duration) {

        int offset = (mPlayer - 1) * (endClip - startClip + 1);
        mTexture.setClip(startClip);
        mAnimation = new Animation(startClip + offset, endClip + offset, duration);
        mAnimation.setLoop(true);

    }

    /// Set movement and action status of unit.
    private void setStatus() {

        mHealthStatus.setText("HP: " + mHitPoints);
        String actionString = new String("");
        if (!mMoved) {
            actionString += "M";
        }
        if (!mActed) {
            actionString += "A";
        }
        mActionStatus.setText(actionString);

    }

    /// Create for carrying out movement.
    /// \param gameManager game to carry out move in.
    /// \return button that carries out movement or null if no movement possible.
    private Button createMoveButton(GameManager gameManager) {

        if (mMoveRange <= 0) {
            return null;
        }
        ArrayList<Coordinates> destinations = getDestinations();
        if (destinations.isEmpty()) {
            return null;
        }
        Consumer<Coordinates> move =
            (Coordinates c) -> {
                gameManager.move(mCoords, c);
            };
        Runnable setSelect = 
            () -> {
                gameManager.setSelectable(destinations, move);
            };
        return new ActionButton(mButtonTexture, "Move", Config.BUTTON_TEXT_SIZE, setSelect);

    }

    /// Create button to carry out attack.
    /// \param gameManager game to carry out attack in.
    /// \return button that carries out attack or null if no attack possible.
    private Button createAttackButton(GameManager gameManager) {

        // Cannot attack if no attack power.
        if (mAttack <= 0) {
            return null;
        }
        ArrayList<Coordinates> targets = getAttackTargets();
        Iterator<Coordinates> it = targets.iterator();
        while (it.hasNext()) {
            if (mGrid.getTile(it.next()).getUnit().isBusy()) {
                it.remove();
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        Consumer<Coordinates> attack =
            (Coordinates c) -> {
                gameManager.attack(mCoords, c);
            };
        Runnable setSelect = 
            () -> {
                gameManager.setSelectable(targets, attack);
            };
        return new ActionButton(mButtonTexture, "Attack", Config.BUTTON_TEXT_SIZE, setSelect);

    }

    /// Create button to capture building.
    /// \param gameManager game to carry out capture in.
    /// \return button that carries out capture or null if no capture possible.
    private Button createCaptureButton(GameManager gameManager) {

        Building building = mGrid.getTile(mCoords).getBuilding();
        if (building == null || building.getOwner() == mPlayer) {
            return null;
        }
        Runnable capture = 
            () -> {
                gameManager.capture(mCoords);
            };
        return new ActionButton(mButtonTexture, "Capture", Config.BUTTON_TEXT_SIZE, capture);

    }


    /// Get map of destinations unit can move to with paths to each destination.
    /// \return Map of destinations unit can move to with paths to each destination.
    private Map<Coordinates, ArrayList<Coordinates>> getPaths() {

        Map<Coordinates, ArrayList<Coordinates>> paths = new HashMap<Coordinates, ArrayList<Coordinates>>();

        Queue<PathData> unevaluated = new LinkedList<>();
        unevaluated.add(new PathData(0, mCoords));
        while (!unevaluated.isEmpty()) {

            PathData current = unevaluated.poll();
            if (current.cost <= mMoveRange) {

                if (!current.coords.equals(mCoords) && 
                    !paths.containsKey(current.coords) &&
                    mGrid.getTile(current.coords).getUnit() == null) {
                    paths.put(current.coords, current.path);
                }

                ArrayList<Coordinates> neighbours = mGrid.getNeighbourCoords(current.coords);
                Iterator<Coordinates> it = neighbours.iterator();
                while (it.hasNext()) {

                    Coordinates neighbour = it.next();
                    Tile tile = mGrid.getTile(neighbour);
                    Terrain terrain = tile.getTerrain();
                    Unit unit = tile.getUnit();
                    if ((unit == null || unit.getPlayer() == mPlayer) && 
                        terrain.isPassable(mMoveType)) {
                        unevaluated.add(
                            new PathData(current.cost + terrain.getMoveCost(mMoveType), 
                                         neighbour,
                                         current.path));
                    }
                }
            }
        }
        return paths;

    }

    /// Update move progress.
    /// \param delta time since last update.
    private void updateMove(float delta) {

        mActionProgress += delta;
        while (mMovePath != null && mActionProgress >= MOVE_DURATION) {
            
            mActionProgress -= MOVE_DURATION;
            mMoveCoords = mMovePath.get(0);
            mMovePath.remove(0);
            if (mMovePath.isEmpty()) {
                mMovePath = null;
                mMoveCoords = null;
                setTranslation(0f, 0f);
                mTexture.setRotation(0f);
                mBusy = false;
            }

        }
        if (mMovePath != null) {
            moveTransform();
        }

    }

    /// Update attack progress.
    /// \param delta time since last update.
    private void updateAttack(float delta) {

        mActionProgress += delta;
        while (mAttackCoords != null && mActionProgress >= ATTACK_DURATION) {

            mActionProgress -= ATTACK_DURATION;
            Unit defender = mGrid.getTile(mAttackCoords).getUnit();
            if (mAttackerStep) {

                if (!mAttacked) {

                    mAttacked = true;
                    defender.dealDamage(getModifiedAttack());

                } else {
                    
                    setTranslation(0f, 0f);
                    if (defender.isDestroyed() || defender.getAttack() == 0) {
                        finishAttack();
                    } else {
                        mAttacked = false;
                        mAttackerStep = false;
                    }

                }

            } else {

                if (!mAttacked) {
                    mAttacked = true;
                    dealDamage((int)(defender.getModifiedAttack() * COUNTER_ATTACK_MODIFIER));
                } else {
                    finishAttack();
                }
            }
        }
        if (mAttackCoords != null) {
            attackTransform();
        }

    }

    /// Complete attack by resetting attack tracking variables and resetting transform.
    private void finishAttack() {

        mBusy = false;
        mAttacked = false;
        mAttackerStep = true;
        Unit defender = mGrid.getTile(mAttackCoords).getUnit();
        defender.setTranslation(0f, 0f);
        defender.setBusy(false);
        mAttackCoords = null;

    }
    
    /// Translate and rotate unit based on move progress.
    private void moveTransform() {

        Coordinates target = mMovePath.get(0);
        Tile source = mGrid.getTile(mMoveCoords); 
        Tile destination = mGrid.getTile(target);

        if (mMoveCoords.y > target.y) {
            mTexture.setRotation(90f * mTexture.getXScale());
        } else if (mMoveCoords.y < target.y) {
            mTexture.setRotation(-90f * mTexture.getXScale());
        } else {
            mTexture.setRotation(0f);
        }

        if (mMoveCoords.x < target.x) {
            mTexture.setScale(-1f, 1f);
        } else if (mMoveCoords.x > target.x) {
            mTexture.setScale(1f, 1f);
        }
        
        PVector sourcePos = source.getTranslation();
        PVector distance = PVector.sub(destination.getTranslation(), sourcePos);

        float multiplier = mActionProgress / MOVE_DURATION;
        distance.x *= multiplier;
        distance.y *= multiplier;
        setTranslation(distance);

        Tile relative = mGrid.getTile(mCoords);
        PVector relativePos = relative.getTranslation();

        PVector relativeOffset = PVector.sub(sourcePos, relativePos);
        translate(relativeOffset);

    }

    /// Translate unit based on attack progress.
    private void attackTransform() {

        Tile attackerTile = mGrid.getTile(mCoords);
        Tile defenderTile = mGrid.getTile(mAttackCoords);

        PVector distance = PVector.sub(defenderTile.getTranslation(), attackerTile.getTranslation());

        float multiplier = mActionProgress / ATTACK_DURATION;
        multiplier /= 3f;
        if (mAttacked) {
            multiplier = ATTACK_DURATION - multiplier;
        }
        if (!mAttackerStep) {
            multiplier *= -1f;
        }
        distance.x *= multiplier;
        distance.y *= multiplier;

        if (mAttackerStep) {
            setTranslation(distance);
        } else {
            defenderTile.getUnit().setTranslation(distance);
        }

    }

}

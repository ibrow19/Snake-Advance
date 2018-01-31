package scene;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.function.Consumer;
import texture.TextureManager;
import grid.Grid;
import grid.Coordinates;
import grid.Tile;
import menu.Menu;
import menu.ActionButton;
import menu.Button;
import texture.Texture;
import unit.Unit;
import unit.Snake;
import unit.WheelSnake;
import unit.TankSnake;
import unit.JetSnake;
import unit.HeadQuarters;
import building.Building;
import building.Flag;
import terrain.Terrain;
import terrain.Road;
import terrain.Water;
import terrain.Plains;
import terrain.Mountain;
import rect.Rect;
import config.Config;
import gameobject.TextObject;
import controller.Controller;

/// Manages the playing of a round of the game. Provides interface for controllers to interact with
/// the game world.
public abstract class GameManager {

    /// Textures for displaying game.
    protected final TextureManager mTextureManager;

    /// Id of player who's turn it is.
    private int mActivePlayer;

    /// Status for each player.
    private final PlayerStatus[] mStatus;

    /// Controller for each player.
    private final Controller[] mControllers;

    /// Grid game is being played on.
    private final Grid mGrid;

    /// Menu with possible actions.
    private Menu mActionMenu;

    /// Menu managing end turn.
    private Menu mEndMenu;

    /// Button managing end turn.
    private Button mEndButton;

    /// Dialogue with start/end of round info.
    private InfoBox mInfo;

    /// Whether the game has finished.
    private boolean mGameOver;

    /// Currently highlighted tile.
    private Tile mHighlighted;

    /// List of coordinates that are currently valid for selection.
    private ArrayList<Coordinates> mSelectable;

    /// Action to carry out when a position is selected.
    private Consumer<Coordinates> mSelectAction;

    /// Initialise game.
    public GameManager(TextureManager textureManager, Controller[] controllers) {

        mTextureManager = textureManager;
        mActivePlayer = 1;
        mStatus = new PlayerStatus[Config.PLAYER_COUNT];
        mControllers = controllers;
        assert controllers.length == Config.PLAYER_COUNT;

        mActionMenu = null;
        mEndMenu = null;
        mEndButton = null;

        initEndMenu();
        initStatus();

        mGrid = new Grid();
        initTiles();
        setup();
        mGrid.alignTiles();

        mHighlighted = null;
        mSelectable = null;
        mSelectAction = null;

        mGameOver = false;
        setInfo(getInitialInfo());

    }

    // Initialisation functions to be implemented by subclasses.
    protected abstract void initTiles();
    protected abstract void setup();
    protected abstract String getInitialInfo();
    protected abstract String getEndInfo();

    /// Update the different parts of the game.
    public void update(PVector mousePos, float delta) {

        if (mInfo == null) {
            mControllers[mActivePlayer - 1].update(this, mousePos, delta);
        }

        mGrid.update(delta);
        if (!mGameOver) {

            for (int i = 0; i < mStatus.length && !mGameOver; ++i) {
                mStatus[i].update(delta);
                mGameOver = mStatus[i].hasLost();
            }
            if (mGameOver) {
                setInfo(getEndInfo());
            }

        }

    }

    /// Render the game.
    public void render(PApplet core) {

        mGrid.render(core);
        mEndMenu.render(core);
        for (int i = 0; i < mStatus.length; ++i) {
            mStatus[i].render(core);
        }
        if (mActionMenu != null) {
            mActionMenu.render(core);
        }
        if (mInfo != null) {
            mInfo.render(core);
        }

    }

    /// Check if the game has finished.
    public boolean gameIsOver() {
        return mGameOver && mInfo == null;
    }

    /// Get the next game/level.
    public GameManager getNext() {
        return null;
    }

    /// Get the status for a player.
    public PlayerStatus getStatus(int player) {
        return mStatus[player - 1];
    }

    /// Get the status of the active player.
    public PlayerStatus getActiveStatus() {
        return getStatus(mActivePlayer);
    }

    /// Get the status of non active player.
    public PlayerStatus getInactiveStatus() {
        return getStatus((mActivePlayer % mStatus.length) + 1);
    }

    /// Get the grid.
    public Grid getGrid() {
        return mGrid;
    }

    /// Highlight a position in the game.
    /// \param pos position to highlight.
    public void highlight(PVector pos) {

        if (mHighlighted != null) {
            mHighlighted.setHighlight(false);
            mHighlighted = null;
        }
        if (mInfo == null) {
            if (!mEndMenu.highlight(pos) && mActionMenu != null) {
                mActionMenu.highlight(pos);
            } else {
                highlightGrid(pos);
            }
        }

    }

    /// Handle a mouse click.
    /// \param pos position of mouse click.
    public void handleClick(PVector pos) {

        /// If there is an info dialogue, clear it and start first turn.
        if (mInfo != null) {
            mInfo = null;
            if (!mGameOver) {
                getStatus(mActivePlayer).startTurn();
            }

        /// Otherwise let controller handle click.
        } else {
            mControllers[mActivePlayer - 1].handleClick(this, pos); 
        }

    }

    /// Select a position in the game.
    /// \param pos position to select.
    public void select(PVector pos) {

        assert mInfo == null;

        // First attempt to select end turn button.
        if (!mEndMenu.select(pos)) {

            // If there is an action menu, attempt to select it then remove it.
            if (mActionMenu != null) {

                mActionMenu.select(pos);
                mActionMenu = null;

            // Otherwise, attempt to select tiles on the grid.
            } else {
                selectGrid(pos);
            }

        }

    }

    /// Set which tiles are selectable.
    /// \param selectable coordinates of selectable tiles.
    /// \param action action to be carried out when a tile is selected.
    public void setSelectable(ArrayList<Coordinates> selectable, 
                              Consumer<Coordinates> action) {

        mSelectable = selectable;
        mSelectAction = action;
        Iterator<Coordinates> it = selectable.iterator();
        while (it.hasNext()) {
            Tile current = mGrid.getTile(it.next());
            current.setSelectable(mActivePlayer);
        }

    }


    /// End the current turn, switch active player and controller.
    public void endTurn() {

        getStatus(mActivePlayer).endTurn();
        mActivePlayer %= Config.PLAYER_COUNT;
        ++mActivePlayer;
        getStatus(mActivePlayer).startTurn();
        mEndButton.setClip(mActivePlayer);
        mActionMenu = null;
        clear();

    }

    /// Move a unit.
    /// \param source position of unit to be moved.
    /// \param destination position to move unit to.
    public void move(Coordinates source, Coordinates destination) {

        Tile sourceTile = mGrid.getTile(source);
        Tile destTile = mGrid.getTile(destination);
        destTile.setUnit(sourceTile.getUnit());
        sourceTile.setUnit(null);

    }

    /// Carry out attack action.
    /// \param attackerPos position of attacker.
    /// \param defenderPos position of defender.
    public void attack(Coordinates attackerPos, Coordinates defenderPos) {

        Tile attackerTile = mGrid.getTile(attackerPos);
        Unit attacker = attackerTile.getUnit();
        attacker.attack(defenderPos);
        attacker.deactivate();

    }

    /// Capture a building.
    /// \param coords position of building to capture.
    public void capture(Coordinates coords) {

        Tile tile = mGrid.getTile(coords);
        tile.getBuilding().setOwner(mActivePlayer);
        tile.getUnit().deactivate();

    }

    /// Buy a snake.
    /// \param hq HeadQuarters carrying out buy action.
    /// \param coords position to place new unit.
    public void buySnake(HeadQuarters hq, Coordinates coords) {

        buy(hq, Snake.COST);
        addSnake(coords, hq.getPlayer());

    }

    /// Buy a wheel snake.
    /// \param hq HeadQuarters carrying out buy action.
    /// \param coords position to place new unit.
    public void buyWheelSnake(HeadQuarters hq, Coordinates coords) {

        buy(hq, WheelSnake.COST);
        addWheelSnake(coords, hq.getPlayer());

    }

    /// Buy a tank snake.
    /// \param hq HeadQuarters carrying out buy action.
    /// \param coords position to place new unit.
    public void buyTankSnake(HeadQuarters hq, Coordinates coords) {

        buy(hq, TankSnake.COST);
        addTankSnake(coords, hq.getPlayer());

    }

    /// Buy a jet snake.
    /// \param hq HeadQuarters carrying out buy action.
    /// \param coords position to place new unit.
    public void buyJetSnake(HeadQuarters hq, Coordinates coords) {

        buy(hq, JetSnake.COST);
        addJetSnake(coords, hq.getPlayer());

    }

    /// Add a plains tile to the grid.
    /// \param coords position to add tile.
    protected void addPlains(Coordinates coords) {

        Texture terrainTexture = mTextureManager.getTexture(Config.TERRAIN_TEXTURE_ID);
        addTile(coords, new Plains(terrainTexture));

    }

    /// Add a mountain tile to the grid.
    /// \param coords position to add tile.
    protected void addMountain(Coordinates coords) {

        Texture terrainTexture = mTextureManager.getTexture(Config.TERRAIN_TEXTURE_ID);
        addTile(coords, new Mountain(terrainTexture));

    }

    /// Add a water tile to the grid.
    /// \param coords position to add tile.
    protected void addWater(Coordinates coords) {

        Texture terrainTexture = mTextureManager.getTexture(Config.TERRAIN_TEXTURE_ID);
        addTile(coords, new Water(terrainTexture));

    }

    /// Add a road tile to the grid.
    /// \param coords position to add tile.
    /// \param straight whether the road is straight or a corner.
    /// \param rotations how many times the road should be rotated 90 degrees.
    protected void addRoad(Coordinates coords, boolean straight, int rotations) {

        Texture terrainTexture = mTextureManager.getTexture(Config.TERRAIN_TEXTURE_ID);
        Road road = new Road(terrainTexture, straight);
        road.setRotation(90f * rotations);
        addTile(coords, road);

    }

    /// Add an HQ to the grid.
    /// \param coords position to add HQ.
    /// \param owner owner of new HQ.
    protected void addHq(Coordinates coords, int owner) {

        Texture hqTexture = mTextureManager.getTexture(Config.HQ_TEXTURE_ID);
        Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        HeadQuarters hq = new HeadQuarters(hqTexture, buttonTexture, owner, mGrid);
        mGrid.getTile(coords).setUnit(hq);
        getStatus(owner).addHq(hq);

    }

    /// Add a Flag to the grid.
    /// \param coords position to add flag.
    protected void addFlag(Coordinates coords) {

        Texture flagTexture = mTextureManager.getTexture(Config.FLAG_TEXTURE_ID);
        Flag flag = new Flag(flagTexture);
        mGrid.getTile(coords).setBuilding(flag);
        for (int i = 0; i < mStatus.length; ++i) {
            mStatus[i].addFlag(flag);
        }

    }

    /// Add a Snake to the grid.
    /// \param coords position to add unit.
    /// \param owner owner of new unit.
    protected void addSnake(Coordinates coords, int owner) {

        Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        Texture snakeTexture = mTextureManager.getTexture(Config.SNAKE_TEXTURE_ID);
        Snake snake = new Snake(snakeTexture, buttonTexture, owner, mGrid);
        mGrid.getTile(coords).setUnit(snake);
        getStatus(owner).addUnit(snake);

    }

    /// Add a Wheel Snake to the grid.
    /// \param coords position to add unit.
    /// \param owner owner of new unit.
    protected void addWheelSnake(Coordinates coords, int owner) {

        Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        Texture wheelTexture = mTextureManager.getTexture(Config.WHEEL_SNAKE_TEXTURE_ID);
        WheelSnake wheel  = new WheelSnake(wheelTexture, buttonTexture, owner, mGrid);
        mGrid.getTile(coords).setUnit(wheel);
        getStatus(owner).addUnit(wheel);

    }

    /// Add a Tank Snake to the grid.
    /// \param coords position to add unit.
    /// \param owner owner of new unit.
    protected void addTankSnake(Coordinates coords, int owner) {

        Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        Texture tankTexture = mTextureManager.getTexture(Config.TANK_SNAKE_TEXTURE_ID);
        TankSnake tank = new TankSnake(tankTexture, buttonTexture, owner, mGrid);
        mGrid.getTile(coords).setUnit(tank);
        getStatus(owner).addUnit(tank);

    }

    /// Add a Jet Snake to the grid.
    /// \param coords position to add unit.
    /// \param owner owner of new unit.
    protected void addJetSnake(Coordinates coords, int owner) {

        Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        Texture jetTexture = mTextureManager.getTexture(Config.JET_SNAKE_TEXTURE_ID);
        JetSnake jet = new JetSnake(jetTexture, buttonTexture, owner, mGrid);
        mGrid.getTile(coords).setUnit(jet);
        getStatus(owner).addUnit(jet);

    }

    /// Carry out buy action. 
    /// \param hq HeadQuarters performing buy.
    /// \param cost points cost of buy.
    private void buy(HeadQuarters hq, int cost) {

        hq.deactivate();
        PlayerStatus status = getStatus(hq.getPlayer());
        assert status.getPoints() >= cost;
        status.subPoints(cost);

    }

    /// Add a new tile to the grid.
    /// \param coords position to add the tile.
    /// \param terrain terrain to use for the tile.
    private void addTile(Coordinates coords, Terrain terrain) {

        Texture selectTexture = mTextureManager.getTexture(Config.SELECTABLE_TEXTURE_ID);
        Texture highlightTexture = mTextureManager.getTexture(Config.HIGHLIGHT_TEXTURE_ID);
        Tile tile = new Tile(coords, terrain, selectTexture, highlightTexture);
        mGrid.setTile(coords, tile);

    }

    /// Clear game of any highlighted tiles.
    private void clear() {

        if (mSelectable != null) {

            Iterator<Coordinates> it = mSelectable.iterator();
            while (it.hasNext()) {
                Tile current = mGrid.getTile(it.next());
                current.deselect();
            }
            mSelectable = null;

        }

    }

    /// Select position on grid.
    /// \param pos selected position.
    private void selectGrid(PVector pos) {

        Tile selected = mGrid.getTile(pos); 
        if (selected != null && mSelectable != null) {
            selectSelectable(selected);
        } else if(selected != null) {
            selectTile(selected);
        }

    }

    /// Carry out action on selected tile then unhighlight selectable tiles.
    /// \param selected the tile that was selected.
    private void selectSelectable(Tile selected) {

        if (mSelectable.contains(selected.getCoords())) {
            mSelectAction.accept(selected.getCoords());
        }
        clear();

    }

    /// Get menu for selected tile.
    /// \param selected the tile that was selected.
    private void selectTile(Tile selected) {

        Unit unit = selected.getUnit();
        if (unit != null && unit.getPlayer() == mActivePlayer && !unit.isBusy()) {

            ArrayList<Button> actions = unit.getActions(this);
            Texture buttonTexture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
            String closeText = actions.isEmpty() ? "No Action" : "Close";
            actions.add(new Button(buttonTexture, closeText, Config.BUTTON_TEXT_SIZE));
            for (Button button : actions) {
                button.setClip(mActivePlayer);
            }
            mActionMenu = new Menu(actions, 0f);
            alignMenu(selected);

        }

    }

    /// Align menu next to tile ensuring menu does not got off screen.
    /// \param tile tile to align menu next to.
    private void alignMenu(Tile tile) {

        PVector centre = mGrid.getTranslation();
        mActionMenu.setTranslation(centre);
        mActionMenu.translate(tile.getXTranslation() * mGrid.getXScale(),
                       tile.getYTranslation() * mGrid.getYScale());

        Rect menuBounds = mActionMenu.getBounds();
        Rect tileBounds = tile.getBounds().transform(mGrid);

        float xOffset = menuBounds.width / 2f + tileBounds.width / 2f;
        float yOffset = menuBounds.height / 2f - tileBounds.height / 2f;

        if ((mActionMenu.getXTranslation() + xOffset + (menuBounds.width / 2f)) > Config.WINDOW_WIDTH) {
            xOffset = -xOffset;
        }
        if ((mActionMenu.getYTranslation() + yOffset + (menuBounds.height / 2f)) > Config.WINDOW_HEIGHT) {
            yOffset = -yOffset;
        }
        mActionMenu.translate(xOffset, yOffset);

    }

    /// Initialise end turn menu.
    private void initEndMenu() {

        Texture texture = mTextureManager.getTexture(Config.BUTTON_TEXTURE_ID);
        Runnable endAction =
            () -> {
                this.endTurn();
            };

        mEndButton = new ActionButton(texture, "End Turn", Config.BUTTON_TEXT_SIZE, endAction);
        mEndButton.setClip(mActivePlayer);
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(mEndButton);
        mEndMenu = new Menu(buttons, 0f);
        mEndMenu.setTranslation(Config.WINDOW_WIDTH / 2f, 25f);

    }

    /// Initialise player status.
    private void initStatus() {

        mStatus[0] = new PlayerStatus(Config.PLAYER1);
        mStatus[0].setTranslation(0f, 0f);
        mStatus[1] = new PlayerStatus(Config.PLAYER2);
        mStatus[1].setTranslation(Config.WINDOW_WIDTH / 3f * 2f, 0f);

    }


    /// Initialise info dialogue.
    /// \param text information to display. 
    private void setInfo(String text) {

        Texture infoTexture = mTextureManager.getTexture(Config.INFO_BACK_TEXTURE_ID);
        mInfo = new InfoBox(infoTexture, text);
        mInfo.setTranslation(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f);

    }

    /// Highlight a position in the grid.
    /// \param pos position to highlight.
    private void highlightGrid(PVector pos) {

        Tile highlighted = mGrid.getTile(pos);
        if (highlighted != null && 
            (mSelectable == null || mSelectable.contains(highlighted.getCoords()))) {

            mHighlighted = highlighted;
            mHighlighted.setHighlight(true);

        }

    }

}

package controller;

import processing.core.PVector;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Runnable;
import scene.GameManager;
import grid.Grid;
import grid.Coordinates;
import unit.Unit;
import unit.Snake;
import unit.WheelSnake;
import unit.JetSnake;
import unit.TankSnake;
import unit.HeadQuarters;
import random.Randomiser;
import scene.PlayerStatus;
import building.Building;
import building.Flag;


/// Controller that uses AI to control the game.
public class AIController extends Controller {

    /// Duration to wait before acting.
    private static final float WAIT_DURATION = 1f;

    /// Time passed waiting.
    private float mWaitProgress;

    /// Hqs that have not yet acted.
    private ArrayList<HeadQuarters> mWaitingHqs;

    /// Units that have not yet acted.
    private ArrayList<Unit> mWaitingUnits;

    /// Action to carry out next time unit is not busy.
    private Runnable mAction;

    /// Unit that must not be busy for action to be carried out.
    private Unit mDependent;

    /// Whether the last action of the current acting unit is being carried
    /// out/ has been completed.
    private boolean mLastAction;

    public AIController() {

        mWaitProgress = 0f;
        mWaitingHqs = null;
        mWaitingUnits = null;
        mAction = null;
        mDependent = null;
        mLastAction = false;

    }

    /// Update the game by using AI to determine actions..
    /// \param gameManager game to update.
    /// \param mousePos position at time of update.
    /// \param delta time since last update.
    public void update(GameManager gameManager, PVector mousePos, float delta) {

        // First wait set period before carrying out actions.
        if (mWaitingUnits == null) {

            mWaitProgress += delta;
            if (mWaitProgress >= WAIT_DURATION) {
                mWaitProgress = 0f;
                PlayerStatus status = gameManager.getActiveStatus();
                mWaitingHqs = status.getHqs();
                mWaitingUnits = status.getUnits();
            }

        // If there are waiting units carry out unit actions.
        } else if (!mWaitingUnits.isEmpty()) {

            updateUnits(gameManager);

        // If there are waiting hqs carry out hq actions.
        } else if (!mWaitingHqs.isEmpty()) {

            updateHqs(gameManager);

        // Once all actions are complete, end turn and reset controller.
        } else {

            gameManager.endTurn();
            mWaitProgress = 0f;
            mWaitingHqs = null;
            mWaitingUnits = null;
            mAction = null;
            mDependent = null;
            mLastAction = false;

        } 
    }

    /// Update waiting hqs.
    /// \param gameManager game to update.
    private void updateHqs(GameManager gameManager) {
        
        HeadQuarters hq = mWaitingHqs.get(0);
        ArrayList<Runnable> buildActions = getBuildActions(gameManager, hq);

        // Choose build action randomly (or no action).
        int index = Randomiser.randomInt(0, 3);
        if (index < buildActions.size()) {
            buildActions.get(index).run();
        }
        mWaitingHqs.remove(0);

    }

    /// Update waiting units.
    /// \param gameManager game to update.
    private void updateUnits(GameManager gameManager) {

        /// Carry out action for next unit if it is not busy.
        Unit unit = mWaitingUnits.get(0);
        if (!unit.isBusy()) {
            
            /// Finish acting for current unit.
            if (mLastAction) {

                completeUnitAction();

            // Carry out action if there is action and dependent not busy.
            } else if (mAction != null && (mDependent == null || !mDependent.isBusy())) {

                mAction.run();                
                mLastAction = true;

            // If no action assigned for current unit, determine an action to carry out
            // for that unit.
            } else if (mDependent == null) {

                determineUnitAction(gameManager, unit);

            }

        }

    }

    /// Complete action by removing unit and resetting action variables.
    private void completeUnitAction() {

        mWaitingUnits.remove(0);
        mAction = null;
        mDependent = null;
        mLastAction = false;

    }

    /// Determine action for unit to carry out.
    /// \param gameManager game to carry out action in.
    /// \param unit the unit to determine an action for.
    private void determineUnitAction(GameManager gameManager, Unit unit) {

        // Attempt to capture a nearby flag or attack a nearby unit.
        // If both attempts fail then try and move towards a nearby objective.
        ArrayList<Coordinates> destinations = unit.getDestinations();
        if (!attemptCapture(gameManager, unit, destinations) &&
            !attemptAttack(gameManager, unit, destinations)) {

            attemptMove(gameManager, unit, destinations);

        }

    }

    /// Attempt to attack a nearby enemy.
    /// \param gameManager game to carry out actions in.
    /// \param unit the unit attack with.
    /// \param destinations moves available to the unit.
    private boolean attemptAttack(GameManager gameManager, Unit unit,
                                  ArrayList<Coordinates> destinations) {

        // First check if attack possible without move.
        chooseTarget(gameManager, unit, unit.getAttackTargets());

        // Otherwise try and move then attack.
        Iterator<Coordinates> it = destinations.iterator();
        while (mAction == null && it.hasNext()) {
            Coordinates current = it.next();
            if (chooseTarget(gameManager, unit, unit.getAttackTargets(current))) {
                gameManager.move(unit.getCoords(), current);
            }
        }
        return mAction != null;

    }

    /// Choose a target and assign attack for a unit.
    /// \param gameManager game to carry out actions in.
    /// \param target the unit attack with.
    /// \param targets attacks available to the unit.
    /// \return whether an attack was assigned.
    private boolean chooseTarget(GameManager gameManager, Unit unit, 
                                 ArrayList<Coordinates> targets) {

        if (targets.isEmpty()) {
            return false;
        }
        Coordinates target = targets.get(Randomiser.randomInt(0, targets.size() - 1));
        mAction =
            () -> {
                gameManager.attack(unit.getCoords(), target); 
            };
        mDependent = gameManager.getGrid().getTile(target).getUnit();
        return true;

    }

    /// Attempt to capture a nearby flag.
    /// \param gameManager game to carry out actions in.
    /// \param unit the unit to capture with.
    /// \param destinations moves available to the unit.
    private boolean attemptCapture(GameManager gameManager, Unit unit,
                                   ArrayList<Coordinates> destinations) {

        // Attempt to capture flag on current position.
        chooseCapture(gameManager, unit, unit.getCoords());

        // Otherwise attempt to move and capture a flag.
        Iterator<Coordinates> it = destinations.iterator();
        while (mAction == null && it.hasNext()) {
            Coordinates current = it.next();
            if (chooseCapture(gameManager, unit, current)) {
                gameManager.move(unit.getCoords(), current);
            }
        }
        return mAction != null;

    }

    /// Assign capture for a unit.
    /// \param gameManager game to carry out actions in.
    /// \param coords the position to assign the capture to.
    /// \return whether a capture was successfully assigned.
    private boolean chooseCapture(GameManager gameManager, Unit unit, Coordinates coords) {

        Building building = gameManager.getGrid().getTile(coords).getBuilding();
        if (building != null && building.getOwner() != unit.getPlayer()) {
            mAction =
                () -> {
                    gameManager.capture(coords); 
                };
            return true;
        }
        return false;

    }

    /// Attempt to move towards a nearby objective.
    /// \param gameManager game to carry out actions in.
    /// \param unit the unit to move.
    /// \param destinations moves available to the unit.
    private void attemptMove(GameManager gameManager, Unit unit,
                             ArrayList<Coordinates> destinations) {

        // Get list of flags not owned by this player.
        PlayerStatus enemyStatus = gameManager.getInactiveStatus();
        ArrayList<Flag> flags = enemyStatus.getFlags();
        ArrayList<Coordinates> unownedPos = new ArrayList<>();
        for (Flag flag : flags) {
            if (flag.getOwner() != unit.getPlayer()) {
                unownedPos.add(flag.getCoords());
            }
        }

        // If unit's owner owns less than half of all flags move towards nearest flag.
        if (unownedPos.size() > (flags.size() / 2)) {

            moveTowardsNearest(gameManager, unit, destinations, unownedPos);
        
        // Otherwise, move towards nearest enemy hq.
        } else {

            ArrayList<HeadQuarters> enemyHqs = enemyStatus.getHqs();
            ArrayList<Coordinates> hqPos = new ArrayList<>();
            for (HeadQuarters hq : enemyHqs) {
                hqPos.add(hq.getCoords());
            }
            moveTowardsNearest(gameManager, unit, destinations, hqPos);

        }

        mLastAction = true;

    }

    /// Move unit towards closest target.
    /// \param gameManager game to carry out actions in.
    /// \param unit the unit to move.
    /// \param destinations moves available to the unit.
    /// \param targets to move towards.
    private void moveTowardsNearest(GameManager gameManager, Unit unit,
                                    ArrayList<Coordinates> destinations,
                                    ArrayList<Coordinates> targets) {

        // Get paths to targets, and find which path is shortest.
        ArrayList<Coordinates> shortest = null;
        for (Coordinates target : targets) {
            ArrayList<Coordinates> path = unit.getShortestPath(target);
            if (path != null && (shortest == null || shortest.size() > path.size())) {
                shortest = path;
            }
        }

        // Move unit as far along path as possible.
        if (shortest != null) {

            Coordinates moveTarget = null;
            for (int i = 0; i < shortest.size(); ++i) {

                Coordinates current = shortest.get(i);
                if (destinations.contains(current)) {
                    moveTarget = current;
                } 

            }
            if (moveTarget != null) {
                gameManager.move(unit.getCoords(), moveTarget);
            }

        }

    }
         
    /// Get a list of possible build actions.
    /// \param gameManager game that actions are for.
    /// \param hq hq that these actions are to be carried out by.
    private ArrayList<Runnable> getBuildActions(GameManager gameManager, HeadQuarters hq) {

        // Get possible build positions.
        ArrayList<Runnable> buildActions = new ArrayList<>();
        ArrayList<Coordinates> buildCoords = hq.getBuildCoords();
        if (buildCoords.isEmpty()) {
            return buildActions;
        }

        // Choose position to build new units.
        int index = Randomiser.randomInt(0, buildCoords.size() - 1);
        Coordinates buildTarget = buildCoords.get(index);

        // Add possible build actions.
        int points = gameManager.getActiveStatus().getPoints();
        if (points >= Snake.COST) {
            Runnable buySnake =
                () -> {
                    gameManager.buySnake(hq, buildTarget);
                };
            buildActions.add(buySnake);
        }
        if (points >= WheelSnake.COST) {
            Runnable buyWheel =
                () -> {
                    gameManager.buyWheelSnake(hq, buildTarget);
                };
            buildActions.add(buyWheel);
        }
        if (points >= TankSnake.COST) {
            Runnable buyTank =
                () -> {
                    gameManager.buyTankSnake(hq, buildTarget);
                };
            buildActions.add(buyTank);
        }
        if (points >= JetSnake.COST) {
            Runnable buyJet =
                () -> {
                    gameManager.buyJetSnake(hq, buildTarget);
                };
            buildActions.add(buyJet);
        }
        return buildActions;

    }

}

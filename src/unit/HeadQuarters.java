package unit;

import java.util.function.Consumer;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.Iterator;
import gameobject.TextureObject;
import texture.Texture;
import grid.Grid;
import grid.Coordinates;
import menu.Button;
import menu.ActionButton;
import scene.PlayerStatus;
import scene.GameManager;
import config.Config;


/// HeadQuarters that is capable of creating more snakes.
public class HeadQuarters extends Unit {

    private static final int MOVE_RANGE = 0;
    private static final int ATTACK = 0;
    private static final float DEFENSE = 0.6f;

    public HeadQuarters(Texture unitTexture, Texture buttonTexture,
                        int player, Grid grid) {

        super(unitTexture, buttonTexture, player,
              grid, ATTACK, DEFENSE, MOVE_RANGE, STANDARD_MOVE);

    }

    protected ArrayList<Button> getExtraActions(GameManager gameManager) {

        ArrayList<Button> buttons = new ArrayList<>();

        ArrayList<Coordinates> buildCoords = getBuildCoords();
        if (buildCoords.isEmpty()) {
            return buttons;
        }
        
        Button snakeButton = createSnakeButton(gameManager, buildCoords);
        Button wheelButton = createWheelButton(gameManager, buildCoords);
        Button tankButton = createTankButton(gameManager, buildCoords);
        Button jetButton = createJetButton(gameManager, buildCoords);
        if (snakeButton != null) {
            buttons.add(snakeButton);
        }
        if (wheelButton != null) {
            buttons.add(wheelButton);
        }
        if (tankButton != null) {
            buttons.add(tankButton);
        }
        if (jetButton != null) {
            buttons.add(jetButton);
        }

        return buttons;

    }

    public ArrayList<Coordinates> getBuildCoords() {

        ArrayList<Coordinates> neighbours = mGrid.getNeighbourCoords(mCoords);
        Iterator<Coordinates> it = neighbours.iterator();
        while (it.hasNext()) {
            Coordinates current = it.next();
            if (mGrid.getTile(current).getUnit() != null) {
                it.remove();
            }
        }
        return neighbours;

    }

    private Button createButton(GameManager gameManager,
                                Consumer<Coordinates> selectAction, 
                                ArrayList<Coordinates> buildCoords,
                                int cost,
                                String name) {

        PlayerStatus status = gameManager.getStatus(mPlayer);
        if (status.getPoints() < cost) {
            return null;
        }
        Runnable setSelect = 
            () -> {
                gameManager.setSelectable(buildCoords, selectAction);
            };
        return new ActionButton(mButtonTexture, name + " (" + cost + ")", 
                                Config.BUTTON_TEXT_SIZE, setSelect);

    }

    private Button createSnakeButton(GameManager gameManager, ArrayList<Coordinates> buildCoords) {

        Consumer<Coordinates> createSnake =
            (Coordinates c) -> {
                gameManager.buySnake(this, c);
            };
        return createButton(gameManager, createSnake, buildCoords, Snake.COST, "Snake");

    }

    private Button createWheelButton(GameManager gameManager, ArrayList<Coordinates> buildCoords) {

        Consumer<Coordinates> createWheel =
            (Coordinates c) -> {
                gameManager.buyWheelSnake(this, c);
            };
        return createButton(gameManager, createWheel, buildCoords, WheelSnake.COST, "Roller");

    }

    private Button createTankButton(GameManager gameManager, ArrayList<Coordinates> buildCoords) {

        Consumer<Coordinates> createTank =
            (Coordinates c) -> {
                gameManager.buyTankSnake(this, c);
            };
        return createButton(gameManager, createTank, buildCoords, TankSnake.COST, "Tank");

    }

    private Button createJetButton(GameManager gameManager, ArrayList<Coordinates> buildCoords) {

        Consumer<Coordinates> createJet =
            (Coordinates c) -> {
                gameManager.buyJetSnake(this, c);
            };
        return createButton(gameManager, createJet, buildCoords, JetSnake.COST, "Jet");

    }

}

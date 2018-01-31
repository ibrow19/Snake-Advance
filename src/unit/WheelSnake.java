package unit;

import gameobject.TextureObject;
import texture.Texture;
import java.util.ArrayList;
import grid.Grid;
import grid.Coordinates;
import menu.Button;

/// Vehicle type unit with long range movement and slightly above
/// average attack and defense modifier.
public class WheelSnake extends Unit {

    public static final int COST = 2000;

    private static int MOVE_RANGE = 6;
    private static int ATTACK = 60;
    private static float DEFENSE = 0.1f;

    private static int START_CLIP = 1;
    private static int END_CLIP = 12;
    private static float DURATION = 0.6f;

    public WheelSnake(Texture unitTexture, Texture buttonTexture,
                      int player, Grid grid) {

        super(unitTexture, buttonTexture, player,
              grid, ATTACK, DEFENSE, MOVE_RANGE, VEHICLE_MOVE);
        setAnimation(START_CLIP, END_CLIP, DURATION);

    }

}

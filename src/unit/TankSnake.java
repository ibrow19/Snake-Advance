package unit;

import gameobject.TextureObject;
import texture.Texture;
import java.util.ArrayList;
import grid.Grid;
import grid.Coordinates;
import menu.Button;

/// Vehicle type unit with limited move range but very high attack
/// and defense.
public class TankSnake extends Unit {

    public static final int COST = 3000;

    private static int MOVE_RANGE = 4;
    private static int ATTACK = 90;
    private static float DEFENSE = 0.4f;

    private static int START_CLIP = 1;
    private static int END_CLIP = 3;
    private static float DURATION = 0.3f;

    public TankSnake(Texture unitTexture, Texture buttonTexture,
                     int player, Grid grid) {

        super(unitTexture, buttonTexture, player,
              grid, ATTACK, DEFENSE, MOVE_RANGE, VEHICLE_MOVE);
        setAnimation(START_CLIP, END_CLIP, DURATION);

    }

}

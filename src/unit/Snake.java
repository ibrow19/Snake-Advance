package unit;

import gameobject.TextureObject;
import texture.Texture;
import java.util.ArrayList;
import grid.Grid;
import grid.Coordinates;
import menu.Button;

/// Standard unit with standard movement and attack and no defense modifier.
public class Snake extends Unit {

    public static final int COST = 1000;

    private static int MOVE_RANGE = 3;
    private static int ATTACK = 50;
    private static float DEFENSE = 0f;

    private static int START_CLIP = 1;
    private static int END_CLIP = 6;
    private static float DURATION = 1.3f;

    public Snake(Texture unitTexture, Texture buttonTexture,
                 int player, Grid grid) {

        super(unitTexture, buttonTexture, player,
              grid, ATTACK, DEFENSE, MOVE_RANGE, STANDARD_MOVE);
        setAnimation(START_CLIP, END_CLIP, DURATION);

    }


}

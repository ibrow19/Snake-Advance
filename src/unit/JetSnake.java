package unit;

import gameobject.TextureObject;
import texture.Texture;
import java.util.ArrayList;
import grid.Grid;
import grid.Coordinates;
import menu.Button;

/// Flying unit with unrestricted movement but only standard
/// attack and no extra defense modifier.
public class JetSnake extends Unit {

    public static final int COST = 3000;

    private static int MOVE_RANGE = 5;
    private static int ATTACK = 50;
    private static float DEFENSE = 0f;

    private static int START_CLIP = 1;
    private static int END_CLIP = 3;
    private static float DURATION = 0.7f;

    public JetSnake(Texture unitTexture, Texture buttonTexture,
                    int player, Grid grid) {

        super(unitTexture, buttonTexture, player,
              grid, ATTACK, DEFENSE, MOVE_RANGE, AIR_MOVE);
        setAnimation(START_CLIP, END_CLIP, DURATION);

    }

}

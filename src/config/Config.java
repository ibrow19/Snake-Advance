package config;

import java.lang.String;
import rect.Rect;
import colour.Colour;

/// General constants used for the game.
public class Config {

    // Window parameters.
    public static final int WINDOW_WIDTH = 750;
    public static final int WINDOW_HEIGHT = 800;

    public static final int BUTTON_TEXT_SIZE = 20;

    public static final int PLAYER_COUNT = 2;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    public static final Colour[] PLAYER_COLOURS = {new Colour(0, 0, 0),
                                                   new Colour(255, 25, 100),
                                                   new Colour(25, 25, 200)};

    // Texture identifiers.
    public static final String TITLE_TEXTURE_ID = new String("title");
    public static final String TITLE_SNAKE_TEXTURE_ID = new String("titlesnake");

    public static final String BUTTON_TEXTURE_ID = new String("button");

    public static final String INFO_BACK_TEXTURE_ID = new String("infoback");

    public static final String HIGHLIGHT_TEXTURE_ID = new String("highlight");
    public static final String SELECTABLE_TEXTURE_ID = new String("selectable");

    public static final String TERRAIN_TEXTURE_ID = new String("terrain");
    public static final String FLAG_TEXTURE_ID = new String("flag");

    public static final String HQ_TEXTURE_ID = new String("HQ");
    public static final String SNAKE_TEXTURE_ID = new String("snake");
    public static final String WHEEL_SNAKE_TEXTURE_ID = new String("wheelsnake");
    public static final String TANK_SNAKE_TEXTURE_ID = new String("tanksnake");
    public static final String JET_SNAKE_TEXTURE_ID = new String("jetsnake");

}

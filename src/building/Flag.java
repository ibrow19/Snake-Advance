package building;

import texture.Texture;

/// Flag building that grants points to the player.
public class Flag extends Building {

    public final static int POINTS_VALUE = 500;
    private final static float BOOST = 0.2f;
    private final static float PROTECTION = 0.3f;

    /// Initialise flag.
    public Flag(Texture texture) {
        super(texture, BOOST, PROTECTION);
    }

}

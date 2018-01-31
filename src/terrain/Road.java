package terrain;

import texture.Texture;

/// Road that is easy to move over for all unit move types. Offers no
/// cover defense in combat.
public class Road extends Terrain {

    private static int STRAIGHT_CLIP = 2;
    private static int CORNER_CLIP = 3;
    private static int[] MOVE_COSTS = {1, 1, 1};
    private static float COVER_MULTIPLIER = 0f;

    public Road(Texture texture, boolean straight) {

        super(texture, (straight ? STRAIGHT_CLIP : CORNER_CLIP), MOVE_COSTS, COVER_MULTIPLIER);

    }

}

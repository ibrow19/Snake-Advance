package terrain;

import texture.Texture;

/// Terrain impassable by all but air units. Offers no cover
/// defense in combat.
public class Water extends Terrain {

    private static int CLIP = 5;
    private static int[] MOVE_COSTS = {-1, -1, 1};
    private static float COVER_MULTIPLIER = 0f;

    public Water(Texture texture) {

        super(texture, CLIP, MOVE_COSTS, COVER_MULTIPLIER);

    }

}

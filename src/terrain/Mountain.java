package terrain;

import texture.Texture;

/// Terrain causing movement issues for standard move type units and impassable
/// for vehicle move type units. Offers significant cover in combat.
public class Mountain extends Terrain {

    private static int CLIP = 4;
    private static int[] MOVE_COSTS = {2, -1, 1};
    private static float COVER_MULTIPLIER = 0.5f;

    public Mountain(Texture texture) {

        super(texture, CLIP, MOVE_COSTS, COVER_MULTIPLIER);

    }

}

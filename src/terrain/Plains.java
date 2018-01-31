package terrain;

import texture.Texture;

/// Terrain causing movement issues for vehicles and offers slight cover
/// defense in combat.
public class Plains extends Terrain {

    private static int CLIP = 1;
    private static int[] MOVE_COSTS = {1, 2, 1};
    private static float COVER_MULTIPLIER = 0.1f;

    public Plains(Texture texture) {

        super(texture, CLIP, MOVE_COSTS, COVER_MULTIPLIER);

    }

}

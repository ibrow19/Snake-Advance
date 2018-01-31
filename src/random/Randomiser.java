package random;

import processing.core.PVector;
import java.util.Random;
import rect.Rect;

/// Class managing random operations.
public class Randomiser {

    /// Random number generator.
    private static Random mRandom;

    /// Initialise random number generator.
    static {

        mRandom = new Random();

    }

    /// Generate a random integer in the specified range.
    /// \param min the minimum value that can be generated.
    /// \param max the maximum value that can be generated.
    /// \return a random int a least min and no more than max.
    public static int randomInt(int min, int max) {

        return mRandom.nextInt((max - min) + 1) + min;

    }

    /// Generate a random float in the specified range.
    /// \param min the minimum value that can be generated.
    /// \param max the maximum value that can be generated.
    /// \return a random float a least min and no more than max.
    public static float randomFloat(float min, float max) {

        return min + mRandom.nextFloat() * (max - min);

    }

    /// Generate a random point in a rectangle.
    /// \param bounds rectangle to generate a point within.
    /// \return a random point within bounds.
    public static PVector randomPoint(Rect bounds) {
        
        float x = randomFloat(bounds.x, bounds.x + bounds.width);
        float y = randomFloat(bounds.y, bounds.y + bounds.height);
        return new PVector(x, y);

    }

}

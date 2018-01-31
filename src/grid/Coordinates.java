package grid;

import processing.core.PVector;
import java.lang.String;

/// Grid coordinates.
public final class Coordinates {

    /// x coordinate.
    public int x;

    // y coordinate.
    public int y;

    /// Initialise coordinates.
    public Coordinates(int x, int y) {

        this.x = x;
        this.y = y;

    }

    /// Get a copy of the coordinates.
    /// \return a copy of the coordinates.
    public Coordinates copy() {

        return new Coordinates(x, y);

    }

    /// Check if two coordinates are equal.
    /// \param other coordinate to check equality with.
    /// \return whether the two coordinates are equal.
    public boolean equals(Object other) {

        boolean result = false;
        if (other instanceof Coordinates) {
            
            Coordinates coord = (Coordinates)other;
            result = (x == coord.x) &&
                     (y == coord.y);

        }
        return result;

    }

    /// Get the distance between this coordinate and another.
    /// \param other the coordinate to check the distance to.
    /// \return the distance between these coordinates and other.
    public float distance(Coordinates other) {

        PVector distance = new PVector(other.x - x, other.y - y);
        return distance.mag();

    }

    public String toString() {

        return "x: " + x + " y: " + y;

    }

    public int hashCode() {

        return x * -2 + y * 3;

    }

}

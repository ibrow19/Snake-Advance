package unit;

import java.util.ArrayList;
import grid.Coordinates;

final class PathData {

    public final int cost;
    public final Coordinates coords;
    public final ArrayList<Coordinates> path;

    public PathData(int cost, Coordinates coords) {

        this.cost = cost;
        this.coords = coords;
        this.path = new ArrayList<>();

    }

    public PathData(int cost, Coordinates coords, ArrayList<Coordinates> previousPath) {

        this.cost = cost;
        this.coords = coords;
        this.path = new ArrayList<>();
        this.path.addAll(previousPath);
        this.path.add(coords);

    }

}

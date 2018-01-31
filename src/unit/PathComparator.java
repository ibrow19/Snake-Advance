package unit;

import java.util.Comparator;

class PathComparator implements Comparator<PathCost> {

    public int compare(PathCost o1, PathCost o2) {

        if (o1.cost < o2.cost) {
            return -1;
        }
        if (o1.cost > o2.cost) {
            return 1;
        }
        return 0;

    }

}

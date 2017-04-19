package pathfinding;


import java.util.List;

public class Path {
    private final static double FEET_PER_PIXEL = 0.2902;
    private final static double SECONDS_PER_FOOT = 0.2975;
    private final static double STEPS_PER_FOOT = 0.5157;

    private List<MapNode> path;

    public Path(List<MapNode> path) {
        this.path = path;
    }

    private double lengthInPixels() {
        double totalLength = 0;
        MapNode currentNode = null;
        for (MapNode n : path) {
            totalLength += currentNode == null ? 0 : currentNode.distanceTo(n);
            currentNode = n;
        }
        return totalLength;
    }

    public double distanceInFeet() {
        return lengthInPixels() * FEET_PER_PIXEL;
    }

    public double timeInSeconds() {
        return distanceInFeet() * SECONDS_PER_FOOT;
    }

    public double distanceInSteps() {
        return distanceInFeet() * STEPS_PER_FOOT;
    }
}

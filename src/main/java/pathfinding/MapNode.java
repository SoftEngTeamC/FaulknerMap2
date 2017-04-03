package pathfinding;

import java.awt.*;
import java.util.Collection;
import java.util.Map;


public abstract class MapNode {
    private Point position;
    private int floor;
    private Map<MapNode, Double> neighbors;  // Weighted adjacency list

    protected Point getPosition() {
        return position;
    }

    Collection<MapNode> getNeighbors() {
        return neighbors.keySet();
    }

    protected double heuristicDistanceTo(MapNode otherNode) {
        return Math.sqrt(Math.pow(position.x - otherNode.position.x, 2) + Math.pow(position.y - otherNode.position.y, 2));
    }

    protected double distanceTo(MapNode otherNode) {
        return neighbors.get(otherNode);
    }
}
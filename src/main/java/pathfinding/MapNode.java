package pathfinding;

import java.awt.*;
import java.util.Collection;
import java.util.Map;


public class MapNode {
    private Point position;
    private int floor;
    private Map<MapNode, Double> neighbors;  // Weighted adjacency list

    /**
     * @return the position of the node
     */
    protected Point getPosition() {
        return position;
    }

    /**
     * @return the node's neighbors
     */
    Collection<MapNode> getNeighbors() {
        return neighbors.keySet();
    }

    /**
     * Calculate the euclidean to the otherNode
     * @param otherNode the node to calculate the distance to
     * @return the euclidean distance to otherNode
     */
     double heuristicDistanceTo(MapNode otherNode) {
        return Math.sqrt(Math.pow(position.x - otherNode.position.x, 2) + Math.pow(position.y - otherNode.position.y, 2));
    }

    /**
     * Get the weight (distance) of the edge connecting this and otherNode
     * @param otherNode a neighboring node
     * @return the distance to otherNode
     */
     double distanceTo(MapNode otherNode) {
        return neighbors.get(otherNode);
    }
}
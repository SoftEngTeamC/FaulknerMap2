package pathfinding;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MapNode {
    private Point position;
    private int floor;
    private Map<MapNode, Double> neighbors;  // Weighted adjacency list

    /**
     * Create a map node with no neighbors at position on floor
     * @param position the coordinates of the node
     * @param floor which floor the node is on
     */
    public MapNode(Point position, int floor) {
        this.position = position;
        this.floor = floor;
        this.neighbors = new HashMap<>();
    }

    /**
     * Create a map node with neighbors
     * @param position the coordinates of the node
     * @param floor the floor of the node
     * @param neighbors a map containing each node and its distance from this node
     */
    public MapNode(Point position, int floor, Map<MapNode, Double> neighbors) {
        this.position = position;
        this.floor = floor;
        this.neighbors = neighbors;
    }

    /**
     * Add a neighbor to this node
     * @param n the neighboring node
     * @param distance the distance to the neighboring node
     */
    public void addNeighbor(MapNode n, double distance) {
        this.neighbors.put(n, distance);
    }

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
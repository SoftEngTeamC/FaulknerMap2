package pathfinding;

import java.awt.geom.Point2D;
import java.util.*;


public class MapNode {
    private Point2D.Double position;
    private int floor;
    private Collection<MapNode> neighbors;

    /**
     * Create a map node with no neighbors at position on floor
     * @param position the coordinates of the node
     * @param floor which floor the node is on
     */
    public MapNode(Point2D.Double position, int floor) {
        this.position = position;
        this.floor = floor;
        this.neighbors = new HashSet<>();
    }

    /**
     * Create a map node with neighbors
     * @param position the coordinates of the node
     * @param floor the floor of the node
     * @param neighbors a map containing each node and its distance from this node
     */
    public MapNode(Point2D.Double position, int floor, Collection<MapNode> neighbors) {
        this.position = position;
        this.floor = floor;
        this.neighbors = neighbors;
    }

    /**
     * Add a neighbor to this node
     * @param n the neighboring node
     */
    public void addNeighbor(MapNode n) {
        this.neighbors.add(n);
        n.neighbors.add(this);
    }

    /**
     * Remove the given neighbor
     * @param n the neighbor
     */
    public void removeNeighbor(MapNode n) {
        this.neighbors.remove(n);
    }

    /**
     * @return the position of the node
     */
    protected Point2D.Double getPosition() {
        return position;
    }

    /**
     * @return which floor this node is on
     */
    public int getFloor() {
        return floor;
    }

    /**
     * @return the node's neighbors
     */
    public Collection<MapNode> getNeighbors() {
        return this.neighbors;
    }

    /**
     * Calculate the euclidean to the otherNode
     * @param otherNode the node to calculate the distance to
     * @return the euclidean distance to otherNode
     */
     double distanceTo(MapNode otherNode) {
         // TODO: Give a warning if the nodes are on different floors
         return Math.sqrt(Math.pow(position.x - otherNode.position.x, 2) + Math.pow(position.y - otherNode.position.y, 2));
    }
}
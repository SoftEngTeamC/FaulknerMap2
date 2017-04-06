package pathfinding;

import model.Coordinate;

import java.util.HashSet;
import java.util.Set;


public class MapNode implements Node<MapNode> {
    private Coordinate location;
    private Set<MapNode> neighbors;

    public MapNode(Coordinate location) {
        this.location = location;
        this.neighbors = new HashSet<>();
    }

    public MapNode(Coordinate location, Set<MapNode> neighbors) {
        this.location = location;
        this.neighbors = neighbors;
    }


    public MapNode(model.Node n) {
        this.location = n.getLocation();
        this.neighbors = new HashSet<>();
    }

    public double heuristicCost(MapNode goal) {
        return distanceTo(goal);
    }

    public double traversalCost(MapNode neighbor) {
        return distanceTo(neighbor);
    }

    public Set<MapNode> neighbors() {
        return this.neighbors;
    }

    public double distanceTo(MapNode n) {
        double xDelta = this.location.getX() - n.location.getX();
        double yDelta = this.location.getY() - n.location.getY();
        return Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }

    public Coordinate getLocation() {
        return location;
    }

    public void addNeighbor(MapNode n) {
        neighbors.add(n);
    }
}
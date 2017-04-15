package pathfinding;

import model.Coordinate;
import service.CoordinateService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapNode implements Node<MapNode> {
    public final static double FEET_PER_PIXEL = 0.2902;
    public final static double SECONDS_PER_FOOT = 0.2975;
    public final static double STEPS_PER_FOOT = 0.5157;
    private Set<MapNode> neighbors;
    private model.Node modelNode;
    private Coordinate location;

    public MapNode(model.Node n) {
        this.neighbors = new HashSet<>();
        this.modelNode = n;
        this.location = n.getLocation();
    }

    public static double getPixels(List<MapNode> myPath) {
        int totalDistance = 0;
        for(int i = 0; i < myPath.size()-2;i++){ //Go until 1 before the end
            totalDistance += myPath.get(i).distanceTo(myPath.get(i+1));
        }
        return totalDistance;
    }

    public static double getFeet(List<MapNode> myPath){
        return getPixels(myPath)* FEET_PER_PIXEL;
    }

    public static double getSeconds(List<MapNode> myPath){
        return getFeet(myPath) * SECONDS_PER_FOOT;
    }

    public static double getSteps(List<MapNode> myPath){
        return getFeet(myPath) * STEPS_PER_FOOT;
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
        double xDelta = location.getX() - n.getLocation().getX();
        double yDelta = location.getY() - n.getLocation().getY();
        return Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }

    public Coordinate getLocation() {
        return location;
    }

    public void addNeighbor(MapNode n) {
        neighbors.add(n);
        n.neighbors.add(this);
    }

    public model.Node getModelNode() {
        return modelNode;
    }
}
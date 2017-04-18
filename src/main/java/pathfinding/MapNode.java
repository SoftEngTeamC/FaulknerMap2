package pathfinding;

import model.Coordinate;
import service.NodeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapNode implements Node<MapNode> {
    public final static double FEET_PER_PIXEL = 0.2902;
    public final static double SECONDS_PER_FOOT = 0.2975;
    public final static double STEPS_PER_FOOT = 0.5157;
    private final double ELEVATOR_COST = 0.1;

    private Set<MapNode> neighbors = new HashSet<>();
    private model.Node modelNode;
    private Coordinate location;

    public MapNode(Coordinate location) {
        this.location = location;
        this.modelNode = null;
    }

    public MapNode(model.Node n) {
        this.modelNode = n;
        this.location = n.getLocation();
    }

    public static double getPixels(List<MapNode> myPath) {
        int totalDistance = 0;
        for(int i = 0; i < myPath.size()-1;i++){ //Go until 1 before the end
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
        int floorDifference = this.location.getFloor() - goal.getLocation().getFloor();
        if (floorDifference != 0) {
            Coordinate closestElevatorLocation = closestElevator().getLocation();
            return distanceTo(closestElevatorLocation) + goal.distanceTo(closestElevatorLocation) + ELEVATOR_COST * floorDifference;
        }
        return distanceTo(goal);
    }

    public double traversalCost(MapNode neighbor) {
        // Make elevators take more than 0 cost so that we don't take elevators to a different floor
        int floorDifference = this.location.getFloor() - neighbor.getLocation().getFloor();
        if (floorDifference != 0) {
            return Math.abs(floorDifference) * ELEVATOR_COST;
        }

        return distanceTo(neighbor);
    }

    public Set<MapNode> neighbors() {
        return this.neighbors;
    }

    public double distanceTo(MapNode n) {
        return distanceTo(n.getLocation());
    }

    private double distanceTo(Coordinate otherLocation) {
        double xDelta = location.getX() - otherLocation.getX();
        double yDelta = location.getY() - otherLocation.getY();
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

    private model.Node closestElevator() {
        NodeService nodeService = new NodeService();
        List<model.Node> elevators = nodeService.getElevatorNodes();
        model.Node closestElevator = elevators.get(0); // TODO: Don't assume existence of elevators
        for (model.Node e : elevators) {
            if (distanceTo(e.getLocation()) < distanceTo(closestElevator.getLocation())) {
                closestElevator = e;
            }
        }
        return closestElevator;
    }

    public String toString() {
        return modelNode.getName();
    }
}
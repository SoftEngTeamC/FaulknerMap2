package pathfinding;

import model.Coordinate;
import service.CoordinateService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MapNode implements Node<MapNode> {
    static double FEET_PER_PIXEL = 0.2902;
    static double SECONDS_PER_FOOT = 0.2975;
    static double STEPS_PER_FOOT = 0.5157;
    private long locationID;
    private Set<MapNode> neighbors;
    private long modelNodeID;

    public MapNode(Coordinate location) {
        this.locationID = location.getId();
        this.neighbors = new HashSet<>();
    }

    public MapNode(Coordinate location, Set<MapNode> neighbors) {
        this.locationID = location.getId();
        this.neighbors = neighbors;
    }


    public MapNode(model.Node n) {
        this.locationID = n.getLocation().getId();
        this.neighbors = new HashSet<>();
        this.modelNodeID = n.getId();
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
        CoordinateService cs = new CoordinateService();
        Coordinate location = cs.find(locationID);
        double xDelta = location.getX() - n.getLocation().getX();
        double yDelta = location.getY() - n.getLocation().getY();
        return Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }

    public Coordinate getLocation() {
        CoordinateService cs = new CoordinateService();
        return cs.find(locationID);
    }

    public void addNeighbor(MapNode n) {
        neighbors.add(n);
        n.neighbors.add(this);
    }

    public long getModelNodeID() {
        return modelNodeID;
    }

    public void setLocationID(long id){
        this.locationID = id;
    }

}
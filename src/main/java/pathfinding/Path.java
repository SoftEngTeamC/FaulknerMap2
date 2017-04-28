package pathfinding;


import model.Edge;
import service.EdgeService;

import java.util.*;
import java.util.stream.Collectors;

public class Path implements Iterable<MapNode> {
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

    public MapNode getNode(int i) {
        return path.get(i);
    }

    public List<MapNode> getPath(){return path;}

    public List<Edge> edges() {
        EdgeService edgeService = new EdgeService();

        List<Edge> edges = new LinkedList<>();
        MapNode currentNode = null;
        for (MapNode n : path) {
            if (currentNode != null) {
                Edge edge = edgeService.findByNodes(currentNode.getModelNode(), n.getModelNode());
                edges.add(edge);
            }
            currentNode = n;
        }
        return edges;
    }

    public List<Edge> edgesOnFloor(int floor) {
        return edges().stream()
                .filter(
                        edge -> edge.getStart().getLocation().getFloor() == floor
                                && edge.getStart().getLocation().getFloor() == floor)
                .collect(Collectors.toList());
    }

    /**
     * @return a set of the floors spanned by this path
     */
    public Set<Integer> floorsSpanned() {
        Set<Integer> floors = new HashSet<>();
        for (MapNode n : path) {
            floors.add(n.getLocation().getFloor());
        }
        return floors;
    }

    public Set<Integer> floorsNotSpanned() {
        Set<Integer> floors = floorsSpanned();
        Set<Integer> notFloors = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        notFloors.removeAll(floors);
        return new HashSet<>(notFloors);
    }

    public List<Path> groupedByFloor() {
        List<Path> subPaths = new LinkedList<>();
        List<MapNode> currentSubPath = new LinkedList<>();
        MapNode prevNode = null;
        for (MapNode node : path) {
            if (prevNode != null && node.getLocation().getFloor() != prevNode.getLocation().getFloor()) {
                subPaths.add(new Path(currentSubPath));
                currentSubPath = new LinkedList<>();
            } else {
                currentSubPath.add(node);
            }
            prevNode = node;
        }
        return subPaths;
    }

    /**
     * @return if the path is empty
     */
    public boolean isEmpty() {
        return path == null || path.isEmpty();
    }

    public int numNodes() {
        return path.size();
    }

    @Override
    public Iterator<MapNode> iterator() {
        return path.iterator();
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
